package org.teree.client.view.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.teree.client.Settings;
import org.teree.client.Teree;
import org.teree.client.event.NodeChanged;
import org.teree.client.event.NodeChangedHandler;
import org.teree.client.event.SelectNode;
import org.teree.client.event.SelectNodeHandler;
import org.teree.client.map.MapType;
import org.teree.client.map.MindMap;
import org.teree.client.map.Renderer;
import org.teree.client.view.editor.NodeWidget;
import org.teree.shared.data.Node;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Scene extends Composite {
	
	private static final int NODE_WIDGET_MARK = 1; // from this mark are node widgets in container
    
    private Node root;
    private Renderer<NodeWidget> map;
    
    private AbsolutePanel container;
    private Canvas canvas;
    
    private NodeWidget selected;
    
    private HandlerManager eventBus;
    
    public Scene() {
        
        setMapType(Settings.DEFAULT_MAP_TYPE);
        
        container = new AbsolutePanel();
        canvas = Canvas.createIfSupported();
        if (canvas == null) { // canvas is not supported
            // deal with it
        }
        
        bind();
        
        initWidget(container);
        
    }
    
    public void bind() {
    	
    	eventBus = Teree.getHandlerManager(); // TODO: fix this, inject doesn't work!!
        
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
    	container.clear();
        container.add(canvas);
        
        NodeWidget nw = NodeWidget.create(root);
        container.add(nw);
        
        update(root); // initialize
    }
    
    public void update(Node changed) {
    	update(root, changed, 0);
    	map.renderEditor(canvas, getNodeWidgets(), root);
    }
    
    private List<NodeWidget> getNodeWidgets() {
    	Iterator<Widget> it = container.iterator();
    	List<NodeWidget> nodes = new ArrayList<NodeWidget>();
    	for (int i=0; it.hasNext(); ++i) {
    		if(i >= NODE_WIDGET_MARK){
    			nodes.add((NodeWidget)it.next()); // there is the casting from Widget to NodeWidget
    		}
    	}
    	
    	return nodes;
    }
    
    private int update(Node current, Node changed, int id) {
    	
    	List<Node> cn = current.getChildNodes();
    	for(int i=0; cn!=null && i<cn.size(); ++i){
    		Node n = cn.get(i);
    		if (n == changed || id < container.getWidgetCount() - NODE_WIDGET_MARK) {
    			id = insertNode(n, id);
    		}
    	}
    	
    	return id;
    }
    
    private int insertNode(Node node, int id) {
    	NodeWidget nw = NodeWidget.create(node);
    	if (id < container.getWidgetCount() - NODE_WIDGET_MARK) {
    		container.add(nw, 0, 0);
    	} else {
    		container.insert(nw, 0, 0, id + NODE_WIDGET_MARK);
    	}
    	id++;
    	
    	List<Node> cn = node.getChildNodes();
        for(int i=0; cn != null && i<cn.size(); ++i){
        	id = insertNode(cn.get(i), id);
        }
    	
    	return id;
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
