package org.teree.client.visualization.tree;

import java.util.List;

import org.teree.client.view.NodeInterface;
import org.teree.shared.data.common.Node;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * A scheme displayed from top of scene.
 * 
 * @author ibek
 *
 */
public class HierarchicalVertical<T extends Widget & NodeInterface> extends TreeRenderer<T> {

	@Override
	protected int[] render(Canvas canvas, List<T> nodes, Node root,
			boolean makePicture, boolean editable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getWidth(T rw, int leftw, int rightw) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getHeight(T rw, int lefth, int righth) {
		// TODO Auto-generated method stub
		return 0;
	}

}
