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
	
	@Override
	public String toString() {
		return "Scheme title";
	}
	
}
