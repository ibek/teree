package org.teree.client.event;

import org.teree.shared.data.Node;

import com.google.gwt.event.shared.GwtEvent;

public class MapReceived extends GwtEvent<MapReceivedHandler> {
    
    public static Type<MapReceivedHandler> TYPE = new Type<MapReceivedHandler>();

    private Node root;
    
    public MapReceived(Node root) {
        this.root = root;
    }
    
    public Node getRoot() {
    	return root;
    }
    
    @Override
    public Type<MapReceivedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MapReceivedHandler handler) {
        handler.received(this);
    }
}
