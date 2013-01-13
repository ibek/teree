package org.teree.client.presenter;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.event.GlobalKeyUp;
import org.teree.client.event.GlobalKeyUpHandler;
import org.teree.client.event.RefreshUserInfo;
import org.teree.client.event.SchemeReceived;
import org.teree.client.event.SchemeReceivedHandler;
import org.teree.client.text.UIMessages;
import org.teree.client.view.KeyAction;
import org.teree.shared.SchemeService;
import org.teree.shared.SecuredSchemeService;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.Scheme;

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
        void setScheme(Scheme scheme);
        String getSchemeSamplePicture();
    }
    
    @Inject @Named(value="eventBus")
    private HandlerManager eventBus;
    
	@Inject
	private Caller<SchemeService> generalService;
    
	@Inject
	private Caller<SecuredSchemeService> securedScheme;
    
    @Inject
    private Display display;
    
    private Scheme scheme;
    
    public void bind() {
    	eventBus.addHandler(SchemeReceived.TYPE, new SchemeReceivedHandler() {
			@Override
			public void received(SchemeReceived event) {
				scheme = event.getScheme();
				display.setScheme(scheme);
			}
		});
    	
    	eventBus.addHandler(GlobalKeyUp.TYPE, new GlobalKeyUpHandler() {
			@Override
			public void onKeyUp(GlobalKeyUp event) {
				Event e = event.getEvent();
				int key = e.getKeyCode();
				
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
                	//display.copy(); // TODO: create buttons for copy, cut and paste
                }
				else if (key == 88 && e.getCtrlKey())
				{
                	//display.cut();
                }
				else if (key == 86 && e.getCtrlKey())
				{
                	//display.paste();
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
                saveScheme(scheme);
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
    
    public void saveScheme(final Scheme scheme) {
    	if (scheme.getOid() == null) {
    		insertScheme(scheme, new RemoteCallback<String>() {
                @Override
                public void callback(String response) {
                    scheme.setOid(response);
                    display.info(UIMessages.LANG.schemeCreated(scheme.getOid()));
                }
            });
    	} else {
    		securedScheme.call(new RemoteCallback<Void>() {
	            @Override
	            public void callback(Void response) {
	                display.info(UIMessages.LANG.schemeUpdated(scheme.getOid()));
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
    
    public void insertScheme(Scheme scheme, RemoteCallback<String> callback) {
    	securedScheme.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				display.error(message.toString());
				return false;
			}
		}).insertScheme(scheme);
    }
    
    public void getScheme(String oid, RemoteCallback<Scheme> callback) {
    	generalService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				display.error(message.toString());
				return false;
			}
		}).getScheme(oid);
    }
    
    public void searchFrom(String fromOid, String text, RemoteCallback<List<Scheme>> callback) {
    	generalService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				display.error(message.toString());
				return false;
			}
		}).searchFrom(fromOid, text, 5);
    }

	@Override
	public String getTitle() {
		return "Editor";
	}

}
