package org.teree.server.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.MessageCallback;
import org.jboss.errai.bus.client.api.QueueSession;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.common.client.protocols.Resources;
import org.teree.server.dao.UserInfoManager;
import org.teree.shared.data.UserInfo;

@ApplicationScoped
@Service
public class UserInfoProvider implements MessageCallback {

	@Inject
	UserInfoManager _uim;
	
	@Inject
	private MessageBus _bus;
	
	@Override
	public void callback(Message message) {
		QueueSession session = message.getResource(QueueSession.class, Resources.Session.name());
		String username = session.getAttribute(String.class, "username");
		String password = session.getAttribute(String.class, "password");
		UserInfo ui = _uim.select(username, password);
		MessageBuilder.createMessage()
	        .toSubject("UserInfoReceiver").signalling()
	        .with(UserInfo.PART, ui).noErrorHandling()
	        .sendNowWith(_bus);
	}

}
