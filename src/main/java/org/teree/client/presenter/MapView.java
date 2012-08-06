package org.teree.client.presenter;

import javax.inject.Inject;

import org.teree.shared.data.Node;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class MapView implements Presenter {

    public interface Display {
        Widget asWidget();
        void setRoot(Node root);
    }
    
    @Inject
    private HandlerManager eventBus;
    
    @Inject
    private Display display;
    
    private Node root;
    
    public void bind() {
        
    }
    
    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
    }

}
