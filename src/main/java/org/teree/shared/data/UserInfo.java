package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class UserInfo {
	
	public static final String PART = "UserInfo";
	
	private String username;
	
	private String name;

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
	
}
