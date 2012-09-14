package org.teree.shared;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.UserInfo;

@Remote
public interface UserService {
	
	public UserInfo getUserInfo();
	
	public void register(UserInfo ui, String password);
	
}
