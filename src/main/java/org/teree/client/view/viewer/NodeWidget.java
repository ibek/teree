package org.teree.client.view.viewer;

import org.teree.client.view.NodeInterface;
import org.teree.client.view.resource.NodeCssStyle;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeStyle;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public abstract class NodeWidget extends Composite implements NodeInterface {
    
    protected NodeCssStyle resources = NodeCssStyle.INSTANCE;
    
    protected Node node;
    
    protected AbsolutePanel container;
    
    protected boolean collapsed = false;
    
    public NodeWidget() {
    	
    }
    
    public NodeWidget(Node node) {
        this.node = node;
        container = new AbsolutePanel();
        resources.css().ensureInjected();
        
        initWidget(container);
    }
    
    @Override
	public void update() {
    	
    }

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public int getWidgetWidth() {
		return getWidget().getOffsetWidth();
	}

	@Override
	public int getWidgetHeight() {
		return getWidget().getOffsetHeight();
	}
	
	@Override
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
	
	@Override
	public boolean isCollapsed() {
		return collapsed;
	}

	@Override
	public void edit() {
		// not needed
	}

	@Override
	public void select() {
		// TODO Auto-generated method stub
	}

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
	}
	
	public void changeViewpoint(int index) {
		
	}

}
