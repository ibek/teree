package org.teree.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UserInfoReceivedHandler extends EventHandler {

    public void received(UserInfoReceived event);
    
}
