package org.teree.client.view.viewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.teree.client.map.MapType;
import org.teree.client.map.MindMap;
import org.teree.client.map.Renderer;
import org.teree.shared.data.Node;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class Scene {
	
	private static final int NODE_WIDGET_MARK = 1; // from this mark are node widgets in container
    
	private Node root;
	private Renderer<NodeWidget> map;

    private AbsolutePanel container;
    private Canvas canvas;
    
    @Inject
    private HandlerManager eventBus;
    
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
        
        init(root);
        
        map.renderViewer(canvas, getNodeWidgets(), root);
    }
    
    private void init(Node node) {
    	
		NodeWidget nw = NodeWidget.create(node);
		container.add(nw,0,0);
		
    	List<Node> cn = node.getChildNodes();
    	for(int i=0; cn!=null && i<cn.size(); ++i){
    		Node n = cn.get(i);
    		init(n);
    	}
    }
    
    private List<NodeWidget> getNodeWidgets() {
    	Iterator<Widget> it = container.iterator();
    	List<NodeWidget> nodes = new ArrayList<NodeWidget>();
    	for (int i=0; it.hasNext(); ++i) {
    		if(i >= NODE_WIDGET_MARK){
    			nodes.add((NodeWidget)it.next());
    		}
    	}
    	
    	return nodes;
    }
    
}
