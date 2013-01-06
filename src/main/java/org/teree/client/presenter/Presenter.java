package org.teree.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

public interface Presenter {
    
    /**
     * Change current scene and generate the new scene into container. 
     * @param container
     */
    public void go(final HasWidgets container);
    
    public Template getTemplate();
    
    public String getTitle();
    
}
