package org.teree.controller;

import javax.inject.Inject;

import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.MessageCallback;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.bus.server.annotations.Service;
import org.teree.shared.data.MapChange;

@Service
public class CooperationService implements MessageCallback{

    @Inject
    private MessageBus _bus;
    
    @Override
    public void callback(Message message) {
        System.out.println("tady");
        System.out.println(message.getSubject());
        MapChange change = message.get(MapChange.class, "coop-change");
        System.out.println("change:"+change);
        MessageBuilder.createMessage()
        .toSubject(change.getOid())
        .signalling()
        .with("coop-change", change)
        .noErrorHandling()
        .sendNowWith(_bus);
    }

}
