package org.teree.shared;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.UserInfo;

@Remote
public interface UserService {
	
	public UserInfo getUserInfo();
	
	public boolean register(UserInfo ui, String password);
	
	public void registerWithGoogle(UserInfo ui, String googleid);
	
	public void update(UserInfo ui);
	
	public void updatePassword(String oldPassword, String newPassword);
	
}
