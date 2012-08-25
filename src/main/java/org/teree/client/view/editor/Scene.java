package org.teree.client.view.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.map.MapType;
import org.teree.client.map.MindMap;
import org.teree.client.map.Renderer;
import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.NodeChangedHandler;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.editor.event.SelectNodeHandler;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
    private Node copied;
    
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
        
    	container.addHandler(new SelectNodeHandler() {
            @Override
            public void select(SelectNode event, NodeWidget node) {
                selectNode(node);
            }
            
        }, SelectNode.TYPE);
        
        canvas.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectNode(null);
            }
        });
        
        container.addHandler(new NodeChangedHandler() {
            @Override
            public void changed(NodeChanged event, NodeWidget node) {
                update(null);
            }
        }, NodeChanged.TYPE);
        
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
        
        NodeWidget nw = createNodeWidget(root);
        container.add(nw, 0, 0);
        
        update(root); // initialize
    }
    
    /**
     * Update scene from the left nodes to the right nodes ... to guarantee the order of nodes.
     * @param changed or new inserted node
     */
    public void update(Node changed) {
    	int id = 1;
    	
    	List<Node> cn = root.getChildNodes();
    	List<Node> right = new ArrayList<Node>();
    	for (int i=0; cn!=null && i<cn.size(); ++i){
    		Node n = cn.get(i);
    		if (n.getLocation() == NodeLocation.LEFT) {
    			id = update(n, changed, id);
    		} else {
    			right.add(n);
    		}
    	}
    	
    	for (int i=0; i<right.size(); ++i){
    		Node n = right.get(i);
    		id = update(n, changed, id);
    	}
    	
    	map.renderEditor(canvas, getNodeWidgets(), root);
    }
    
    public void editSelectedNode() {
    	if (selected != null) {
    		selected.edit();
    	}
    }
    
    public void removeSelectedNode() {
    	if (selected != null) {
    		removeNodeWidget(selected);
    		selected.getNode().remove();
    		selected = null;
    		update(null);
    	}
    }
    
    public void createTextChildNode() {
    	Node child = new Node();
    	child.setContent("");
    	insertChildNode(child);
    	
    	 // to ensure that the node can be focused after insert
    	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
        		editSelectedNode();
            }
        });
    }
    
    public void copySelectedNode() {
    	if (selected != null) {
    		copied = selected.getNode().clone();
    	}
    }
    
    public void cutSelectedNode() {
    	if (selected != null) {
    		copySelectedNode();
    		removeSelectedNode();
    	}
    }
    
    /**
     * FIXME: conflict between CTRL+key for text and for nodes, cannot copy a text in node and paste the text
     */
    public void pasteNode() {
    	if (copied != null) {
    		//insertChildNode(copied.clone());
    	}
    }
    
    public void selectUpperNode() {
    	if (selected != null) {
    		int id = container.getWidgetIndex(selected);
    		
    		// get previous node to have number of child nodes
    		Node snode = selected.getNode();
    		List<Node> cn = snode.getParent().getChildNodes();
            Node n = null, child = null;
            for(int i=0; cn != null && i<cn.size(); ++i){
                n = child;
                child = cn.get(i);
                if(child == snode){
                    break;
                }
                
            }
            
            NodeWidget upper = (NodeWidget)container.getWidget(id - n.getNumberOfChildNodes() - 1);
            if (upper.getNode().getParent() == selected.getNode().getParent()) { // has upper node
            	selectNode(upper);
            }
    	}
    }
    
    public void selectUnderNode() {
    	if (selected != null) {
    		int id = container.getWidgetIndex(selected);
            NodeWidget under = (NodeWidget)container.getWidget(id + selected.getNode().getNumberOfChildNodes() + 1);
            if (under.getNode().getParent() == selected.getNode().getParent()) { // has upper node
            	selectNode(under);
            }
    	}
    }
    
    public void selectLeftNode() {
    	if (selected != null) {
    		if (selected.getNode().getLocation() == NodeLocation.LEFT) {
    			selectNext();
    		} else if (selected.getNode().getLocation() == NodeLocation.RIGHT) {
    			selectPrevious();
    		} else {
    			selectNext();
    		}
    	}
    }
    
    public void selectRightNode() {
    	if (selected != null) {
    		if (selected.getNode().getLocation() == NodeLocation.LEFT) {
    			selectPrevious();
    		} else if (selected.getNode().getLocation() == NodeLocation.RIGHT) {
    			selectNext();
    		} else {
    			int id = container.getWidgetIndex(selected);
                NodeWidget next = (NodeWidget)container.getWidget(id + selected.getNode().getNumberOfLeftChildNodes() + 1);
            	selectNode(next);
    		}
    	}
    }
    
    /**======================================================*/
    
    /**
     * FIXME: doesn't work if we want to get back to root when we created a left child of root
     */
    private void selectPrevious() {
    	if (selected != null) {
	    	// get number of all the previous nodes in parent node
			Node snode = selected.getNode();
			List<Node> cn = snode.getParent().getChildNodes();
	        Node child = null;
	        int count = 0;
	        for(int i=0; cn != null && i<cn.size(); ++i){
	            count += (child != null)?child.getNumberOfChildNodes()+1:0;
	            child = cn.get(i);
	            if(child == snode){
	                break;
	            }
	        }
	        
	        int id = container.getWidgetIndex(selected);
	        NodeWidget prev = (NodeWidget)container.getWidget(id - count - 1);
	        if (prev.getNode() == selected.getNode().getParent()) { // selected is child of previous node
	        	selectNode(prev);
	        }
    	}
    }
    
    private void selectNext() {
    	if (selected != null) {
    		int id = container.getWidgetIndex(selected);
            NodeWidget next = (NodeWidget)container.getWidget(id + 1);
            if (next.getNode().getParent() == selected.getNode()) { // selected is parent of next node
            	selectNode(next);
            }
    	}
    }
    
    /**
     * FIXME: fix add left node with parent root - there are strange widths
     * @param child
     */
    private void insertChildNode(Node child) {
    	if (selected != null) {
    		
    		NodeLocation loc = selected.getNode().getLocation();
    		int offset = selected.getNode().getNumberOfChildNodes() + 1;
    		if (selected.getNode().getParent() == null) {
    			loc = map.getRootChildNodeLocation(root);
    			if (loc == NodeLocation.LEFT) {
    				offset = selected.getNode().getNumberOfLeftChildNodes() + 1;
    			}
    		}
    		child.setLocation(loc);
    		selected.getNode().addChild(child);
    		update(child);
    		
    		// select new child node
    		int id = container.getWidgetIndex(selected);
    		selectNode((NodeWidget)container.getWidget(id + offset)); 
    	}
    }
    
    private void removeNodeWidget(NodeWidget nw) {
    	if (nw.getNode() == root) { // root cannot be removed
    		return;
    	}
    	int id = container.getWidgetIndex(nw);
    	int count = nw.getNode().getNumberOfChildNodes();
    	for (int i=-1; i<count; ++i) {
    		container.remove(id);
    	}
    }
    
    private int update(Node current, Node changed, int id) {
    	
    	if (current == changed || id > container.getWidgetCount() - NODE_WIDGET_MARK - 1) { // -1 because of root
    		id = insertNode(current, id); // recursively insert the node
    		return id; // we don't have to insert it recursively again, so return
    	}
    	
    	id++;
    	
    	List<Node> cn = current.getChildNodes();
    	for(int i=0; cn!=null && i<cn.size(); ++i){
    		Node n = cn.get(i);
			id = update(n, changed, id);
    	}
    	
    	return id;
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
    
    private NodeWidget createNodeWidget(Node node) {
    	switch(node.getType()){
	        case String: {
	            return new TextNodeWidget(node);
	        }
	    }
    	
    	return null;
    }
    
    private int insertNode(Node node, int id) {
    	NodeWidget nw = createNodeWidget(node);
    	if (id < container.getWidgetCount() - NODE_WIDGET_MARK) {
    		container.insert(nw, 0, 0, id + NODE_WIDGET_MARK);
    	} else {
    		container.add(nw, 0, 0);
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
