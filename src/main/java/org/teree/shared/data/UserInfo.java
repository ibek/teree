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
package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class UserInfo {
	
	public static final String PART = "UserInfo";
	
	private String userId;
	private String username;
	private String name;
	private String email;
	
	private String joined;

	private long memUsed = 0;
	
	private UserPackage userPackage;
	
	public void clear() {
		setUsername(null);
		setName(null);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UserInfo)) {
			return false;
		}
		UserInfo ui = (UserInfo)obj;
		if (!userId.equals(ui.userId)) {
			return false;
		}
		return true;
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
		if (memUsed < 0) {
			memUsed = 0L;
		}
		this.memUsed = memUsed;
	}

	public UserPackage getUserPackage() {
		return userPackage;
	}

	public void setUserPackage(UserPackage userPackage) {
		this.userPackage = userPackage;
	}

	public String getJoined() {
		return joined;
	}

	public void setJoined(String joined) {
		this.joined = joined;
	}

	public void set(UserInfo ui) {
		if (ui != null) {
			userId = ui.userId;
			username = ui.username;
			name = ui.name;
			email = ui.email;
			joined = ui.joined;
			memUsed = ui.memUsed;
			userPackage = ui.userPackage;
		}
	}
	
}
