package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Scheme {

	private String oid;
	private Node.NodeType type;
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

	public Node.NodeType getType() {
		return type;
	}

	public void setType(Node.NodeType type) {
		this.type = type;
	}
	
}
