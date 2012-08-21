package org.teree.client;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class Teree {
    
    //private MessageBus msgBus = ErraiBus.get();
    
    private static HandlerManager eventBus = new HandlerManager(null);

    @Inject
    private TereeController tc;

    @AfterInitialization
    public void startApp() {
        tc.go(RootPanel.get());
    }

    @Produces
    private HandlerManager produceEventBus() {
        return eventBus;
    }
    
    public static HandlerManager getHandlerManager() {
    	return eventBus;
    }

    /**@Produces
    public MessageBus produceMessageBus() {
        return msgBus;
    }*/

}
