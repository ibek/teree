package org.teree.server.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.bus.server.api.RpcContext;
import org.teree.server.dao.UserInfoManager;
import org.teree.shared.UserService;
import org.teree.shared.data.AuthType;
import org.teree.shared.data.UserInfo;

@ApplicationScoped
@Service
public class UserServiceImpl implements UserService {

	@Inject
	UserInfoManager _uim;
	
	@Override
	public UserInfo getUserInfo() {
		HttpSession session = RpcContext.getHttpSession();
        String auth = (String)session.getAttribute("auth");
        UserInfo ui = null;
        if (auth != null) {
	        switch(AuthType.valueOf(auth)) {
		        case Database: {
		            String username = (String)session.getAttribute("username");
		    		String password = (String)session.getAttribute("password");
		    		ui = _uim.select(username, password);
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
	public void register(UserInfo ui, String password) {
		_uim.insert(ui, password);
	}

}
