/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.server.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.bus.server.api.RpcContext;
import org.mindrot.jbcrypt.BCrypt;
import org.teree.server.auth.RequireAuthentication;
import org.teree.server.dao.UserInfoManager;
import org.teree.shared.UserService;
import org.teree.shared.data.AuthType;
import org.teree.shared.data.UserInfo;

@ApplicationScoped
@Service
public class UserServiceImpl implements UserService {

	@Inject
	UserInfoManager _uim;
	
	/**
	 * TODO: Consider some caching because it is commonly used by other services to guarantee that the operation is secured.
	 */
	@Override
	public UserInfo getUserInfo() {
		HttpSession session = RpcContext.getHttpSession();
        String auth = (String)session.getAttribute("auth");
        UserInfo ui = null;
        if (auth != null) {
	        switch(AuthType.valueOf(auth)) {
		        case Database: {
		            String username = (String)session.getAttribute("username");
		    		ui = _uim.select(username);
		    		break;
		        }
		        case OAuth: {
		    		String googleid = (String)session.getAttribute("googleid");
		    		ui = _uim.selectByGoogleId(googleid);
		    		break;
		        }
	        }
        }
		return ui;
	}
	
	@Override
	public UserInfo getUserInfo(String userid) {
		return _uim.selectByOid(userid);
	}

	/**
	 * TODO: should throw some exceptions (user exists...)
	 */
	@Override
	public boolean register(UserInfo ui, String password) {
		return _uim.insert(ui, password);
	}

	/**
	 * TODO: should throw some exceptions (user exists...)
	 */
	@Override
	public void registerWithGoogle(UserInfo ui, String googleid) {
		_uim.insertWithGoogleId(ui, googleid);
	}

	@RequireAuthentication
	@Override
	public void update(UserInfo ui) {
		_uim.update(ui);
	}

	@RequireAuthentication
	@Override
	public void updatePassword(String oldPassword, String newPassword) {
		UserInfo ui = getUserInfo();
		if (BCrypt.checkpw(oldPassword, _uim.getHashedPassword(ui.getUsername()))) {
			_uim.updatePassword(ui, newPassword);
		}
	}

}
