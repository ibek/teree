package org.teree.client.view.editor.event;

import org.teree.shared.data.common.Node;

import com.google.gwt.event.shared.GwtEvent;

public class NodeChanged extends GwtEvent<NodeChangedHandler> {
    
    public static Type<NodeChangedHandler> TYPE = new Type<NodeChangedHandler>();

    private Node node;
    
    public NodeChanged(Node node) {
        this.node = node;
    }
    
    public Node getNode() {
    	return node;
    }
    
    @Override
    public Type<NodeChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NodeChangedHandler handler) {
        handler.changed(this);
    }
    
}
