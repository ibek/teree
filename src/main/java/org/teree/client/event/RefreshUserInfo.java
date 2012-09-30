package org.teree.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class RefreshUserInfo extends GwtEvent<RefreshUserInfoHandler> {
    
    public static Type<RefreshUserInfoHandler> TYPE = new Type<RefreshUserInfoHandler>();
    
    public RefreshUserInfo() {

    }

    @Override
    public Type<RefreshUserInfoHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RefreshUserInfoHandler handler) {
        handler.refresh(this);
    }
}
