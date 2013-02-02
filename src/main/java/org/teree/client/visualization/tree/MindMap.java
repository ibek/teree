package org.teree.client.visualization.tree;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.NodeInterface;
import org.teree.client.visualization.utils.Shapes;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Node.NodeLocation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class MindMap<T extends Widget & NodeInterface> extends TreeRenderer<T> {

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
			if (panel == null) {
				return new int[] { rootX, rootY, width, height };
			}
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
		Shapes.drawEllipse(context, leftw - ELLIPSE_OVERFLOW_WIDTH/2, height / 2 - ellipseHeight / 2,
				rw.getOffsetWidth() + ELLIPSE_OVERFLOW_WIDTH, ellipseHeight); // ellipse for the root node
		
		if (makePicture) {

			rootX = leftw;
			rootY = height / 2 + rw.getOffsetHeight() / 2 - 5;
			rw.draw(context, rootX, rootY);

		}

		return new int[] { rootX, rootY, width, height };

	}
	
	@Override
	protected int getWidth(T rw, int leftw, int rightw) {
		return leftw + rw.getOffsetWidth() + rightw;
	}
	
	@Override
	protected int getHeight(T rw, int lefth, int righth) {
		int max_y = (lefth > righth) ? lefth : righth;
		max_y += rw.getOffsetHeight() / 2;

		// for case root is biggest
		max_y = (max_y / 2 - rw.getOffsetHeight() > 0) ? max_y : rw
				.getOffsetHeight() * 2;
		return max_y;
	}

}
