package org.teree.client.presenter;

import com.google.gwt.event.dom.client.HasClickHandlers;

public interface HeaderTemplate {
    HasClickHandlers getCreateLink();
    HasClickHandlers getExploreLink();
    HasClickHandlers getHelpLink();
}
