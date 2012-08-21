package org.teree.client.event;

import org.teree.shared.data.Node;

import com.google.gwt.event.shared.EventHandler;

public interface NodeReceivedHandler extends EventHandler {

    public void received(NodeReceived event, Node root);
    
}
