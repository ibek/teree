package org.teree.client.view.editor;

import org.teree.client.view.NodeInterface;
import org.teree.client.view.common.NodeCategoryStyle;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.resource.NodeCssStyle;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeCategory;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.HasAllDragAndDropHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public abstract class NodeWidget extends Composite implements NodeInterface {
    
    protected NodeCssStyle resources = NodeCssStyle.INSTANCE;
    
    protected Node node;
    
    protected AbsolutePanel container;
    
    protected boolean selected;
    
    protected boolean collapsed = false;
    
    public NodeWidget(Node node) {
        this.node = node;
        selected = false;
        container = new AbsolutePanel();
        resources.css().ensureInjected();
        
        initWidget(container);
        
        bind();
        
    	DOM.setStyleAttribute(getElement(), "visibility", "hidden");
    }
	
	private void bind() {

        addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
            	event.stopPropagation();
                if (selected) { // second click - edit this node
                    edit();
                } else { // first click - select this node
    				if (collapsed) {
    					setCollapsed(false);
    					NodeWidget.this.getParent().fireEvent(new NodeChanged(null));
    				}
                	getParent().fireEvent(new SelectNode<NodeWidget>(NodeWidget.this));
                }
			}
		}, ClickEvent.getType());
        
        addDomHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.stopPropagation(); // prevents the moving handler in scene
			}
		}, MouseDownEvent.getType());
	}
    
    public void select() {
    	//container.getElement().getStyle().setBackgroundColor("#7FAF47");
    	container.getElement().getStyle().setBorderStyle(BorderStyle.DASHED);
    	container.getElement().getStyle().setBorderWidth(2.0, Unit.PX);
    	container.getElement().getStyle().setBorderColor("#08C");
    	container.getElement().getStyle().setProperty("borderBottom", "none");
    	container.getElement().getStyle().setProperty("borderTop", "none");
        selected = true;
    }
    
    public void unselect() {
    	container.getElement().getStyle().setBackgroundColor(null);
    	container.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
        selected = false;
    }
    
    protected void initDragging(HasAllDragAndDropHandlers content) {
        content.addDragStartHandler(new DragStartHandler() {
			@Override
			public void onDragStart(DragStartEvent event) {
				dragData(event);
			}
		});
        
        content.addDragOverHandler(new DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				
			}
		});
        
        content.addDragEnterHandler(new DragEnterHandler() {
			@Override
			public void onDragEnter(DragEnterEvent event) {
				container.getElement().getStyle().setBackgroundColor("#7FAF47");
			}
		});
        
        content.addDragLeaveHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
		    	container.getElement().getStyle().setBackgroundColor(null);
			}
		});
        
        content.addDropHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
		    	container.getElement().getStyle().setBackgroundColor(null);
                dropData(event);
			}
		});
    }
    
    public void dropData(DropEvent event) {
    	event.preventDefault();
        
        try {
        	Integer id = Integer.valueOf(event.getData("id"));
        	if (id != null) {
            	NodeWidget nw = (NodeWidget)((AbsolutePanel)getParent()).getWidget(id);
            	if (nw.getNode() == node) { // don't move the dragged node to the same node
            		return;
            	}
            	if (nw.getNode().isChildNode(node)) { // cannot move a node to its child node (bug5)
            		return;
            	}
                Node child = nw.getNode().clone();
                if (node.getLocation() != null) {
                	child.setLocation(node.getLocation());
                }
                node.addChild(child);
                
                // remove the moved nodes
                int count = child.getNumberOfChildNodes();
                nw.removeFromParent();
                for (int i=0; i<count; ++i) {
                	((NodeWidget)((AbsolutePanel)getParent()).getWidget(id)).removeFromParent();
                }
                nw.getNode().remove();
                
                getParent().fireEvent(new NodeChanged(child));
            }
        } catch(NumberFormatException ex) {
        	// ignore
        }
        
    }

	@Override
	public Node getNode() {
		return node;
	}
	
	@Override
	public int getWidgetWidth() {
		return getWidget().getOffsetWidth();
	}

	@Override
	public int getWidgetHeight() {
		return getWidget().getOffsetHeight();
	}
	
	@Override
	public void update() {
    	NodeCategoryStyle.set(this, node.getCategory());
	}
    
    @Override
    public void setCollapsed(boolean collapsed) {
    	this.collapsed = collapsed;
    }
	
	@Override
	public boolean isCollapsed() {
		return collapsed;
	}
	
	public void setNodeCategory(NodeCategory nc) {
		node.setCategory(nc);
    	NodeCategoryStyle.set(this, nc);
	}
    
    private void dragData(DragStartEvent event) {
    	if (node.getParent() == null) { // cannot drag root
    		event.stopPropagation();
    		return;
    	}
    	event.setData("id", String.valueOf(((AbsolutePanel)getParent()).getWidgetIndex(this)));
        event.getDataTransfer().setDragImage(container.getElement(), 10, 10);
    }

}
