package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.Settings;
import org.teree.client.Text;
import org.teree.client.event.GlobalKeyUp;
import org.teree.client.event.GlobalKeyUpHandler;
import org.teree.client.event.MapReceived;
import org.teree.client.event.MapReceivedHandler;
import org.teree.client.view.KeyAction;
import org.teree.shared.MapService;
import org.teree.shared.data.Node;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class MapEditor implements Presenter {

    public interface Display extends KeyAction {
        HasClickHandlers getNewButton();
        HasClickHandlers getSaveButton();
        HasClickHandlers getExploreLink();
        HasClickHandlers getHelpLink();
        Widget asWidget();
        void setRoot(Node root);
        void info(String msg);
        void error(String msg);
    }
    
    @Inject @Named(value="eventBus")
    private HandlerManager eventBus;
    
	@Inject
	private Caller<MapService> mapService;
    
    @Inject
    private Display display;
    
    private String oid;
    
    private Node root;
    
    public void bind() {
    	eventBus.addHandler(MapReceived.TYPE, new MapReceivedHandler() {
			@Override
			public void received(MapReceived event) {
				oid = event.getOid();
				root = event.getRoot();
				display.setRoot(root);
			}
		});
    	
    	eventBus.addHandler(GlobalKeyUp.TYPE, new GlobalKeyUpHandler() {
			@Override
			public void onKeyUp(GlobalKeyUp event) {
				Event e = event.getEvent();
				int key = e.getKeyCode();
				//System.out.println("key="+key);
				
				if (key == 113)  // #F2
				{
                    display.edit();
                }
				else if (key == 45) // Insert
				{
                	display.insert();
                }
				else if (key == KeyCodes.KEY_DELETE)
				{
					display.delete();
				}
				else if (key == 67 && e.getCtrlKey())
				{
                	display.copy();
                }
				else if (key == 88 && e.getCtrlKey())
				{
                	display.cut();
                }
				else if (key == 86 && e.getCtrlKey())
				{
                	display.paste();
                }
				else if (key == KeyCodes.KEY_UP)
				{
                	display.up();
                }
				else if (key == KeyCodes.KEY_DOWN)
				{
                	display.down();
                }
				else if (key == KeyCodes.KEY_LEFT)
				{
                	display.left();
                }
				else if (key == KeyCodes.KEY_RIGHT)
				{
                	display.right();
                }
			}
		});
		
        display.getNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.CREATE_LINK);
			}
		});
        
        display.getExploreLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.EXPLORE_LINK);
			}
		});
        
        display.getSaveButton().addClickHandler(new ClickHandler() {            
            @Override
            public void onClick(ClickEvent event) {
                saveMap();
            }
        });
    }
    
    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
    }
    
    public void saveMap() {
    	if (oid == null) {
	    	mapService.call(new RemoteCallback<String>() {
	            @Override
	            public void callback(String response) {
	                oid = response;
	                display.info(Text.LANG.mapCreated(oid));
	            }
	        }, new ErrorCallback() {
				@Override
				public boolean error(Message message, Throwable throwable) {
					display.error(message.toString());
					return false;
				}
			}).insertMap(root);
    	} else {
    		mapService.call(new RemoteCallback<Void>() {
	            @Override
	            public void callback(Void response) {
	                display.info(Text.LANG.mapUpdated(oid));
	            }
	        }, new ErrorCallback() {
				@Override
				public boolean error(Message message, Throwable throwable) {
					display.error(message.toString());
					return false;
				}
			}).updateMap(oid, root);
    	}
    }

}
