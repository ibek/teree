package org.teree.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

public abstract interface Presenter {
    
    /**
     * Change current scene and generate the new scene into container. 
     * @param container
     */
    public abstract void go(final HasWidgets container);
    
}
