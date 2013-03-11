package org.teree.client.view.type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.CurrentPresenter;
import org.teree.client.presenter.Editor;
import org.teree.client.view.NodeInterface;
import org.teree.client.view.common.NodeWidgetFactory;
import org.teree.client.view.editor.ConnectorNodeWidget;
import org.teree.client.view.editor.LinkNodeWidget;
import org.teree.client.view.editor.Scene;
import org.teree.client.view.editor.TextNodeWidget;
import org.teree.client.view.editor.event.SelectedNodeListener;
import org.teree.client.view.resource.PageStyle;
import org.teree.client.visualization.tree.HierarchicalHotizontal;
import org.teree.client.visualization.tree.MindMap;
import org.teree.client.visualization.tree.TreeRenderer;
import org.teree.shared.data.common.Connector;
import org.teree.shared.data.common.IconText;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.StructureType;
import org.teree.shared.data.common.Node.NodeLocation;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.tree.Tree;
import org.teree.shared.data.tree.TreeType;

import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.FrameElement;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class TreeController<T extends Widget & NodeInterface> extends BehaviorController<T> {
	
	private static final int NODE_WIDGET_MARK = 1; // from this mark are node widgets in container

	private Tree tree;

    private T selected;
    private Node copied;
    private TreeRenderer<T> renderer;
	
	public TreeController(AbsolutePanel container, Canvas canvas, Frame tmpFrame, NodeWidgetFactory<T> nodeFactory, Tree tree) {
		super(container, canvas, tmpFrame, nodeFactory);

		this.tree = tree;
		setTreeType(tree.getVisualization());
		init();
	}

	@Override
	public void render(Canvas canvas, List<T> nodes) {
		renderer.render(canvas, nodes, tree.getRoot());
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
		selectNode((T)container.getWidget(newid));
	}

	@Override
	public void insertNodeBefore(Node inserted) {
		if (selected.getNode().getParent() != null) {
			NodeLocation loc = selected.getNode().getLocation();
			inserted.setLocation(loc);
			selected.getNode().insertBefore(inserted);
			int id = container.getWidgetIndex(selected);
			update(inserted);
			// select new child node
			selectNode((T)container.getWidget(id));
		}
	}

	@Override
	public void insertNodeAfter(Node inserted) {
		if (selected.getNode().getParent() != null) {
			NodeLocation loc = selected.getNode().getLocation();
			inserted.setLocation(loc);
			selected.getNode().insertAfter(inserted);
			int id = container.getWidgetIndex(selected);
			update(inserted);
			// select new child node
			selectNode((T)container.getWidget(id+1));
		}
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
		update(null);
	}
	
	/**
     * Update scene from the left nodes to the right nodes ... to guarantee the order of nodes.
     * @param changed or new inserted node
     */
	@Override
	public void update(Node changed) {
		updateAndCollapse(changed, false);
	}
	
	@Override
	public void checkAllNodes() {
		Iterator<Widget> it = container.iterator();
    	while (it.hasNext()) {
    		Widget w = it.next();
    		if (w instanceof NodeInterface) {
        		T nw = (T)w;
    			nw.update();
    		}
    	}
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
	                T next = (T)container.getWidget(id + selected.getNode().getNumberOfLeftChildNodes() + 1);
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
            T upper = (T)container.getWidget(id - n.getNumberOfChildNodes() - 1);
            if (upper.getNode().getParent() == selected.getNode().getParent()) { // has upper node
            	selectNode(upper);
            }
        }
	}

	@Override
	public void selectUnderNode() {
		int id = container.getWidgetIndex(selected);
        T under = (T)container.getWidget(id + selected.getNode().getNumberOfChildNodes() + 1);
        if (under.getNode().getParent() == selected.getNode().getParent()) { // has upper node
        	selectNode(under);
        }
	}

    @Override
	public void selectNode(T node) {
        if (selected != null) { // only one node can be selected
        	selected.unselect();
        	selected = null;
        }

    	fireSelectedNode(node);
        if (node != null) {
            selected = node;
            selected.select();
        } else {
        	selected = null;
        }
    }
    
    @Override
    public T getSelectedNode() {
    	return selected;
    }
    
    private List<SelectedNodeListener<T>> slisteners;
    @Override
	public void addSelectedNodeListener(SelectedNodeListener<T> snl) {
    	if (slisteners == null) {
    		slisteners = new ArrayList<SelectedNodeListener<T>>();
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
	public void setNodeIcon(IconType icon) {
		boolean updateReq = false;
		if (selected != null && selected instanceof TextNodeWidget) {
    		IconText it = (IconText)selected.getNode().getContent();
    		updateReq = (it.getIconType() == null || it.getIconType().isEmpty());
    		if (it.getIconType() != null && (icon == IconType.SIGN_BLANK || icon == IconType.valueOf(it.getIconType()))) {
       		 	// remove icon
				it.setIconType(null);
				updateReq = true;
			} else {
    			it.setIconType(icon.name());
    		}
			((TextNodeWidget) selected).update();
    	}
		if (updateReq) {
			update(null);
		}
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
		        	t.setSchemePicture(scene.getSchemeSamplePicture());
		        	
		    		FrameElement frameElt = tmpFrame.getElement().cast();
		    		Document frameDoc = frameElt.getContentDocument();
		    		frameDoc.getBody().removeChild(scene.getElement());
		    		
		            final T oldSelected = selected;
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
		                    
		                    insertNodeBefore(connector);
		                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		            	        @Override
		            	        public void execute() {
		            	        	T tmpSelected = selected;
		            	        	selected = oldSelected;
		            	        	removeNode();
		            	        	selected = tmpSelected;
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
	        final T oldSelected = selected;
			CurrentPresenter.getInstance().getPresenter().getScheme(con.getOid(), new RemoteCallback<Scheme>() {
				@Override
				public void callback(Scheme response) {
					switch(response.getStructure()) {
						case Tree: {
							Tree tree = (Tree)response;
							List<Node> childNodes = tree.getRoot().getChildNodes();
							for (int i=0; childNodes != null && i<childNodes.size(); ++i) {
								childNodes.get(i).setLocation(n.getLocation());
							}
							insertNodeBefore(tree.getRoot());
			                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			        	        @Override
			        	        public void execute() {
			        	        	selected = oldSelected;
			        	        	removeNode();
			        	        }
			                });
							break;
						}
					}
				}
			});
		}
	}
    
    @Override
	public void collapseAll(List<T> widgets, boolean collapse) {
    	for (int i=1; i<widgets.size(); ++i) {
			T nw = widgets.get(i);
			if (!collapse) { // for uncollapse update content - necessary for images
				nw.update();
			}
			if (nw.getNode().getChildNodes() != null && 
					!nw.getNode().getChildNodes().isEmpty() && 
					nw.getNode().getParent() != null &&
					!(nw instanceof LinkNodeWidget)) {
				nw.setCollapsed(collapse);
			}
		}
    }

	@Override
	public void center() {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        @Override
	        public void execute() {
	    		Widget w = container.getParent();
	    		if (w instanceof ScrollPanel && container.getWidgetCount() > 1) {
	    			ScrollPanel sp = (ScrollPanel)w;
	    			Widget root = container.getWidget(1);
	    			int hsp = root.getAbsoluteLeft() - sp.getOffsetWidth()/2 + root.getOffsetWidth()/2;
	    			int vsp = root.getAbsoluteTop() - sp.getOffsetHeight()/2 - root.getOffsetHeight()/2;
	    			hsp = (hsp > 0)?hsp:0;
	    			vsp = (vsp > 0)?vsp:0;
	    			sp.setHorizontalScrollPosition(hsp);
	    			sp.setVerticalScrollPosition(vsp);
	    		}
	        }
        });
	}
    
	@Override
    public List<T> getNodeWidgets() {
    	Iterator<Widget> it = container.iterator();
    	List<T> nodes = new ArrayList<T>();
    	while (it.hasNext()) {
    		Widget w = it.next();
    		if (w instanceof NodeInterface) {
    			nodes.add((T)w); // there is the casting from Widget to NodeWidget
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
        
        T nw = nodeFactory.create(root);
        container.add(nw, 0, 0);

		updateAndCollapse(root, true);
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
        
        center();
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
	        
	        T prev = (T)container.getWidget(id - count - 1);
	        if (prev.getNode() == selected.getNode().getParent()) { // selected is child of previous node
	        	selectNode(prev);
	        }
    	}
    }
    
    private void selectNext() {
    	if (selected != null) {
    		int id = container.getWidgetIndex(selected);
            T next = (T)container.getWidget(id + 1);
            if (next.getNode().getParent() == selected.getNode()) { // selected is parent of next node
            	selectNode(next);
            }
    	}
    }
    
    private void removeNodeWidget(T nw) {
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
	
	private void updateAndCollapse(Node changed, boolean collapse) {
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
    	
    	List<T> nw = getNodeWidgets();
    	if (collapse) {
    		collapseAll(nw, collapse);
    	}
    	
    	renderer.render(canvas, nw, root);
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
    	T nw = nodeFactory.create(node);
    	if (id < container.getWidgetCount() - NODE_WIDGET_MARK) {
    		container.insert(nw, 0, 0, id + NODE_WIDGET_MARK);
    	} else {
    		container.add(nw, 0, 0);
    	}
    	id++;
    	
    	if (nodeFactory.needsRender()) {
    		Timer t = new Timer() {
				@Override
				public void run() {
					update(null);
				}
        	};
        	t.schedule(1000);
    	}
    	
    	List<Node> cn = node.getChildNodes();
        for(int i=0; cn != null && i<cn.size(); ++i){
        	id = insertNode(cn.get(i), id);
        }
    	
    	return id;
    }
    
    private void fireSelectedNode(T nw) {
    	for (int i=0; slisteners != null && i<slisteners.size(); ++i) {
    		slisteners.get(i).selected(nw);
    	}
    }

}
