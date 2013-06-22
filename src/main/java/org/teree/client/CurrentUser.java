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
