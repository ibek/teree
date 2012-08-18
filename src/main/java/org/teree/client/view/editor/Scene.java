package org.teree.client.view.editor;

import javax.inject.Inject;

import org.teree.client.event.NodeChanged;
import org.teree.client.event.NodeChangedHandler;
import org.teree.client.event.SelectNode;
import org.teree.client.event.SelectNodeHandler;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class Scene {
    
    @Inject
    private HandlerManager eventBus;
    
    private Canvas canvas;
    
    private NodeWidget selected;
    
    private AbsolutePanel container;
    
    public Scene() {
        
        bind();
        
        container = new AbsolutePanel();
        canvas = Canvas.createIfSupported();
        if (canvas == null) { // canvas is not supported
            // deal with it
        }
        
        container.add(canvas);
    }
    
    public void bind() {
        
        eventBus.addHandler(SelectNode.TYPE, new SelectNodeHandler() {
            @Override
            public void select(SelectNode event, NodeWidget node) {
                selectNode(node);
            }
            
        });
        
        canvas.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectNode(null);
            }
        });
        
        eventBus.addHandler(NodeChanged.TYPE, new NodeChangedHandler() {
            @Override
            public void changed(NodeChanged event, NodeWidget node) {
                // probably regenerate
            }
        });
        
    }

    private void selectNode(NodeWidget node) {
        if (selected != null) { // only one node can be selected
            selected = selected.unselect();
        }
        
        if (node != null) {
            selected = node.select();
        } else {
            selected = null;
        }
    }
    
}
