package org.teree.client.view.editor.event;

import org.teree.client.view.editor.NodeWidget;

import com.google.gwt.event.shared.GwtEvent;

public class NodeChanged extends GwtEvent<NodeChangedHandler> {
    
    public static Type<NodeChangedHandler> TYPE = new Type<NodeChangedHandler>();

    private NodeWidget node;
    
    public NodeChanged(NodeWidget node) {
        this.node = node;
    }
    
    @Override
    public Type<NodeChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NodeChangedHandler handler) {
        handler.changed(this, node);
    }
    
}
