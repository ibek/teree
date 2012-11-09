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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class MindMap<T extends Widget & NodeInterface> extends Renderer<T> {

	private static final int MARGIN = 25;
	private static final int CURVENESS = 20;

	private int max_width;
	
	/**
	 * level of max_width for the case:
	 * parent1|child2
	 *        |child3
	 * the longest parent 2
	 */
	private int level;
	
	private int left = 0;
	private int top = 0;
	
	private boolean makePicture;
	
	private Context2d context;

	@Override
	protected void render(Canvas canvas, List<T> nodes, Node root, boolean makePicture, boolean editable) {
	    
	    this.makePicture = makePicture;
		
		/**
		 * left.get(col).get(row)
		 */
		List<List<Integer>> left = new ArrayList<List<Integer>>(); // left bounds
		left.add(new ArrayList<Integer>());
		List<List<Integer>> right = new ArrayList<List<Integer>>(); // right bounds
		right.add(new ArrayList<Integer>());

		int maxlw = 0; // max left width
		int maxrw = 0; // max right width
		List<Node> rootcn = root.getChildNodes();
		
		int id = 1; // identifier for node widgets
		int left_level = 0;
		int right_level = 0;

		// set bounds for left and right nodes
		for (int i = 0; rootcn != null && i < rootcn.size(); ++i) {
			Node n = rootcn.get(i);
			T nc = nodes.get(id);

			if (n.getLocation() == NodeLocation.LEFT
					&& n.getChildNodes() != null) {
				max_width = 0; // for this node n, it is set in setBounds method!!
				level = 1;
				int h = setBounds(nodes, n.getChildNodes(), left, nc.getOffsetWidth(), level, id+1); // don't worry, the current node is set in setBounds
				if (nc.getOffsetHeight() > h) {
					h = nc.getOffsetHeight();
				}
				left.get(0).add(h);
				id += n.getNumberOfChildNodes() + 1;
				if (max_width > maxlw) {
					maxlw = max_width;
					left_level = level+1;
				}
			} else if (n.getChildNodes() != null) {
				max_width = 0;
				level = 1;
				int h = setBounds(nodes, n.getChildNodes(), right, nc.getOffsetWidth(), level, id+1); // don't worry, the current node is set in setBounds
				if (nc.getOffsetHeight() > h) {
					h = nc.getOffsetHeight();
				}
				right.get(0).add(h);
				id += n.getNumberOfChildNodes() + 1;
				if (max_width > maxrw) {
					maxrw = max_width;
					right_level = level+1;
				}
			} else if (n.getLocation() == NodeLocation.LEFT) {
				left.get(0).add(nc.getOffsetHeight());
				id++;
				if (maxlw < nc.getOffsetWidth()) {
					maxlw = nc.getOffsetWidth();
					left_level = 1;
				}
			} else {
				right.get(0).add(nc.getOffsetHeight());
				id++;
				if (maxrw < nc.getOffsetWidth()) {
					maxrw = nc.getOffsetWidth();
					right_level = 1;
				}
			}
		}

		List<Node> lcn = new ArrayList<Node>(); // left child nodes
		List<Node> rcn = new ArrayList<Node>(); // right child nodes
		int lefth = 0; // left height
		int righth = 0; // right height

		// split child nodes to left and right
		for (int i = 0; rootcn != null && i < rootcn.size(); ++i) {
			Node n = rootcn.get(i);
			if (n.getLocation() == NodeLocation.LEFT) {
				lefth += left.get(0).get(lcn.size());
				lcn.add(n);
			} else {
				righth += right.get(0).get(rcn.size());
				rcn.add(n);
			}
		}
		
		final T rw = nodes.get(0); // root widget

		maxlw += left_level * MARGIN;
		maxrw += right_level * MARGIN;
		int max_x = maxlw + rw.getOffsetWidth() + maxrw;
		int max_y = (lefth > righth) ? lefth : righth;
		max_y += rw.getOffsetHeight() / 2;
		
		// for case root is biggest
		max_y = (max_y / 2 - rw.getOffsetHeight() > 0) ? max_y : rw.getOffsetHeight() * 2;
		
		if (!makePicture) {
			canvas.setCoordinateSpaceWidth(max_x);
			canvas.setCoordinateSpaceHeight(max_y);
		}
		Context2d context = canvas.getContext2d();
		this.context = context;

		id = 0;

		context.beginPath(); // of lines

		// support content
		context.setFillStyle("white");
		context.fillRect(0, 0, max_x, max_y);

		context.setLineWidth(2.0);
		context.setStrokeStyle(CssColor.make(0,0,0));
		
		if (makePicture) {

		    rw.draw(context, maxlw, max_y / 2 - 5);
		    
		} else {
			AbsolutePanel panel = (AbsolutePanel) rw.getParent();
    		int pw = maxlw+maxrw+rw.getOffsetWidth() + Settings.NODE_MAX_WIDTH; // the NODE_MAX_WIDTH fixes bug that last node had minimal width because of panel.setWidth (there wasn't any place for new node)
    		if (pw >= Window.getClientWidth()) {
    			panel.setWidth(pw+"px"); // to enable scrolling with horizontal scrollbar
    		} else {
    			panel.setWidth("auto");
    		}
			
			this.left = canvas.getAbsoluteLeft() - canvas.getParent().getAbsoluteLeft(); // getRelativeLeft
			this.top = canvas.getAbsoluteTop() - canvas.getParent().getAbsoluteTop(); // getRelativeTop
			
    		panel.setWidgetPosition(rw, this.left + maxlw, this.top + max_y / 2 - rw.getOffsetHeight()); // set root node into middle of scene
    		DOM.setStyleAttribute(rw.getElement(), "visibility", "visible");
    		
		}
		id++;

		// underline root
		drawLine(context, maxlw, max_y / 2, maxlw
		+ rw.getOffsetWidth(), max_y / 2);
		/**drawCover(context, maxlw, max_y / 2, maxlw
				+ rw.getOffsetWidth(), max_y / 2, rw.getOffsetHeight());*/
		
		

		int lh = 0, rh = 0;

		// start position for child nodes where root height is biggest
		// and for child nodes where height is smaller
		lh += (max_y - lefth) / 2;
		rh += (max_y - righth) / 2;
		
		// to get right identifier for left and right heights in specific level
		List<Integer> status = new ArrayList<Integer>(); 
		for (int i = 0; i < left.size(); ++i) {
			status.add(0);
		}

		id = generate(nodes, context, lcn, left, status, NodeLocation.LEFT,
				maxlw, max_y / 2, 0, lh, id);
		status.clear();
		for (int i = 0; i < right.size(); ++i) {
			status.add(0);
		}
		generate(nodes, context, rcn, right, status, NodeLocation.RIGHT, maxlw
				+ rw.getOffsetWidth(), max_y / 2, 0, rh, id);

		context.stroke(); // draw the lines
		
	}

	@Override
	public NodeLocation getRootChildNodeLocation(Node root) {
		int left = 0, right = 0;
		List<Node> cn = root.getChildNodes();
		if (cn == null || cn.isEmpty()) {
			return NodeLocation.RIGHT;
		}
		for (int i = 0; i < cn.size(); ++i) {
			Node n = cn.get(i);
			if (n.getLocation() == NodeLocation.LEFT) {
				left++;
			} else if (n.getLocation() == NodeLocation.RIGHT) {
				right++;
			}
		}
		return (left < right) ? NodeLocation.LEFT : NodeLocation.RIGHT;
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
	 * @param start_x
	 *            the point where arrow begins
	 * @param start_y
	 *            the point where arrow begins
	 * @param level
	 *            depth
	 * @param start_cn
	 *            the point where child nodes begins (Y)
	 */
	private int generate(List<T> nodes, Context2d context, List<Node> cn,
			List<List<Integer>> level_bounds, List<Integer> status,
			NodeLocation loc, int start_x, int start_y, int level,
			int start_cn, int id) {

		int py = start_cn;
		int lvl = 0;

		int margin = (loc == NodeLocation.LEFT) ? -MARGIN : MARGIN;
		int curveness = (loc == NodeLocation.LEFT) ? -CURVENESS : CURVENESS;

		for (int i = 0; i < cn.size(); ++i) {
			Node n = cn.get(i);
			T nw = nodes.get(id);
			AbsolutePanel panel = (AbsolutePanel) nw.getParent();
			
			int x = (loc == NodeLocation.LEFT) ? start_x - nw.getOffsetWidth() : start_x;
			x += margin;
			lvl = ((level_bounds.size() > level) ? level_bounds.get(level).get(status.get(level)) / 2 : 0);
			int y = lvl + py;

			if (makePicture) {
	            nw.draw(context, x, y + nw.getOffsetHeight() / 2 - 5);
	        } else {
    			panel.setWidgetPosition(nw, this.left + x, this.top + y - nw.getOffsetHeight() / 2);
    			DOM.setStyleAttribute(nw.getElement(), "visibility", "visible");
	        }
			status.set(level, status.get(level) + 1);
			id++;
			// underline node
			drawLine(context, x, y + nw.getOffsetHeight() / 2, x + nw.getOffsetWidth(), 
					 y + nw.getOffsetHeight()
					/ 2);
			// draw arrow
			drawCurve(context, start_x, start_y, 
					  x + ((loc == NodeLocation.LEFT) ? nw.getOffsetWidth() : 0), 
					  y + nw.getOffsetHeight() / 2, curveness);
			// drawLine(context, start_x, start_y, x+((loc ==NodeLocation.LEFT)?n.getContent().getWidth():0), y+n.getContent().getHeight()/2);

			if (n.getChildNodes() != null && n.getChildNodes().size() > 0) {
				// generate child nodes
				x = (loc == NodeLocation.RIGHT) ? start_x
						+ nw.getOffsetWidth() : x;
				id = generate(nodes, context, n.getChildNodes(), level_bounds, status, loc, 
							  x + ((loc == NodeLocation.LEFT) ? 0 : margin), 
							  y + nw.getOffsetHeight() / 2, 
							  level + 1, py, id);
			}
			py += lvl * 2; // for next row, increase py
		}

		return id;

	}

	/**
	 * Set required place for nodes in level_bounds.
	 * 
	 * @param cn
	 *            child nodes
	 * @param level_bounds
	 * @param current_width
	 * @param level
	 * @return height of child nodes (cn)
	 */
	private int setBounds(List<T> nodes, List<Node> cn, List<List<Integer>> level_bounds,
			int current_width, int level, int id) {
		int bounds = 0;
		for (int i = 0; i < cn.size(); ++i) {
			Node n = cn.get(i);
			T nw = nodes.get(id);

			List<Node> fcn = n.getChildNodes();
			if (fcn != null && !fcn.isEmpty()) {
				// recursively get height
				int h = setBounds(nodes, fcn, level_bounds, current_width + nw.getOffsetWidth(), level + 1, id+1);
				h = (nw.getOffsetHeight() > h) ? nw.getOffsetHeight() : h;
				bounds += h;
				id += n.getNumberOfChildNodes()+1;
				level_bounds.get(level).add(h); // add max height of the node
												// and its child nodes
			} else { // leaf
				bounds += nw.getOffsetHeight();
				id++;
				while (level_bounds.size() <= level) {
					level_bounds.add(new ArrayList<Integer>());
				}
				// add height of the node which is leaf
				level_bounds.get(level).add(nw.getOffsetHeight());
				
				// !!! this part set maximal width
				if (max_width < current_width + nw.getOffsetWidth()) {
					max_width = current_width + nw.getOffsetWidth();
					this.level = level;
				}
			}
		}
		return bounds;
	}
	
	private void drawCover(Context2d context, int x1, int y1, int x2, int y2, int height) {
		/**context.moveTo(x1 - 2, y1);
		context.lineTo(x1 + 3, y1 - height/2);
		context.lineTo(x2 - 3, y1 - height/2);
		context.lineTo(x2 + 2, y2);
		context.lineTo(x2 - 3, y1 + height/2);
		context.lineTo(x1 + 3, y1 + height/2);
		context.lineTo(x1 - 2, y1);*/
	}

	private void drawCurve(Context2d context, int x1, int y1, int x2, int y2,
			int curveness) {
		context.moveTo(x1, y1);
		context.bezierCurveTo(x1 + curveness, y1, x2 - curveness, y2, x2, y2);
	}

	private void drawLine(Context2d context, int x1, int y1, int x2, int y2) {
		context.moveTo(x1, y1);
		context.lineTo(x2, y2);
	}

}
