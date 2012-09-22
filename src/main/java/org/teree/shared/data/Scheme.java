package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * 
 * TODO: add details for scheme - (author, owner), created, lastEdit 
 *
 */
@Portable
public class Scheme {

	private String oid;
	private String schemePicture;
	private Node root;

	public String getSchemePicture() {
        return schemePicture;
    }

    public void setSchemePicture(String schemePicture) {
        this.schemePicture = schemePicture;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}
	
}
