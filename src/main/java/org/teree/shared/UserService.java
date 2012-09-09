package org.teree.shared;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.UserInfo;

@Remote
public interface UserService {

	public UserInfo getCurrentUserInfo();
	
	public void register(UserInfo ui, String password);
	
}
