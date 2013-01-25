package org.teree.client.view.editor.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.CurrentPresenter;
import org.teree.client.presenter.Editor;
import org.teree.client.view.NodeInterface;
import org.teree.client.view.editor.ConnectorNodeWidget;
import org.teree.client.view.editor.ImageNodeWidget;
import org.teree.client.view.editor.LinkNodeWidget;
import org.teree.client.view.editor.MathExpressionNodeWidget;
import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.Scene;
import org.teree.client.view.editor.TextNodeWidget;
import org.teree.client.view.editor.event.SelectedNodeListener;
import org.teree.client.view.resource.MathExpressionTools;
import org.teree.client.view.resource.PageStyle;
import org.teree.client.visualization.Renderer;
import org.teree.client.visualization.tree.HierarchicalHotizontal;
import org.teree.client.visualization.tree.MindMap;
import org.teree.client.visualization.tree.TreeRenderer;
import org.teree.shared.data.common.Connector;
import org.teree.shared.data.common.IconText;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeStyle;
import org.teree.shared.data.common.Node.NodeLocation;
import org.teree.shared.data.tree.Tree;
import org.teree.shared.data.tree.TreeType;

import com.github.gwtbootstrap.client.ui.constants.BaseIconType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.FrameElement;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

public class TreeController<T extends Widget & NodeInterface> extends BehaviorController<T> {
	
	private static final int NODE_WIDGET_MARK = 1; // from this mark are node widgets in container

	private Tree tree;

    private NodeWidget selected;
    private Node copied;
    private TreeRenderer<T> renderer;
	
	public TreeController(Class<T> clazz, AbsolutePanel container, Canvas canvas, Frame tmpFrame, Tree tree) {
		super(clazz, container, canvas, tmpFrame);

		this.tree = tree;
		init();
		setTreeType(tree.getVisualization());
	}

	@Override
	public void renderEditor(Canvas canvas, List<T> nodes) {
		renderer.renderEditor(canvas, nodes, tree.getRoot());
	}

	@Override
	public void renderViewer(Canvas canvas, List<T> nodes) {
		renderer.renderViewer(canvas, nodes, tree.getRoot());
	}

	@Override
	public int[] renderPicture(Canvas canvas, List<T> nodes) {
		return renderer.renderPicture(canvas, nodes, tree.getRoot());
	}

	@Override
	public void insertChildNode(Node childNode) {
		NodeLocation loc = selected.getNode().getLocation();
		int offset = selected.getNode().getNumberOfChildNodes() + 1;
		if (selected.getNode().getParent() == null) {
			loc = tree.getVisualization().getRootChildNodeLocation(tree.getRoot());
			if (loc == NodeLocation.LEFT) {
				offset = selected.getNode().getNumberOfLeftChildNodes() + 1;
			}
		}
		childNode.setLocation(loc);
		selected.getNode().addChild(childNode);
		int newid = container.getWidgetIndex(selected) + offset;
		update(childNode);
		// select new child node
		selectNode((NodeWidget)container.getWidget(newid));
	}

	@Override
	public void insertNodeBefore(Node inserted) {
		if (selected.getParent() != null) {
			NodeLocation loc = selected.getNode().getLocation();
			inserted.setLocation(loc);
			selected.getNode().insertBefore(inserted);
			int id = container.getWidgetIndex(selected);
			update(inserted);
			// select new child node
			selectNode((NodeWidget)container.getWidget(id));
		}
	}

	@Override
	public void insertNodeAfter(Node inserted) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void editNode() {
		if (selected != null) {
    		selected.edit();
    	}
	}

	@Override
	public void removeNode() {
		removeNodeWidget(selected);
		selected.getNode().remove();
		if (selected.getNode().getParent() != null) {
			selected = null;
		}
	}
	
	/**
     * Update scene from the left nodes to the right nodes ... to guarantee the order of nodes.
     * @param changed or new inserted node
     */
	@Override
	public void update(Node changed) {
		int id = 1;
    	
    	Node root = tree.getRoot();
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

	@Override
	public void selectLeftNode() {
		switch (tree.getVisualization()) {
			case MindMap: {
				if (selected.getNode().getLocation() == NodeLocation.LEFT) {
	    			selectNext();
	    		} else if (selected.getNode().getLocation() == NodeLocation.RIGHT) {
	    			selectPrevious();
	    		} else {
	    			selectNext();
	    		}
				break;
			}
			case HierarchicalHorizontal: {
				if (selected.getNode().getLocation() != null) {
					selectPrevious();
				} else {
					selectNext();
				}
				break;
			}
		}
	}

	@Override
	public void selectRightNode() {
		switch (tree.getVisualization()) {
			case MindMap: {
	    		if (selected.getNode().getLocation() == NodeLocation.LEFT) {
	    			selectPrevious();
	    		} else if (selected.getNode().getLocation() == NodeLocation.RIGHT) {
	    			selectNext();
	    		} else {
	    			int id = container.getWidgetIndex(selected);
	                NodeWidget next = (NodeWidget)container.getWidget(id + selected.getNode().getNumberOfLeftChildNodes() + 1);
	            	selectNode(next);
	    		}
				break;
			}
			case HierarchicalHorizontal: {
				if (selected.getNode().getLocation() != null) {
					selectNext();
				}
				break;
			}
		}
	}

	@Override
	public void selectUpperNode() {
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

	@Override
	public void selectUnderNode() {
		int id = container.getWidgetIndex(selected);
        NodeWidget under = (NodeWidget)container.getWidget(id + selected.getNode().getNumberOfChildNodes() + 1);
        if (under.getNode().getParent() == selected.getNode().getParent()) { // has upper node
        	selectNode(under);
        }
	}

    public void selectNode(NodeWidget node) {
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

	@Override
	public void copyNode() {
		if (selected != null) {
			copied = selected.getNode().clone();
		}
	}

	@Override
	public void cutNode() {
		if (selected != null) {
			copyNode();
			removeNode();
		}
	}

	@Override
	public void pasteNode() {
		if (selected != null && copied != null) { // it has to be selected because of the conflict between copy of text and node
    		insertChildNode(copied.clone());
    	}
	}

	@Override
	public boolean setNodeIcon(IconType icon) {
		boolean updateReq = false;
		if (selected != null && selected instanceof TextNodeWidget) {
    		IconText it = (IconText)selected.getNode().getContent();
    		updateReq = it.getIconType() == null;
    		if (it.getIconType() != null && (icon == IconType.SIGN_BLANK || icon == IconType.valueOf(it.getIconType()))) {
       		 	// remove icon
				it.setIconType(null);
				updateReq = true;
			} else {
    			it.setIconType(icon.name());
    		}
			((TextNodeWidget) selected).update();
    	}
		return updateReq;
	}

	@Override
	public void boldNode() {
		NodeStyle style = selected.getNode().getStyleOrCreate();
		style.setBold(!style.isBold());
		selected.changeStyle(style);
	}

	@Override
	public void splitAndConnectNode() {
		if (selected instanceof TextNodeWidget && !(selected instanceof ConnectorNodeWidget)) {
			final Tree t = new Tree();
			Node root = selected.getNode().clone();
			List<Node> childNodes = root.getChildNodes();
			for (int i=0; childNodes != null && i<childNodes.size(); ++i) {
				childNodes.get(i).setLocation(root.getLocation());
			}
			t.setRoot(root);
			t.setVisualization(tree.getVisualization());
			
			final Scene scene = new Scene();
			scene.addStyleName(PageStyle.INSTANCE.css().scene());
			
			FrameElement frameElt = tmpFrame.getElement().cast();
			Document frameDoc = frameElt.getContentDocument();
			frameDoc.getBody().appendChild(scene.getElement());
			
			scene.setScheme(t);
			
	        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		        @Override
		        public void execute() {
		        	t.setSchemePicture(scene.getSchemePicture());
		        	
		    		FrameElement frameElt = tmpFrame.getElement().cast();
		    		Document frameDoc = frameElt.getContentDocument();
		    		frameDoc.getBody().removeChild(scene.getElement());
		    		
		            final NodeWidget oldSelected = selected;
		    		((Editor)CurrentPresenter.getInstance().getPresenter()).insertScheme(t, new RemoteCallback<String>() {
		                @Override
		                public void callback(String response) {
		                    Node connector = new Node();
		                    Connector con = new Connector();
		                    IconText it = new IconText();
		                    IconText rc = (IconText)t.getRoot().getContent();
		                    it.setText(rc.getText());
		                    it.setIconType(rc.getIconType());
		                    con.setRoot(it);
		                    con.setOid(response);
		                    connector.setContent(con);
		                    NodeStyle ns = t.getRoot().getStyle();
		                    if (ns != null) {
		                    	connector.setStyle(ns.clone());
		                    }
		                    
		                    insertNodeBefore(connector);
		                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		            	        @Override
		            	        public void execute() {
		            	        	selected = oldSelected;
		            	        	removeNode();
		            	        }
		                    });
		                }
		            });
		        }
	        });
		}
	}

	@Override
	public void mergeConnectorNode() {
		if (selected instanceof ConnectorNodeWidget) {
			final Node n = selected.getNode();
			Connector con = (Connector)n.getContent();
	        final NodeWidget oldSelected = selected;
			((Editor)CurrentPresenter.getInstance().getPresenter()).getScheme(con.getOid(), new RemoteCallback<Tree>() {
				@Override
				public void callback(Tree response) {
					List<Node> childNodes = response.getRoot().getChildNodes();
					for (int i=0; childNodes != null && i<childNodes.size(); ++i) {
						childNodes.get(i).setLocation(n.getLocation());
					}
					insertNodeBefore(response.getRoot());
	                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        	        @Override
	        	        public void execute() {
	        	        	selected = oldSelected;
	        	        	removeNode();
	        	        }
	                });
				}
			});
		}
	}
    
    public void collapseAll(List<T> widgets, boolean collapse) {
    	for (int i=1; i<widgets.size(); ++i) {
			T nw = widgets.get(i);
			if (!collapse) { // for uncollapse update content - necessary for images
				nw.update();
			}
			if (nw.getNode().getChildNodes() != null && 
					!nw.getNode().getChildNodes().isEmpty() && 
					nw.getNode().getParent().getParent() == null &&
					!(nw instanceof LinkNodeWidget)) {
				nw.setCollapsed(collapse);
			}
		}
		update(null);
    }
    
	@Override
    public List<T> getNodeWidgets() {
    	Iterator<Widget> it = container.iterator();
    	List<T> nodes = new ArrayList<T>();
    	while (it.hasNext()) {
    		Widget w = it.next();
    		try {
    			T tw = (T)w;
    			nodes.add(tw); // there is the casting from Widget to NodeWidget
    		} catch (ClassCastException cce) {
    			// ignore
    		}
    	}
    	
    	return nodes;
    }
	
	//////////////////////////////////////////////////////////////////////////

    private boolean requiresRender = false;
	private void init() {
		Node root = tree.getRoot();
    	container.clear();
        container.add(canvas);
        
        T nw = createNodeWidget(root);
        container.add(nw, 0, 0);
        
        update(root); // initialize
        if (requiresRender) { // to fix size and position of math expressions
        	requiresRender = false;
        	Timer t = new Timer() {
				@Override
				public void run() {
					update(null);
				}
        	};
        	t.schedule(1000);
        }
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        @Override
	        public void execute() {
	        	selectNode(null); // to call listeners and in edit panel check the buttons to disable unnecessary
	        }
        });
	}
    
    private void setTreeType(TreeType type) {
    	switch(type) {
	    	case MindMap: {
	    		renderer = new MindMap<T>();
	    		break;
	    	}
	    	case HierarchicalHorizontal: {
	    		renderer = new HierarchicalHotizontal<T>();
	    		break;
	    	}
    	}
    }
	
	private T createNodeWidget(Node node) {
		T newnw = null;
		try {
			Method m = clazz.getMethod("createNodeWidget", Node.class);
			newnw = (T)m.invoke(node);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (newnw != null)?newnw:null;
	}
	
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
    
    private void removeNodeWidget(NodeWidget nw) {
    	int id = container.getWidgetIndex(nw);
    	int count = nw.getNode().getNumberOfChildNodes();
    	int i = -1; 
    	if (nw.getNode() == tree.getRoot()) {
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
    
    private int insertNode(Node node, int id) {
    	T nw = createNodeWidget(node);
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
    
    private void fireSelectedNode(NodeWidget nw) {
    	for (int i=0; slisteners != null && i<slisteners.size(); ++i) {
    		slisteners.get(i).selected(nw);
    	}
    }

}
