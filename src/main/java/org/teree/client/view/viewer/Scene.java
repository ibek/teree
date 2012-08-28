package org.teree.client.view.viewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.map.MapType;
import org.teree.client.map.MindMap;
import org.teree.client.map.Renderer;
import org.teree.client.view.viewer.NodeWidget;
import org.teree.shared.data.Node;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Scene extends Composite {
	
	private Renderer<NodeWidget> map;

    private AbsolutePanel container;
    private Canvas canvas;
    
    private HandlerManager viewBus = new HandlerManager(null);
    
    public Scene() {
    	
        setMapType(Settings.DEFAULT_MAP_TYPE);
        
        bind();
        
        container = new AbsolutePanel();
        canvas = Canvas.createIfSupported();
        if (canvas == null) { // canvas is not supported
            // deal with it
        }
        
        container.add(canvas);
        initWidget(container);
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
    	container.clear();
        container.add(canvas);
        
        init(root);
        
        map.renderViewer(canvas, getNodeWidgets(), root);
    }
    
    private void init(Node node) {
    	
		NodeWidget nw = null;
		switch(node.getType()){
	        case String: {
	            nw = new TextNodeWidget(node);
	            break;
	        }
	        case ImageLink: {
	        	nw = new ImageNodeWidget(node);
	        	break;
	        }
	    }
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
    	while (it.hasNext()) {
    		Widget w = it.next();
    		if(w instanceof NodeWidget){
    			nodes.add((NodeWidget)w); // there is the casting from Widget to NodeWidget
    		}
    	}
    	
    	return nodes;
    }
    
}
