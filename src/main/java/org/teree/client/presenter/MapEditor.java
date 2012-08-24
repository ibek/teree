package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.teree.client.event.GlobalKeyUp;
import org.teree.client.event.GlobalKeyUpHandler;
import org.teree.client.event.MapReceived;
import org.teree.client.event.MapReceivedHandler;
import org.teree.client.view.KeyAction;
import org.teree.shared.data.Node;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class MapEditor implements Presenter {

    public interface Display extends KeyAction {
        HasClickHandlers getNewButton();
        HasClickHandlers getSaveButton();
        Widget asWidget();
        void setRoot(Node root);
    }
    
    @Inject @Named(value="eventBus")
    private HandlerManager eventBus;
    
    @Inject
    private Display display;
    
    public void bind() {
    	eventBus.addHandler(MapReceived.TYPE, new MapReceivedHandler() {
			@Override
			public void received(MapReceived event) {
				display.setRoot(event.getRoot());
			}
		});
    	
    	eventBus.addHandler(GlobalKeyUp.TYPE, new GlobalKeyUpHandler() {
			@Override
			public void onKeyUp(GlobalKeyUp event) {
				int key = event.getKey();
				//System.out.println("key="+key);
				
				if (key == 113)  // #F2
				{
                    display.edit();
                }
				else if (key == 45) // Insert
				{
                	
                }
				else if (key == KeyCodes.KEY_DELETE)
				{
					display.delete();
				}
				else if (key == 67) // C - copy
				{
                	
                }
				else if (key == 88) // X - cut
				{
                	
                }
				else if (key == 86) // V - paste
				{
                	
                }
				else if (key == KeyCodes.KEY_UP)
				{
                	
                }
				else if (key == KeyCodes.KEY_DOWN)
				{
                	
                }
				else if (key == KeyCodes.KEY_LEFT)
				{
                	
                }
				else if (key == KeyCodes.KEY_RIGHT)
				{
                	
                }
			}
		});
    	
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
