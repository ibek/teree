package org.teree.client.event;

import org.teree.shared.data.Scheme;
import org.teree.shared.data.UserInfo;

import com.google.gwt.event.shared.GwtEvent;

public class UserInfoReceived extends GwtEvent<UserInfoReceivedHandler> {
    
    public static Type<UserInfoReceivedHandler> TYPE = new Type<UserInfoReceivedHandler>();

    private UserInfo ui;
    
    public UserInfoReceived(UserInfo ui) {
        this.ui = ui;
    }

    public UserInfo getUserInfo() {
		return ui;
	}

	@Override
    public Type<UserInfoReceivedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserInfoReceivedHandler handler) {
        handler.received(this);
    }
}
