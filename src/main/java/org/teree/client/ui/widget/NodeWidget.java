package org.teree.client.ui.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public class NodeWidget extends Composite {

    public interface Resources extends ClientBundle {
        
        @Source("add.png")
        ImageResource addIcon();
        
        @Source("up.png")
        ImageResource upIcon();
        
        @Source("down.png")
        ImageResource downIcon();
        
        Styles nodeButtons();

        public interface Styles extends CssResource {
            String button();
        }
        
    }
    
    private Resources _resources = GWT.create(Resources.class);
    
    

}
