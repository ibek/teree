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
package org.teree.shared.data.common;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.teree.shared.data.UserInfo;

@Portable
public class UserPermissions {

	private UserInfo user;
	private Boolean write;
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UserPermissions)) {
			return false;
		}
		UserPermissions up = (UserPermissions)obj;
		if (write != up.write || !user.equals(up.user)) {
			return false;
		}
		return true;
	}
	
	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public Boolean getWrite() {
		return write;
	}
	
	public void setWrite(Boolean write) {
		this.write = write;
	}
	
}
