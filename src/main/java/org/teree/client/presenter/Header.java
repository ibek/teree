package org.teree.client.presenter;

import com.google.gwt.event.dom.client.HasClickHandlers;

public interface Header {
    HasClickHandlers getCreateLink();
    HasClickHandlers getExploreLink();
    HasClickHandlers getHelpLink();
}
