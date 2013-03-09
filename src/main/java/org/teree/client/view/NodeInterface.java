package org.teree.client.view;

import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeStyle;

import com.google.gwt.canvas.dom.client.Context2d;

public interface NodeInterface {

	public Node getNode();
	
	public int getWidgetWidth();
	
	public int getWidgetHeight();
	
	public void draw(Context2d context, int x, int y);
	
	public void update();
	
	public void setCollapsed(boolean collapsed);
	
	public boolean isCollapsed();
	
	public void edit();
	
	public void select();
	
	public void unselect();
	
}
