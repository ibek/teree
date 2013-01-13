package org.teree.client.view.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.CurrentPresenter;
import org.teree.client.Settings;
import org.teree.client.presenter.SchemeEditor;
import org.teree.client.scheme.HierarchicalHotizontal;
import org.teree.client.scheme.MindMap;
import org.teree.client.scheme.SchemeType;
import org.teree.client.scheme.Renderer;
import org.teree.client.text.UIMessages;
import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.NodeChangedHandler;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.editor.event.SelectNodeHandler;
import org.teree.client.view.editor.event.SelectedNodeListener;
import org.teree.client.view.resource.MathExpressionTools;
import org.teree.client.view.resource.PageStyle;
import org.teree.shared.data.scheme.Connector;
import org.teree.shared.data.scheme.IconText;
import org.teree.shared.data.scheme.ImageLink;
import org.teree.shared.data.scheme.Link;
import org.teree.shared.data.scheme.MathExpression;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;
import org.teree.shared.data.scheme.Node.NodeLocation;
import org.teree.shared.data.scheme.Scheme;

import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.FrameElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class Scene extends Composite {
	
	private static final int NODE_WIDGET_MARK = 1; // from this mark are node widgets in container
    
    private Scheme scheme;
    private Renderer<NodeWidget> renderer;
    
    private AbsolutePanel container;
    private Canvas canvas;

    private NodeWidget selected;
    private Node copied;
    
    public Scene() {

        setSchemeType(Settings.DEFAULT_SCHEME_TYPE);
        
        container = new AbsolutePanel();
        Style style = container.getElement().getStyle();
        style.setProperty("margin", "0 auto");
        style.setProperty("textAlign", "center");
        
        canvas = Canvas.createIfSupported();
        if (canvas == null) { // canvas is not supported
            // deal with it
        }
        
        final ScrollPanel sp = new ScrollPanel(container);
        sp.setWidth(Window.getClientWidth()+"px");
        sp.setHeight((Window.getClientHeight()-Settings.SCENE_HEIGHT_LESS)+"px");
        Window.addResizeHandler(new ResizeHandler() {
            @Override
			public void onResize(ResizeEvent event) {
                sp.setWidth(event.getWidth() + "px");
                sp.setHeight((event.getHeight()-Settings.SCENE_HEIGHT_LESS) + "px");
                update(null);
            }
		});
        initWidget(sp);
        
        sp.getParent().addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectNode(null);
			}
		}, ClickEvent.getType());
        
        bind();
        
    }
    
    private void bind() {
        
    	container.addHandler(new SelectNodeHandler() {
            @Override
            public void select(SelectNode event) {
                selectNode(event.getNodeWidget());
            }
            
        }, SelectNode.TYPE);
        
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
	    		renderer = new MindMap<NodeWidget>();
	    		break;
	    	}
	    	case HierarchicalHorizontal: {
	    		renderer = new HierarchicalHotizontal<NodeWidget>();
	    		break;
	    	}
    	}
    }
    
    public void setScheme(Scheme scheme) {
    	this.scheme = scheme;
    	
    	Node root = scheme.getRoot();
    	container.clear();
        container.add(canvas);
        
        NodeWidget nw = createNodeWidget(root);
        container.add(nw, 0, 0);
        
        update(root); // initialize
        
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        @Override
	        public void execute() {
	        	selectNode(null); // to call listeners and in edit panel check the buttons to disable unnecessary
	        }
        });
        
    }
    
    /**
     * Update scene from the left nodes to the right nodes ... to guarantee the order of nodes.
     * @param changed or new inserted node
     */
    public void update(Node changed) {
    	int id = 1;
    	
    	Node root = scheme.getRoot();
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
    	
    	renderer.renderEditor(canvas, getNodeWidgets(), root);
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
    		if (selected.getNode().getParent() != null) {
    			selected = null;
    		}
    		update(null);
    	}
    }
    
    public void createTextChildNode() {
    	Node child = new Node();
    	IconText content = new IconText();
    	content.setText("");
    	child.setContent(content);
    	createChildNode(child);
    }
    
    public void createImageChildNode() {
    	Node child = new Node();
    	child.setContent(new ImageLink());
    	createChildNode(child);
    }
    
    public void createLinkChildNode() {
    	Node child = new Node();
    	child.setContent(new Link());
    	createChildNode(child);
    }
    
    public void createMathExpressionChildNode() {
    	Node child = new Node();
    	MathExpression me = new MathExpression();
    	me.setExpression("");
    	child.setContent(me);
    	createChildNode(child);
    }
    
    public void createConnectorChildNode() {
    	Node child = new Node();
    	Connector con = new Connector();
    	con.setRoot(new IconText());
    	child.setContent(con);
    	createChildNode(child);
    }
    
    private void createChildNode(Node child) {
    	insertChildNode(child);
    	
	   	 // to ensure that the node can be focused after insert
	   	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        @Override
	        public void execute() {
	   			editSelectedNode();
	        }
        });
    }
    
    public void mergeSelectedConnector() {
    	if (selected != null && selected instanceof ConnectorNodeWidget) {
    		final Node n = selected.getNode();
    		Connector con = (Connector)n.getContent();
            final NodeWidget oldSelected = selected;
    		((SchemeEditor)CurrentPresenter.getInstance().getPresenter()).getScheme(con.getOid(), new RemoteCallback<Scheme>() {
				@Override
				public void callback(Scheme response) {
					List<Node> childNodes = response.getRoot().getChildNodes();
					for (int i=0; childNodes != null && i<childNodes.size(); ++i) {
						childNodes.get(i).setLocation(n.getLocation());
					}
					insertNodeBefore(response.getRoot());
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            	        @Override
            	        public void execute() {
            	        	selected = oldSelected;
            	        	removeSelectedNode();
            	        }
                    });
				}
			});
    	}
    }
    
    private Frame tmpFrame;
    public void splitSelectedNode(Frame tmpFrame) {
    	if (selected != null && selected instanceof TextNodeWidget && !(selected instanceof ConnectorNodeWidget)) {
    		this.tmpFrame = tmpFrame;
    		final Scheme s = new Scheme();
    		Node root = selected.getNode().clone();
    		List<Node> childNodes = root.getChildNodes();
    		for (int i=0; childNodes != null && i<childNodes.size(); ++i) {
    			childNodes.get(i).setLocation(root.getLocation());
    		}
    		s.setRoot(root);
    		
    		final Scene scene = new Scene();
    		scene.addStyleName(PageStyle.INSTANCE.css().scene());
    		
    		FrameElement frameElt = Scene.this.tmpFrame.getElement().cast();
    		Document frameDoc = frameElt.getContentDocument();
    		frameDoc.getBody().appendChild(scene.getElement());
    		
    		scene.setScheme(s);
    		
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
    	        @Override
    	        public void execute() {
    	        	s.setSchemePicture(scene.getSchemePicture());
    	        	
    	    		FrameElement frameElt = Scene.this.tmpFrame.getElement().cast();
    	    		Document frameDoc = frameElt.getContentDocument();
    	    		frameDoc.getBody().removeChild(scene.getElement());
    	    		
    	            final NodeWidget oldSelected = selected;
    	    		((SchemeEditor)CurrentPresenter.getInstance().getPresenter()).insertScheme(s, new RemoteCallback<String>() {
    	                @Override
    	                public void callback(String response) {
    	                    Node connector = new Node();
    	                    Connector con = new Connector();
    	                    IconText it = new IconText();
    	                    IconText rc = (IconText)s.getRoot().getContent();
    	                    it.setText(rc.getText());
    	                    it.setIconType(rc.getIconType());
    	                    con.setRoot(it);
    	                    con.setOid(response);
    	                    connector.setContent(con);
    	                    NodeStyle ns = s.getRoot().getStyle();
    	                    if (ns != null) {
    	                    	connector.setStyle(ns.clone());
    	                    }
    	                    
    	                    insertNodeBefore(connector);
    	                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
    	            	        @Override
    	            	        public void execute() {
    	            	        	selected = oldSelected;
    	            	        	removeSelectedNode();
    	            	        }
    	                    });
    	                }
    	            });
    	        }
            });
    	}
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
            
            if (n != null) {
	            NodeWidget upper = (NodeWidget)container.getWidget(id - n.getNumberOfChildNodes() - 1);
	            if (upper.getNode().getParent() == selected.getNode().getParent()) { // has upper node
	            	selectNode(upper);
	            }
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
        renderer.renderPicture(canvas, getNodeWidgets(), scheme.getRoot());
        return canvas.toDataUrl();
    }
    
    public String getSchemeSamplePicture() {
        Canvas canvas = Canvas.createIfSupported();
        double scale = 0.7;
        canvas.setCoordinateSpaceHeight((int)(this.canvas.getOffsetHeight()*scale));
        canvas.setCoordinateSpaceWidth((int)(this.canvas.getOffsetWidth()*scale));
        canvas.getContext2d().scale(scale, scale);
        List<NodeWidget> nw = getNodeWidgets();
        collapseAll(nw, true);
        int[] rp = renderer.renderPicture(canvas, nw, scheme.getRoot());
        NodeWidget rw = nw.get(0);
        int x,y,w,h;
        x = (int)(rp[0] + rw.getWidgetWidth()/2 - Settings.SAMPLE_MAX_WIDTH/2/scale);
        if (x < 0) {
        	x = 0;
        	w = (int)(rp[0] + rw.getWidgetWidth()/2 + Settings.SAMPLE_MAX_WIDTH/2/scale);
        	if (w > rp[2]) {
        		w = rp[2];
        	}
        } else {
        	w = (int)(Settings.SAMPLE_MAX_WIDTH/scale);
        }
        
        y = (int)(rp[1] + rw.getWidgetHeight()/2 - Settings.SAMPLE_MAX_HEIGHT/2/scale);
        if (y < 0) {
        	y = 0;
        	h = (int)(rp[1] + rw.getWidgetHeight()/2 + Settings.SAMPLE_MAX_HEIGHT/2/scale);
        	if (h > rp[3]) {
        		h = rp[3];
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
        collapseAll(nw, false);
        update(null);
        
        return canvasTmp.toDataUrl();
    }
    
    public void setSelectedIcon(IconType icon) {
    	if (selected != null && selected instanceof TextNodeWidget) {
    		IconText it = (IconText)selected.getNode().getContent();
    		boolean updateReq = it.getIconType() == null;
    		if (it.getIconType() != null && (icon == IconType.SIGN_BLANK || icon == IconType.valueOf(it.getIconType()))) {
       		 	// remove icon
				it.setIconType(null);
				updateReq = true;
			} else {
    			it.setIconType(icon.name());
    		}
			((TextNodeWidget) selected).update();
			if (updateReq) {
				update(null);
			}
    	}
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
    			loc = renderer.getRootChildNodeLocation(scheme.getRoot());
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
    
    private void insertNodeBefore(Node node) {
    	if (selected != null && selected.getParent() != null) {
    		NodeLocation loc = selected.getNode().getLocation();
    		node.setLocation(loc);
    		selected.getNode().insertBefore(node);
    		update(node);
    		
    		// select new child node
    		int id = container.getWidgetIndex(selected);
    		selectNode((NodeWidget)container.getWidget(id));
    	}
    }
    
    private void removeNodeWidget(NodeWidget nw) {
    	int id = container.getWidgetIndex(nw);
    	int count = nw.getNode().getNumberOfChildNodes();
    	int i = -1; 
    	if (nw.getNode() == scheme.getRoot()) {
    		// skip root
    		i++;
    		id++;
    	}
    	for (; i<count; ++i) {
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
    
    private boolean initMathScript = true;
    private NodeWidget createNodeWidget(Node node) {
    	switch(node.getType()){
    		case IconText: {
	            return new TextNodeWidget(node);
	        }
	        case ImageLink: {
	        	return new ImageNodeWidget(node);
	        }
	        case Link: {
	        	return new LinkNodeWidget(node);
	        }
	        case MathExpression: {
	        	if (initMathScript) {
	        		initMathScript = false;
	        		MathExpressionTools.initScript();
	        	}
	        	return new MathExpressionNodeWidget(node);
	        }
	        case Connector:{
	        	return new ConnectorNodeWidget(node);
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

    	fireSelectedNode(node);
        if (node != null) {
            selected = node.select();
        } else {
            selected = null;
        }
    }
    
    private List<SelectedNodeListener> slisteners;
    public void addSelectedNodeListener(SelectedNodeListener snl) {
    	if (slisteners == null) {
    		slisteners = new ArrayList<SelectedNodeListener>();
    	}
    	slisteners.add(snl);
    }
    
    private void fireSelectedNode(NodeWidget nw) {
    	for (int i=0; slisteners != null && i<slisteners.size(); ++i) {
    		slisteners.get(i).selected(nw);
    	}
    }
    
    private void collapseAll(List<NodeWidget> widgets, boolean collapse) {
    	for (int i=1; i<widgets.size(); ++i) {
    		NodeWidget nw = widgets.get(i);
    		if ((nw.getNode().getParent().getParent() == null || nw.getNode().getParent().getParent().getParent() == null) && // collapse first or second level of nodes 
    				nw.getNode().getChildNodes() != null && 
    				nw.getNode().getChildNodes().size() > 2) {
    			nw.setCollapsed(collapse);
    		}
    	}
    }
    
}
