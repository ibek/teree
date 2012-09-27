package org.teree.client.view.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.scheme.MindMap;
import org.teree.client.scheme.SchemeType;
import org.teree.client.scheme.Renderer;
import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.event.BrowseItems;
import org.teree.client.view.editor.event.BrowseItemsHandler;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.NodeChangedHandler;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.editor.event.SelectNodeHandler;
import org.teree.client.view.editor.storage.Browser;
import org.teree.client.view.editor.storage.BrowserLoadRequestHandler;
import org.teree.client.view.editor.storage.ItemType;
import org.teree.shared.data.scheme.ImageLink;
import org.teree.shared.data.scheme.Link;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;
import org.teree.shared.data.scheme.Node.NodeLocation;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
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
    private Renderer<NodeWidget> scheme;
    
    private AbsolutePanel container;
    private Canvas canvas;

    private NodeWidget selected;
    private Node copied;
    
    private Browser browser;
    private Modal browserWindow;
    private BrowserLoadRequestHandler browserLoadRequestHandler;
    
    public Scene() {
        
        setSchemeType(Settings.DEFAULT_SCHEME_TYPE);
        
        container = new AbsolutePanel();
        canvas = Canvas.createIfSupported();
        if (canvas == null) { // canvas is not supported
            // deal with it
        }
        
		browserWindow = new Modal(false);
		browserWindow.setTitle("Choose image");
		browser = new Browser();
		browserWindow.add(browser);
        
        bind();
        
        initWidget(container);
        
    }
    
    public void bind() {
        
    	container.addHandler(new SelectNodeHandler() {
            @Override
            public void select(SelectNode event) {
                selectNode(event.getNodeWidget());
            }
            
        }, SelectNode.TYPE);
        
    	container.addHandler(new BrowseItemsHandler() {
			@Override
			public void browse(BrowseItems event) {
				browserWindow.show();
				browserLoadRequestHandler.loadRequest(event.getType());
			}
		}, BrowseItems.TYPE);
        
        canvas.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectNode(null);
            }
        });
        
        container.addHandler(new NodeChangedHandler() {
			@Override
			public void changed(NodeChanged event) {
				update(event.getNode());
			}
		}, NodeChanged.TYPE);
        
    }
    
    public void setSchemeType(SchemeType type) {
    	switch(type) {
	    	case MindMap: {
	    		scheme = new MindMap<NodeWidget>();
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
    	
    	scheme.renderEditor(canvas, getNodeWidgets(), root);
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
    
    public void createImageChildNode() {
    	Node child = new Node();
    	child.setContent(new ImageLink());
    	insertChildNode(child);
    	
    	 // to ensure that the node can be focused after insert
    	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
        		editSelectedNode();
            }
        });
    }
    
    public void createLinkChildNode() {
    	Node child = new Node();
    	child.setContent(new Link());
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
    
    public void pasteNode() {
    	if (selected != null && copied != null) { // it has to be selected because of the conflict between copy of text and node
    		insertChildNode(copied.clone());
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
    
    public void changeBoldOfSelectedNode() {
    	if (selected != null) {
			NodeStyle style = selected.getNode().getStyleOrCreate();
			style.setBold(!style.isBold());
			selected.changeStyle(style);
    	}
    }
    
    public String getSchemePicture() {
        Canvas canvas = Canvas.createIfSupported();
        canvas.setCoordinateSpaceHeight(this.canvas.getOffsetHeight());
        canvas.setCoordinateSpaceWidth(this.canvas.getOffsetWidth());
        scheme.renderPicture(canvas, getNodeWidgets(), root);
        return canvas.toDataUrl();
    }
    
    public String getSchemeSamplePicture() {
        Canvas canvas = Canvas.createIfSupported();
        double scale = 0.7;
        canvas.setCoordinateSpaceHeight((int)(this.canvas.getOffsetHeight()*scale));
        canvas.setCoordinateSpaceWidth((int)(this.canvas.getOffsetWidth()*scale));
        canvas.getContext2d().scale(scale, scale);
        List<NodeWidget> nw = getNodeWidgets();
        scheme.renderPicture(canvas, nw, root);
        
        NodeWidget root = nw.get(0);
        int x,y,w,h;
        x = (int)(root.getAbsoluteLeft() - this.canvas.getAbsoluteLeft() + root.getWidgetWidth()/2 - Settings.SAMPLE_MAX_WIDTH/2/scale);
        if (x < 0) {
        	x = 0;
        	w = (int)(root.getAbsoluteLeft() - this.canvas.getAbsoluteLeft() + root.getWidgetWidth()/2 + Settings.SAMPLE_MAX_WIDTH/2/scale);
        	if (w > this.canvas.getOffsetWidth()) {
        		w = this.canvas.getOffsetWidth();
        	}
        } else {
        	w = (int)(Settings.SAMPLE_MAX_WIDTH/scale);
        }
        
        y = (int)(root.getAbsoluteTop() - this.canvas.getAbsoluteTop() + root.getWidgetHeight()/2 - Settings.SAMPLE_MAX_HEIGHT/2/scale);
        if (y < 0) {
        	y = 0;
        	h = (int)(root.getAbsoluteTop() - this.canvas.getAbsoluteTop() + root.getWidgetHeight()/2 + Settings.SAMPLE_MAX_HEIGHT/2/scale);
        	if (h > this.canvas.getOffsetHeight()) {
        		h = this.canvas.getOffsetHeight();
        	}
        } else {
        	h = (int)(Settings.SAMPLE_MAX_HEIGHT/scale);
        }
        
        ImageData data = canvas.getContext2d().getImageData(x*scale, y*scale, w*scale, h*scale);
        Canvas canvasTmp = Canvas.createIfSupported();
        canvasTmp.setCoordinateSpaceHeight(data.getHeight());
        canvasTmp.setCoordinateSpaceWidth(data.getWidth());
        Context2d context = canvasTmp.getContext2d();
        context.putImageData(data, 0, 0);
        
        return canvasTmp.toDataUrl();
    }
    
    public void setBrowserItems(List<?> items, ItemType type) {
    	browser.setBrowserItems(items, type);
    }
    
    public void setBrowserLoadRequestHandler(BrowserLoadRequestHandler handler) {
		browserLoadRequestHandler = handler;
	}
    
    /**======================================================*/

    private void selectPrevious() {
    	if (selected != null) {
    		
    		int count = 0;
	        int id = container.getWidgetIndex(selected);
    		
    		if (selected.getNode().getParent().getParent() == null) { // parent is root
    			count = id - NODE_WIDGET_MARK - 1; // to get the right index of the root
    		} else {
		    	// get number of all the previous nodes in parent node
				Node snode = selected.getNode();
				List<Node> cn = snode.getParent().getChildNodes();
		        Node child = null;
		        for(int i=0; cn != null && i<cn.size(); ++i){
		            count += (child != null)?child.getNumberOfChildNodes()+1:0;
		            child = cn.get(i);
		            if(child == snode){
		                break;
		            }
		        }
    		}
	        
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
    
    private void insertChildNode(Node child) {
    	if (selected != null) {
    		
    		NodeLocation loc = selected.getNode().getLocation();
    		int offset = selected.getNode().getNumberOfChildNodes() + 1;
    		if (selected.getNode().getParent() == null) {
    			loc = scheme.getRootChildNodeLocation(root);
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
	        case ImageLink: {
	        	return new ImageNodeWidget(node);
	        }
	        case Link: {
	        	return new LinkNodeWidget(node);
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
