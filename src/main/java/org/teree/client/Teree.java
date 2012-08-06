package org.teree.client;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class Teree {
    
    private MessageBus msgBus = ErraiBus.get();
    
    private HandlerManager eventBus = new HandlerManager(null);

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

    @Produces
    private MessageBus produceMessageBus() {
        return msgBus;
    }

}
