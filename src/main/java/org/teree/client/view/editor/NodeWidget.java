package org.teree.client.view.editor;

import org.teree.client.view.NodeInterface;
import org.teree.shared.data.Node;

import com.google.gwt.core.client.GWT;
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

	@Override
	public abstract void update();
	
	public abstract void edit();
    
    public NodeWidget select() {
        selected = true;
        return this;
    }
    
    public NodeWidget unselect() {
        selected = false;
        return null;
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
