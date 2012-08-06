package org.teree.client.view.widget;

import org.teree.shared.data.Node;

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
        
        @Source("resource/nodeStyle.css")
        NodeStyle nodeButtons();
        
        public interface NodeStyle extends CssResource {
            String button();
        }
        
    }
    
    private Resources resources = GWT.create(Resources.class);
    
    private Node node;
    
    protected NodeWidget(Node node) {
        this.node = node;
    }
    
    public static NodeWidget create(Node node) {
        
        switch(node.getContent().getType()){
            case String: {
                return new TextNodeWidget(node);
            }
        }
        
        return null;
    }

}
