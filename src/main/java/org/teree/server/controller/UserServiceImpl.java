package org.teree.server.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.server.dao.UserInfoManager;
import org.teree.shared.UserService;
import org.teree.shared.data.UserInfo;

@ApplicationScoped
@Service
public class UserServiceImpl implements UserService {

	@Inject
	UserInfoManager _uim;
	
	@Override
	public UserInfo getCurrentUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(UserInfo ui, String password) {
		_uim.insert(ui, password);
	}

}
