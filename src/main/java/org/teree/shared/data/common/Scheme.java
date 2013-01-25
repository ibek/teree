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
	private String title; // TODO: add title to db
	private String schemePicture;
	private StructureType structure; // TODO: add structure to db

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	
}
