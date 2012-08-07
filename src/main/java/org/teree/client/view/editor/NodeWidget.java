package org.teree.client.view.editor;

import org.teree.shared.data.Node;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public class NodeWidget extends Composite {

    public interface Resources extends ClientBundle {
        
        @Source("resource/add.png")
        ImageResource addIcon();
        
        @Source("resource/up.png")
        ImageResource upIcon();
        
        @Source("resource/down.png")
        ImageResource downIcon();
        
        @Source("../resource/nodeStyle.css")
        NodeStyle nodeStyle();
        
        public interface NodeStyle extends CssResource {
            String button();
            String selected();
            String view();
            String edit();
        }
        
    }
    
    protected Resources resources = GWT.create(Resources.class);
    
    protected Node node;
    
    protected AbsolutePanel container;
    
    protected boolean selected;
    
    protected NodeWidget(Node node) {
        this.node = node;
        selected = false;
        container = new AbsolutePanel();
        initWidget(container);
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
    
    public NodeWidget select() {
        selected = true;
        container.setStyleName(resources.nodeStyle().selected());
        return this;
    }
    
    public NodeWidget unselect() {
        selected = false;
        container.removeStyleName(resources.nodeStyle().selected());
        return null;
    }

}
