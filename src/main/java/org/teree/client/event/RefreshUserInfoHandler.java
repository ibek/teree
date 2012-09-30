package org.teree.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface RefreshUserInfoHandler extends EventHandler {

    public void refresh(RefreshUserInfo event);
    
}
