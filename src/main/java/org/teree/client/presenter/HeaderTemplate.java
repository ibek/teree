package org.teree.client.presenter;

import org.teree.shared.data.UserInfo;

import com.google.gwt.event.dom.client.HasClickHandlers;

public interface HeaderTemplate {
    HasClickHandlers getCreateLink();
    HasClickHandlers getExploreLink();
    HasClickHandlers getHelpLink();
    public void setCurrentUser(UserInfo user);
}
