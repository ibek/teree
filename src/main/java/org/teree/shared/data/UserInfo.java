package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class UserInfo {
	
	public static final String PART = "UserInfo";
	
	private String userId;
	private String username;
	private String name;
	private String email;

	private long memUsed = 0;
	private long memLimit = 0;
	
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
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getMemUsed() {
		return memUsed;
	}

	public void setMemUsed(long memUsed) {
		this.memUsed = memUsed;
	}

	public long getMemLimit() {
		return memLimit;
	}

	public void setMemLimit(long memLimit) {
		this.memLimit = memLimit;
	}

	public void set(UserInfo ui) {
		if (ui != null) {
			username = ui.username;
			name = ui.name;
		}
	}
	
}
