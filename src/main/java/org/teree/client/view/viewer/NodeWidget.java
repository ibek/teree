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
	
	private static boolean initMathScript = true;
	public static NodeWidget createNodeWidget(Node node) {
		NodeWidget nw = null;
		switch(node.getType()){
	        case IconText: {
	            nw = new TextNodeWidget(node);
	            break;
	        }
	        case ImageLink: {
	        	nw = new ImageNodeWidget(node);
	        	break;
	        }
	        case Link: {
	        	nw = new LinkNodeWidget(node);
	        	break;
	        }
	        case MathExpression: {
	        	if (initMathScript) {
	        		initMathScript = false;
	        		MathExpressionTools.initScript();
	        	}
	        	//requiresRender = true; // TODO: needs to be updated!!
	        	nw = new MathExpressionNodeWidget(node);
	        	break;
	        }
	        case Connector: {
	        	nw = new ConnectorNodeWidget(node);
	        	break;
	        }
	    }
		return nw;
	}

}
