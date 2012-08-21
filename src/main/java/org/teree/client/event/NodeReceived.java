package org.teree.client.event;

import org.teree.shared.data.Node;

import com.google.gwt.event.shared.GwtEvent;

public class NodeReceived extends GwtEvent<NodeReceivedHandler> {
    
    public static Type<NodeReceivedHandler> TYPE = new Type<NodeReceivedHandler>();

    private Node root;
    
    public NodeReceived(Node root) {
        this.root = root;
    }
    
    @Override
    public Type<NodeReceivedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NodeReceivedHandler handler) {
        handler.received(this, root);
    }
}
