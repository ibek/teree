package org.teree.client.view.editor.event;

import org.teree.client.view.editor.NodeWidget;

import com.google.gwt.event.shared.GwtEvent;

public class SelectNode extends GwtEvent<SelectNodeHandler> {
    
    public static Type<SelectNodeHandler> TYPE = new Type<SelectNodeHandler>();

    private NodeWidget node;
    
    public SelectNode(NodeWidget node) {
        this.node = node;
    }
    
    public NodeWidget getNodeWidget() {
    	return node;
    }
    
    @Override
    public Type<SelectNodeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SelectNodeHandler handler) {
        handler.select(this);
    }
}
