package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class UserInfo {
	
	public static final String PART = "UserInfo";
	
	private String userId;
	
	private String username;
	
	private String name;
	
	public void clear() {
		setUsername(null);
		setName(null);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void set(UserInfo ui) {
		if (ui != null) {
			username = ui.username;
			name = ui.name;
		}
	}
	
}
