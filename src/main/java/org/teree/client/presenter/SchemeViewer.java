package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.event.SchemeReceived;
import org.teree.client.event.SchemeReceivedHandler;
import org.teree.client.text.UIMessages;
import org.teree.shared.SchemeService;
import org.teree.shared.data.scheme.Scheme;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class SchemeViewer implements Presenter {

    public interface Display extends Template {
        Widget asWidget();
        void setScheme(Scheme scheme);
        Scheme getScheme();
        void sendDownloadRequest(String name, String type, String data);
        HasClickHandlers getExportJSONButton();
    }
    
    @Inject @Named(value="eventBus")
    private HandlerManager eventBus;
	
	@Inject
	private Caller<SchemeService> generalService;
    
    @Inject
    private Display display;
    
    public void bind() {
    	
        eventBus.addHandler(SchemeReceived.TYPE, new SchemeReceivedHandler() {
			@Override
			public void received(SchemeReceived event) {
				display.setScheme(event.getScheme());
				display.info(UIMessages.LANG.schemeReceived(event.getScheme().getOid()));
			}
		});
        
        display.getExportJSONButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				exportJSON(display.getScheme().getOid());
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
	
	private void exportJSON(String oid) {
		generalService.call(new RemoteCallback<String>() {
            @Override
            public void callback(String response) {
            	if (response == null) {
            		display.error(UIMessages.LANG.cannot_export_scheme());
            	} else {
            		display.sendDownloadRequest(display.getScheme().getRoot().getContent().toString(), "json", response);
            	}
            }
        }, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				display.error(UIMessages.LANG.connectionIssue());
				return false;
			}
		}).exportJSON(oid);
	}

}
