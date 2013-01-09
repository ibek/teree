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
	private static final int ELLIPSE_OVERFLOW_WIDTH = 44;
	
	private static final int LEFT = 0;
	private static final int RIGHT = 1;

	@Override
	protected int[] render(Canvas canvas, List<T> nodes, Node root,
			boolean makePicture, boolean editable) {
		
		List<List<Integer>>leftBounds = new ArrayList<List<Integer>>();
		leftBounds.add(new ArrayList<Integer>());
		List<List<Integer>> rightBounds = new ArrayList<List<Integer>>();
		rightBounds.add(new ArrayList<Integer>());
		Integer[] widths = new Integer[]{0,0};
		setBoundsAndWidths(nodes, root, leftBounds, rightBounds, widths);

		List<Node> lcn = new ArrayList<Node>(); // left child nodes
		List<Node> rcn = new ArrayList<Node>(); // right child nodes
		Integer[] heights = new Integer[]{0,0};
		setChildNodesByLocationAndHeights(root, lcn, rcn, leftBounds, rightBounds, heights);
		
		int leftw = widths[LEFT]; // left width
		int rightw = widths[RIGHT]; // right width
		int lefth = heights[LEFT]; // left height
		int righth = heights[RIGHT]; // right height

		final T rw = nodes.get(0); // root widget
		int width = getWidth(rw, leftw, rightw);
		int height = getHeight(rw, lefth, righth);

		if (!makePicture) {
			canvas.setCoordinateSpaceWidth(width);
			canvas.setCoordinateSpaceHeight(height);
		}
		Context2d context = canvas.getContext2d();
		initContext(context, width, height);

		int rootX = 0, rootY = 0;
		int left = 0, top = 0; // position of centered canvas

		if (!makePicture) {
			AbsolutePanel panel = (AbsolutePanel) rw.getParent();
			/**
			 * the NODE_MAX_WIDTH fixes bug that last node had minimal
			 * width because of panel.setWidth (there wasn't any place 
			 * for new node)
			 */
			int panelWidth = width + Settings.NODE_MAX_WIDTH; 
			if (panelWidth >= Window.getClientWidth()) {
				panel.setWidth(panelWidth + "px"); // to enable scrolling with
											// horizontal scrollbar
			} else {
				panel.setWidth("auto");
			}

			left = canvas.getAbsoluteLeft()
					- canvas.getParent().getAbsoluteLeft(); // getRelativeLeft
			top = canvas.getAbsoluteTop()
					- canvas.getParent().getAbsoluteTop(); // getRelativeTop

			rootX = left + leftw;
			rootY = top + height / 2 - rw.getOffsetHeight() / 2;
			panel.setWidgetPosition(rw, rootX, rootY); // set root node into middle of
													// scene
			DOM.setStyleAttribute(rw.getElement(), "visibility", "visible");

		}

		// start position for child nodes where root height is biggest
		// and for child nodes where height is smaller
		int startLeftChildNodeY = (height - lefth) / 2;
		int startRightChildNodeY = (height - righth) / 2;

		// to get right identifier for left and right heights in specific level
		List<Integer> columns = new ArrayList<Integer>();
		for (int i = 0; i < leftBounds.size(); ++i) {
			columns.add(0);
		}

		int id = 1;
		GenerateData<T> data = new GenerateData<T>(nodes, leftBounds, columns, NodeLocation.LEFT, context, makePicture);
		id = generate(data, lcn, leftw, height / 2, left, top, 0, startLeftChildNodeY, id);
		
		columns.clear();
		for (int i = 0; i < rightBounds.size(); ++i) {
			columns.add(0);
		}

		data = new GenerateData<T>(nodes, rightBounds, columns, NodeLocation.RIGHT, context, makePicture);
		generate(data, rcn, leftw + rw.getOffsetWidth(), height / 2, left, top, 0, startRightChildNodeY, id);

		context.stroke(); // draw the arrows

		int ellipseHeight = rw.getOffsetHeight()*2;
		drawEllipse(context, leftw - ELLIPSE_OVERFLOW_WIDTH/2, height / 2 - ellipseHeight / 2,
				rw.getOffsetWidth() + ELLIPSE_OVERFLOW_WIDTH, ellipseHeight); // ellipse for the root node
		
		if (makePicture) {

			rootX = leftw;
			rootY = height / 2 + rw.getOffsetHeight() / 2 - 5;
			rw.draw(context, rootX, rootY);

		}

		return new int[] { rootX, rootY, width, height };

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
	 * Sets left and right bounds and width of left and right part of tree
	 * @param nodes
	 * @param root
	 * @param leftBounds leftBounds.get(col).get(row)
	 * @param rightBounds
	 * @param widths [0]-leftw, [1]-rightw
	 */
	private void setBoundsAndWidths(List<T> nodes, Node root, List<List<Integer>> leftBounds, 
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
	private int setBounds(BoundsData<T> data, List<Node> childNodes, int current_width, int level, int id) {
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
	
	private void setChildNodesByLocationAndHeights(Node root, List<Node> left, List<Node> right, List<List<Integer>> leftBounds, List<List<Integer>> rightBounds, Integer[] heights) {
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
	
	private void initContext(Context2d context, int width, int height) {
		context.beginPath(); // of lines

		// support content
		context.setFillStyle("white");
		context.fillRect(0, 0, width, height);

		context.setLineWidth(2.0);
		context.setStrokeStyle(CssColor.make(0, 0, 0));
	}
	
	private int getWidth(T rw, int leftw, int rightw) {
		return leftw + rw.getOffsetWidth() + rightw;
	}
	
	private int getHeight(T rw, int lefth, int righth) {
		int max_y = (lefth > righth) ? lefth : righth;
		max_y += rw.getOffsetHeight() / 2;

		// for case root is biggest
		max_y = (max_y / 2 - rw.getOffsetHeight() > 0) ? max_y : rw
				.getOffsetHeight() * 2;
		return max_y;
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
	private int generate(GenerateData<T> data, List<Node> childNodes, int startX, int startY, int left, int top, int level,
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
				x = (data.getLocation() == NodeLocation.RIGHT) ? startX + nw.getOffsetWidth()
						: x;
				id = generate(data, n.getChildNodes(), x + ((data.getLocation() == NodeLocation.LEFT) ? 0 : margin), 
								y + nw.getOffsetHeight() / 2, left, top, level + 1, py, id);
			} else if (nw.isCollapsed()) {
				hideChildNodes(nw, data.getNodes());
				id += n.getNumberOfChildNodes();
			}
			py += lvl * 2; // for next row, increase py
		}

		return id;

	}

	private void hideChildNodes(T root, List<T> nodes) {
		int id = nodes.indexOf(root);
		int nocn = root.getNode().getNumberOfChildNodes();
		for (int i = 0; i < nocn; ++i) {
			DOM.setStyleAttribute(nodes.get(id + i + 1).getElement(),
					"visibility", "hidden");
		}
	}

	private void drawEllipse(Context2d ctx, int x, int y, int w, int h) {
		double kappa = .5522848;
		double ox = (w / 2) * kappa, // control point offset horizontal
		oy = (h / 2) * kappa, // control point offset vertical
		xe = x + w, // x-end
		ye = y + h, // y-end
		xm = x + w / 2, // x-middle
		ym = y + h / 2; // y-middle

		ctx.setLineWidth(2.0);
		ctx.setStrokeStyle(CssColor.make(0, 0, 0));
		ctx.beginPath();
		ctx.moveTo(x, ym);
		ctx.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y);
		ctx.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym);
		ctx.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye);
		ctx.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym);
		ctx.closePath();
		ctx.setLineWidth(2.0);
		ctx.setStrokeStyle(CssColor.make(0, 0, 0));
		ctx.fill();
		ctx.stroke();
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
