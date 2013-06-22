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

/**
 * 
 * TODO: add details for scheme - created, lastEdit 
 *
 */
@Portable
public class Scheme {

	private String oid;
	private String schemePicture;
	private StructureType structure;

	private UserInfo author;
	private Permissions permissions = new Permissions();

	public String getSchemePicture() {
        return schemePicture;
    }

    public void setSchemePicture(String schemePicture) {
        this.schemePicture = schemePicture;
    }

    public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public StructureType getStructure() {
		return structure;
	}

	public void setStructure(StructureType structure) {
		this.structure = structure;
	}

	public UserInfo getAuthor() {
		return author;
	}

	public void setAuthor(UserInfo author) {
		this.author = author;
	}

	public Permissions getPermissions() {
		return permissions;
	}

	public void setPermissions(Permissions permissions) {
		this.permissions = permissions;
	}
	
	public Node getFirst() {
		return null;
	}

	@Override
	public String toString() {
		return "Scheme title";
	}
	
}
