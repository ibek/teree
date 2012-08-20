package org.teree.client.view.viewer;

import org.teree.client.view.NodeType;
import org.teree.shared.data.Node;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public class NodeWidget extends Composite implements NodeType {

    public interface Resources extends ClientBundle {
        
        @Source("../resource/nodeStyle.css")
        NodeStyle nodeStyle();
        
        public interface NodeStyle extends CssResource {
            String view();
        }
        
    }
    
    protected Resources resources = GWT.create(Resources.class);
    
    protected Node node;
    
    protected AbsolutePanel container;
    
    protected NodeWidget(Node node) {
        this.node = node;
        container = new AbsolutePanel();
        initWidget(container);
        
    	DOM.setStyleAttribute(getElement(), "visibility", "hidden");
    }
    
    /**
     * Create NodeWidget according to type of node.
     * @param node
     * @return
     */
    public static NodeWidget create(Node node) {
        
        switch(node.getType()){
            case String: {
                return new TextNodeWidget(node);
            }
        }
        
        return null;
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
