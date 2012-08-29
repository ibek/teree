package org.teree.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface SchemeReceivedHandler extends EventHandler {

    public void received(SchemeReceived event);
    
}
