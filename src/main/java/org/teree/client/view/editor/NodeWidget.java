package org.teree.client.view.editor;

import org.teree.client.view.NodeInterface;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.resource.NodeCssStyle;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;

import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public abstract class NodeWidget extends Composite implements NodeInterface {
    
    protected NodeCssStyle resources = NodeCssStyle.INSTANCE;
    
    protected Node node;
    
    protected AbsolutePanel container;
    
    protected boolean selected;
    
    public NodeWidget(Node node) {
        this.node = node;
        selected = false;
        container = new AbsolutePanel();
        resources.css().ensureInjected();
        
        initWidget(container);
        
        changeStyle(node.getStyle());
        
    	DOM.setStyleAttribute(getElement(), "visibility", "hidden");
    }
	
	public abstract void edit();
	
	public abstract void changeStyle(NodeStyle style);
    
    public NodeWidget select() {
        selected = true;
        return this;
    }
    
    public NodeWidget unselect() {
        selected = false;
        return null;
    }
    
    public void dropData(DropEvent event) {
    	event.preventDefault();
        
        try {
        	Integer id = Integer.valueOf(event.getData("id"));
        	if (id != null) {
            	NodeWidget nw = (NodeWidget)((AbsolutePanel)getParent()).getWidget(id);
            	if (nw.getNode() == node) { // don't move the dragged node to the same node
            		return;
            	}
                Node child = nw.getNode().clone();
                if (node.getLocation() != null) {
                	child.setLocation(node.getLocation());
                }
                node.addChild(child);
                
                // remove the moved nodes
                int count = child.getNumberOfChildNodes();
                nw.removeFromParent();
                for (int i=0; i<count; ++i) {
                	((NodeWidget)((AbsolutePanel)getParent()).getWidget(id)).removeFromParent();
                }
                nw.getNode().remove();
                
                getParent().fireEvent(new NodeChanged(child));
            }
        } catch(NumberFormatException ex) {
        	// ignore
        }
        
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

}
