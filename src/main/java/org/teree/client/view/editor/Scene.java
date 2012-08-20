package org.teree.client.view.editor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.teree.client.Settings;
import org.teree.client.event.NodeChanged;
import org.teree.client.event.NodeChangedHandler;
import org.teree.client.event.SelectNode;
import org.teree.client.event.SelectNodeHandler;
import org.teree.client.map.MapType;
import org.teree.client.map.MindMap;
import org.teree.client.map.Renderer;
import org.teree.shared.data.Node;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class Scene {
    
    private Node root;
    private List<NodeWidget> nodes;
    private Renderer<NodeWidget> map;
    
    private AbsolutePanel container;
    private Canvas canvas;
    
    private NodeWidget selected;
    
    @Inject
    private HandlerManager eventBus;
    
    public Scene() {
        
        bind();
        setMapType(Settings.DEFAULT_MAP_TYPE);
        
        container = new AbsolutePanel();
        canvas = Canvas.createIfSupported();
        if (canvas == null) { // canvas is not supported
            // deal with it
        }
        
        nodes = new ArrayList<NodeWidget>();
        
        container.add(canvas);
    }
    
    public void update(Node node) {
    	
    	// TODO: add and insert nodewidgets
    	
    	map.renderEditor(canvas, nodes, root);
    	
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
    
    public void setMapType(MapType type) {
    	switch(type) {
	    	case MindMap: {
	    		map = new MindMap<NodeWidget>();
	    	}
    	}
    }
    
    public void setRoot(Node root) {
    	this.root = root;
        update(null);
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
