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
package org.teree.shared;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.UserInfo;

@Remote
public interface UserService {
	
	public UserInfo getUserInfo();
	
	public UserInfo getUserInfo(String userid);
	
	public boolean register(UserInfo ui, String password);
	
	public void registerWithGoogle(UserInfo ui, String googleid);
	
	public void update(UserInfo ui);
	
	public void updatePassword(String oldPassword, String newPassword);
	
}
