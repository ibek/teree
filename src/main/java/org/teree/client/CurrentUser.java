package org.teree.client;

import org.teree.shared.data.UserInfo;

public class CurrentUser {

	private static CurrentUser instance;
	
	private UserInfo ui;
	
	private CurrentUser() {
		
	}
	
	public static CurrentUser getInstance() {
		if (instance == null) {
			instance = new CurrentUser();
		}
		return instance;
	}
	
	public UserInfo getUserInfo() {
		return ui;
	}
	
	public void setUserInfo(UserInfo ui) {
		this.ui = ui;
	}
	
	public void clear() {
		ui = null;
	}
	
}
