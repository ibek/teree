package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.Text;
import org.teree.client.event.GlobalKeyUp;
import org.teree.client.event.GlobalKeyUpHandler;
import org.teree.client.event.SchemeReceived;
import org.teree.client.event.SchemeReceivedHandler;
import org.teree.client.view.KeyAction;
import org.teree.shared.SecuredService;
import org.teree.shared.data.Scheme;
import org.teree.shared.data.Node;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class SchemeEditor implements Presenter {

    public interface Display extends KeyAction, Template {
        HasClickHandlers getSaveButton();
        Widget asWidget();
        void setRoot(Node root);
        void info(String msg);
        void error(String msg);
        String getSchemeSamplePicture();
    }
    
    @Inject @Named(value="eventBus")
    private HandlerManager eventBus;
    
	@Inject
	private Caller<SecuredService> securedService;
    
    @Inject
    private Display display;
    
    private Scheme scheme;
    
    public void bind() {
    	eventBus.addHandler(SchemeReceived.TYPE, new SchemeReceivedHandler() {
			@Override
			public void received(SchemeReceived event) {
				scheme = event.getScheme();
				display.setRoot(scheme.getRoot());
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
				else if (key == 66 && e.getCtrlKey())
				{
                	display.bold();
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
        
        display.getSaveButton().addClickHandler(new ClickHandler() {            
            @Override
            public void onClick(ClickEvent event) {
                scheme.setSchemePicture(display.getSchemeSamplePicture());
                saveScheme();
            }
        });
    }
    
    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
    }

	@Override
	public Template getTemplate() {
		return display;
	}
    
    public void saveScheme() {
    	if (scheme.getOid() == null) {
    		securedService.call(new RemoteCallback<String>() {
	            @Override
	            public void callback(String response) {
	                scheme.setOid(response);
	                display.info(Text.LANG.schemeCreated(scheme.getOid()));
	            }
	        }, new ErrorCallback() {
				@Override
				public boolean error(Message message, Throwable throwable) {
					display.error(message.toString());
					return false;
				}
			}).insertScheme(scheme);
    	} else {
    		securedService.call(new RemoteCallback<Void>() {
	            @Override
	            public void callback(Void response) {
	                display.info(Text.LANG.schemeUpdated(scheme.getOid()));
	            }
	        }, new ErrorCallback() {
				@Override
				public boolean error(Message message, Throwable throwable) {
					display.error(message.toString());
					return false;
				}
			}).updateScheme(scheme);
    	}
    }

}
