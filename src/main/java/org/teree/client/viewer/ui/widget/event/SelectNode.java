package org.teree.client.viewer.ui.widget.event;


import java.util.ArrayList;
import java.util.List;

import org.teree.shared.data.Node;

public class SelectNode {
    
    private static List<SelectNode.Listener> listeners;
	
	private SelectNode() {
	    
	}
    
    public static void select(Node n) {
        for(int i=0; listeners != null && i<listeners.size(); ++i){
            listeners.get(i).select(n);
        }
    }
    
    public static void unselect() {
        for(int i=0; listeners != null && i<listeners.size(); ++i){
            listeners.get(i).unselect();
        }
    }
	
	public static void addListener(SelectNode.Listener snl) {
	    if(listeners == null){
	        listeners = new ArrayList<SelectNode.Listener>();
	    }
	    listeners.add(snl);
	}
	
	public interface Listener {
	    
        public void select(Node n);
        
        public void unselect();
        
	}
	
}
