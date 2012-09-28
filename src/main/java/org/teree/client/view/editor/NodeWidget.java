package org.teree.client.view.editor;

import org.teree.client.view.NodeInterface;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.storage.ItemWidget;
import org.teree.client.view.resource.NodeCssStyle;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;

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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public abstract class NodeWidget extends Composite implements NodeInterface {
    
    protected NodeCssStyle resources = NodeCssStyle.INSTANCE;
    
    protected Node node;
    
    protected AbsolutePanel container;
    
    protected boolean selected;
    
    public NodeWidget(Node node) {
        this.node = node;
        selected = false;
        container = new AbsolutePanel();
        resources.css().ensureInjected();
        
        initWidget(container);
        
        changeStyle(node.getStyle());
        
    	DOM.setStyleAttribute(getElement(), "visibility", "hidden");
    }
	
	public abstract void edit();
	
	public abstract void changeStyle(NodeStyle style);
    
    public NodeWidget select() {
        selected = true;
        return this;
    }
    
    public NodeWidget unselect() {
        selected = false;
        return null;
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
				container.getElement().getStyle().setBackgroundColor("#ffa");
			}
		});
        
        content.addDragLeaveHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
				container.getElement().getStyle().setBackgroundColor("white");
			}
		});
        
        content.addDropHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
				container.getElement().getStyle().setBackgroundColor("white");
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

	public void setBrowserItem(ItemWidget iw) {
		// nothing to do
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
