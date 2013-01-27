package org.teree.client.view.viewer;

import org.teree.client.view.NodeInterface;
import org.teree.client.view.resource.MathExpressionTools;
import org.teree.client.view.resource.NodeCssStyle;
import org.teree.shared.data.common.Node;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
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
	
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
	
	@Override
	public boolean isCollapsed() {
		return collapsed;
	}

}
