package org.teree.client.presenter;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class MapEditor implements Presenter {

    public interface Display {
        HasClickHandlers getNewButton();
        HasClickHandlers getSaveButton();
        Widget asWidget();
    }
    
    @Inject
    private HandlerManager eventBus;
    
    @Inject
    private Display display;
    
    public void bind() {
        display.getNewButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                
            }
        });
        
        display.getSaveButton().addClickHandler(new ClickHandler() {            
            @Override
            public void onClick(ClickEvent event) {
                
            }
        });
    }
    
    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
    }

}
