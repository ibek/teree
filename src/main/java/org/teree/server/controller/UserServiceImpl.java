package org.teree.server.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.MessageCallback;
import org.jboss.errai.bus.client.api.QueueSession;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.bus.server.util.SessionContext;
import org.jboss.errai.common.client.protocols.MessageParts;
import org.jboss.errai.common.client.protocols.Resources;
import org.teree.server.dao.UserInfoManager;
import org.teree.shared.UserService;
import org.teree.shared.data.UserInfo;

@ApplicationScoped
@Service
public class UserServiceImpl implements UserService {

	@Inject
	UserInfoManager _uim;

	@Override
	public void register(UserInfo ui, String password) {
		_uim.insert(ui, password);
	}

}
