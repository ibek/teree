package org.teree.client;

import org.teree.shared.data.UserInfo;

public class CurrentUser extends UserInfo {

	private String sessionId;
	
	public void clear() {
		setUsername(null);
		setName(null);
		setSessionId(null);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
