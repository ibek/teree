package org.teree.client.view;

import org.teree.shared.data.scheme.Node;

import com.google.gwt.canvas.dom.client.Context2d;

public interface NodeInterface {

	public Node getNode();
	
	public int getWidgetWidth();
	
	public int getWidgetHeight();
	
	public void draw(Context2d context, int x, int y);
	
}
