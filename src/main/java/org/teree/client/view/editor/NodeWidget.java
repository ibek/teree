package org.teree.client.view.editor;

import org.teree.client.view.NodeInterface;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.shared.data.Node;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public abstract class NodeWidget extends Composite implements NodeInterface {

	/**
	 * 
	 * TODO: study cssresources and so ... do it right and fix selected
	 *
	 */
    public interface Resources extends ClientBundle {
        
        /**@Source("resource/add.png")
        ImageResource addIcon();
        
        @Source("resource/up.png")
        ImageResource upIcon();
        
        @Source("resource/down.png")
        ImageResource downIcon();*/
        
        @Source("resource/nodeStyle.css")
        NodeStyle nodeStyle();
        
        @Source("../resource/basicNodeStyle.css")
        BasicNodeStyle basicNodeStyle();
        
        public interface NodeStyle extends CssResource {
            String edit();
        }
        
        public interface BasicNodeStyle extends CssResource {
            String view();
        }
        
    }
    
    protected Resources resources = GWT.create(Resources.class);
    
    protected Node node;
    
    protected AbsolutePanel container;
    
    protected boolean selected;
    
    public NodeWidget() {
    	this(null);
    }
    
    public NodeWidget(Node node) {
        this.node = node;
        selected = false;
        container = new AbsolutePanel();
        resources.nodeStyle().ensureInjected();
        resources.basicNodeStyle().ensureInjected();
        initWidget(container);
        
    	DOM.setStyleAttribute(getElement(), "visibility", "hidden");
    }
	
	public abstract void edit();
    
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
