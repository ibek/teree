package org.teree.client;

import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class Teree {

    @Inject
    private TereeController tc;

    @AfterInitialization
    public void startApp() {
        tc.go(RootPanel.get());
    }

    /**@Produces
    @Named(value="msgBus")
    private MessageBus produceMessageBus() {
        return msgBus;
    }*/

}
