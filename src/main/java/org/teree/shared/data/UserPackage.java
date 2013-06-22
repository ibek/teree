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
public class UserPackage {

	private String name;
	
	private String memLimit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemLimit() {
		return memLimit;
	}

	public void setMemLimit(String memLimit) {
		this.memLimit = memLimit;
	}
	
}
