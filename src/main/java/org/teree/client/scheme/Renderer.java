package org.teree.client.scheme;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.NodeInterface;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.Node.NodeLocation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * The new nodes won't be here inserted ... it has to be done in Scene 
 * 
 * @author ibek
 *
 */
public abstract class Renderer<T extends Widget & NodeInterface> {

	public static final int MARGIN = 25;
	public static final int CURVENESS = 20;
	public static final int ELLIPSE_OVERFLOW_WIDTH = 44;
	
	protected static final int LEFT = 0;
	protected static final int RIGHT = 1;

    public void renderEditor(final Canvas canvas, final List<T> nodes, final Node root) {
    	prepare(nodes);
    	Scheduler.get().scheduleDeferred(new ScheduledCommand() { // to ensure that widget automatically resized size is already set
            @Override
            public void execute() {
                boolean succ = resize(nodes);
                if(!succ){ // some node is too wide
                    resize(nodes);
                }
            	render(canvas, nodes, root, false, true);
            }
        });
    }

    public void renderViewer(final Canvas canvas, final List<T> nodes, final Node root) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() { // to ensure that widget automatically resized size is already set
            @Override
            public void execute() {
                render(canvas, nodes, root, false, false);
            }
        });
    }

    /**
     * 
     * @param canvas
     * @param nodes
     * @param root
     * @return root x,y coordinates
     */
    public int[] renderPicture(Canvas canvas, final List<T> nodes, final Node root) {
        prepare(nodes);
        boolean succ = resize(nodes);
        if(!succ){ // some node is too wide
            resize(nodes);
        }
        return render(canvas, nodes, root, true, false);
    }
    
    /**
     * Generate scheme.
     * 
     * @param canvas
     * @param nodes
     * @param root
     * @param makePicture generate the map into canvas to be transformed into picture
     * @param editable
     * @return root x,y coordinates
     */
    protected abstract int[] render(Canvas canvas, List<T> nodes, Node root, boolean makePicture, boolean editable);
    
    protected abstract int getWidth(T rw, int leftw, int rightw);
    
    protected abstract int getHeight(T rw, int lefth, int righth);
	
	/**
	 * Sets left and right bounds and width of left and right part of tree
	 * @param nodes
	 * @param root
	 * @param leftBounds leftBounds.get(col).get(row)
	 * @param rightBounds
	 * @param widths [0]-leftw, [1]-rightw
	 */
	protected void setBoundsAndWidths(List<T> nodes, Node root, List<List<Integer>> leftBounds, 
			List<List<Integer>> rightBounds, Integer[] widths) {

		List<Node> rootcn = root.getChildNodes();

		int id = 1; // identifier for node widgets
		int leftLevel = 0;
		int rightLevel = 0;
		
		// set bounds for left and right nodes
		for (int i = 0; rootcn != null && i < rootcn.size(); ++i) {
			Node n = rootcn.get(i);
			T nc = nodes.get(id);

			if (n.getLocation() == NodeLocation.LEFT && n.getChildNodes() != null) {
				
				BoundsData<T> data = new BoundsData<T>(nodes, leftBounds, nc.getOffsetWidth());
				
				int h = 0;
				if (!nc.isCollapsed()) {
					// don't worry, the current node is set in setBounds
					h = setBounds(data, n.getChildNodes(), nc.getOffsetWidth(), data.getMaxWidthLevel(), id + 1);
				}
				if (nc.getOffsetHeight() > h) {
					h = nc.getOffsetHeight();
				}
				leftBounds.get(0).add(h);
				id += n.getNumberOfChildNodes() + 1;
				if (data.getMaxWidth() > widths[LEFT]) {
					widths[LEFT] = data.getMaxWidth();
					leftLevel = data.getMaxWidthLevel() + 1;
				}
			} else if (n.getChildNodes() != null) { // right
				
				BoundsData<T> data = new BoundsData<T>(nodes, rightBounds, nc.getOffsetWidth());
				
				int h = 0;
				if (!nc.isCollapsed()) {
					// don't worry, the current node is set in setBounds
					h = setBounds(data, n.getChildNodes(), nc.getOffsetWidth(), data.getMaxWidthLevel(), id + 1);
				}
				if (nc.getOffsetHeight() > h) {
					h = nc.getOffsetHeight();
				}
				rightBounds.get(0).add(h);
				id += n.getNumberOfChildNodes() + 1;
				if (data.getMaxWidth() > widths[RIGHT]) {
					widths[RIGHT] = data.getMaxWidth();
					rightLevel = data.getMaxWidthLevel() + 1;
				}
				
			} else if (n.getLocation() == NodeLocation.LEFT) {
				
				leftBounds.get(0).add(nc.getOffsetHeight());
				id++;
				if (widths[LEFT] < nc.getOffsetWidth()) {
					widths[LEFT] = nc.getOffsetWidth();
					leftLevel = 1;
				}
				
			} else {
				
				rightBounds.get(0).add(nc.getOffsetHeight());
				id++;
				if (widths[RIGHT] < nc.getOffsetWidth()) {
					widths[RIGHT] = nc.getOffsetWidth();
					rightLevel = 1;
				}
				
			}
		}
		
		widths[LEFT] = widths[LEFT] + leftLevel * MARGIN;
		widths[RIGHT] = widths[RIGHT] + rightLevel * MARGIN;
	}

	/**
	 * Set required place for nodes in BoundsData.
	 * 
	 * @param data
	 * @param childNodes
	 * @param current_width
	 * @param level
	 * @param id
	 * @return height of child nodes (cn)
	 */
	protected int setBounds(BoundsData<T> data, List<Node> childNodes, int current_width, int level, int id) {
		int bounds = 0;
		while (data.getBounds().size() <= level) {
			data.getBounds().add(new ArrayList<Integer>());
		}
		for (int i = 0; i < childNodes.size(); ++i) {
			Node n = childNodes.get(i);
			T nw = data.getNodes().get(id);

			List<Node> fcn = n.getChildNodes();
			if (fcn != null && !fcn.isEmpty()) {
				// recursively get height
				int h = 0;
				if (!nw.isCollapsed()) {
					h = setBounds(data, fcn, current_width + nw.getOffsetWidth(), level + 1, id + 1);
				}
				h = (nw.getOffsetHeight() > h) ? nw.getOffsetHeight() : h;
				bounds += h;
				id += n.getNumberOfChildNodes() + 1;
				data.getBounds().get(level).add(h); // add max height of the node
												// and its child nodes
			} else { // leaf
				bounds += nw.getOffsetHeight();
				id++;
				// add height of the node which is leaf
				data.getBounds().get(level).add(nw.getOffsetHeight());
			}

			// !!! this part set maximal width
			if (data.getMaxWidth() < current_width + nw.getOffsetWidth()) {
				data.setMaxWidth(current_width + nw.getOffsetWidth());
				data.setMaxWidthLevel(level);
			}
		}
		return bounds;
	}
	
	protected void setChildNodesByLocationAndHeights(Node root, List<Node> left, List<Node> right, List<List<Integer>> leftBounds, List<List<Integer>> rightBounds, Integer[] heights) {
		List<Node> rootcn = root.getChildNodes();
		// split child nodes to left and right
		for (int i = 0; rootcn != null && i < rootcn.size(); ++i) {
			Node n = rootcn.get(i);
			if (n.getLocation() == NodeLocation.LEFT) {
				heights[LEFT] = heights[LEFT] + leftBounds.get(0).get(left.size());
				left.add(n);
			} else {
				heights[RIGHT] = heights[RIGHT] + rightBounds.get(0).get(right.size());
				right.add(n);
			}
		}
	}
	
	protected void initContext(Context2d context, int width, int height) {
		context.beginPath(); // of lines

		// support content
		context.setFillStyle("white");
		context.fillRect(0, 0, width, height);

		context.setLineWidth(2.0);
		context.setStrokeStyle(CssColor.make("#08C"));
	}

	/**
	 * Generate part of mind map.
	 * 
	 * @param panel
	 * @param canvas
	 * @param cn
	 *            child nodes
	 * @param level_bounds
	 * @param loc
	 *            node location
	 * @param startX
	 *            the point where arrow begins
	 * @param startY
	 *            the point where arrow begins
	 * @param left
	 * @param top
	 * @param level
	 *            depth
	 * @param startChildNodeY
	 *            the point where child nodes begins (Y)
	 */
    protected int generate(GenerateData<T> data, List<Node> childNodes, int startX, int startY, int left, int top, int level,
			int startChildNodeY, int id) {

		int py = startChildNodeY;
		int lvl = 0;

		int margin = (data.getLocation() == NodeLocation.LEFT) ? -MARGIN : MARGIN;
		int curveness = (data.getLocation() == NodeLocation.LEFT) ? -CURVENESS : CURVENESS;

		for (int i = 0; i < childNodes.size(); ++i) {
			Node n = childNodes.get(i);
			T nw = data.getNodes().get(id);

			AbsolutePanel panel = (AbsolutePanel) nw.getParent();

			int x = (data.getLocation() == NodeLocation.LEFT) ? startX - nw.getOffsetWidth()
					: startX;
			x += margin;
			lvl = ((data.getLevelBounds().size() > level) ? data.getLevelBounds().get(level).get(
					data.getColumns().get(level)) / 2 : 0);
			int y = lvl + py;

			if (data.isMakePicture()) {
				nw.draw(data.getContext(), x, y + nw.getOffsetHeight() / 2 - 5);
			} else {
				panel.setWidgetPosition(nw, left + x,
						top + y - nw.getOffsetHeight() / 2);
				DOM.setStyleAttribute(nw.getElement(), "visibility", "visible");
			}
			data.getColumns().set(level, data.getColumns().get(level) + 1);
			id++;
			// underline node
			drawLine(data.getContext(), x, y + nw.getOffsetHeight() / 2,
					x + nw.getOffsetWidth(), y + nw.getOffsetHeight() / 2);
			// draw arrow
			drawCurve(data.getContext(), startX, startY, x
					+ ((data.getLocation() == NodeLocation.LEFT) ? nw.getOffsetWidth() : 0), y
					+ nw.getOffsetHeight() / 2, curveness);

			if (!nw.isCollapsed() && n.getChildNodes() != null
					&& n.getChildNodes().size() > 0) {
				// generate child nodes
				x = (data.getLocation() == NodeLocation.RIGHT) ? startX + nw.getOffsetWidth() + margin
						: x; // new startX
				id = generate(data, n.getChildNodes(), x, 
								y + nw.getOffsetHeight() / 2, left, top, level + 1, py, id);
			} else if (nw.isCollapsed()) {
				hideChildNodes(nw, data.getNodes());
				id += n.getNumberOfChildNodes();
			}
			py += lvl * 2; // for next row, increase py
		}

		return id;

	}

	protected void hideChildNodes(T root, List<T> nodes) {
		int id = nodes.indexOf(root);
		int nocn = root.getNode().getNumberOfChildNodes();
		for (int i = 0; i < nocn; ++i) {
			DOM.setStyleAttribute(nodes.get(id + i + 1).getElement(),
					"visibility", "hidden");
		}
	}

	protected void drawEllipse(Context2d ctx, int x, int y, int w, int h) {
		double kappa = .5522848;
		double ox = (w / 2) * kappa, // control point offset horizontal
		oy = (h / 2) * kappa, // control point offset vertical
		xe = x + w, // x-end
		ye = y + h, // y-end
		xm = x + w / 2, // x-middle
		ym = y + h / 2; // y-middle

		ctx.setLineWidth(2.0);
		ctx.beginPath();
		ctx.moveTo(x, ym);
		ctx.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y);
		ctx.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym);
		ctx.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye);
		ctx.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym);
		ctx.closePath();
		ctx.setLineWidth(2.0);
		ctx.setStrokeStyle(CssColor.make("#08C"));
		ctx.fill();
		ctx.stroke();
	}

	protected void drawCurve(Context2d context, int x1, int y1, int x2, int y2,
			int curveness) {
		context.moveTo(x1, y1);
		context.bezierCurveTo(x1 + curveness, y1, x2 - curveness, y2, x2, y2);
	}

	protected void drawLine(Context2d context, int x1, int y1, int x2, int y2) {
		context.moveTo(x1, y1);
		context.lineTo(x2, y2);
	}

    /**
     * Get location for root child node.
     * Default location is RIGHT.
     * 
     * @param root
     * @return
     */
    public NodeLocation getRootChildNodeLocation(Node root) {
    	return NodeLocation.RIGHT;
    }

    protected void prepare(List<T> nodes) {

    	for (int i=0; i<nodes.size(); ++i) {
    		T node = nodes.get(i);

            // fix for resize minimal and maximal nodes
            if (node.getOffsetWidth() == Settings.NODE_MIN_WIDTH || 
            	node.getOffsetWidth() == Settings.NODE_MAX_WIDTH + 4 || node.getOffsetWidth() == Settings.NODE_MAX_WIDTH) {
            	node.setWidth("auto");
            }
            if (node.getOffsetHeight() == Settings.NODE_MIN_HEIGHT) {
                node.setHeight("auto");
            }
    	}
   
    }
    
    protected boolean resize(List<T> nodes) {
    	boolean succ = true;
    	for(int i=0; i<nodes.size(); ++i){
    		T node = nodes.get(i);//left node3 fds fsd f dsf sd fsd fs df sdfs f sd fsd fsd f sd fsd fs fs d fsd fds fs df

	        if (node.getWidgetWidth() < Settings.NODE_MIN_WIDTH) {
	            node.setWidth(Settings.NODE_MIN_WIDTH+"px");
	        } else if (node.getWidgetWidth() > Settings.NODE_MAX_WIDTH + 4) {
	            succ = false; // to set correct width in next cycle
	            node.setWidth(Settings.NODE_MAX_WIDTH+"px");// fix change from max size to smaller
	        }
	
	        if (node.getWidgetHeight() < Settings.NODE_MIN_HEIGHT) {
	        	node.setHeight(Settings.NODE_MIN_HEIGHT+"px");
	        }
    	}
    	return succ;
    }

}
