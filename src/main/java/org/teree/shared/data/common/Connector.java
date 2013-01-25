package org.teree.shared.data.common;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Connector {

    private IconText root;
    private String oid;
    
	public IconText getRoot() {
		return root;
	}
	
	public void setRoot(IconText root) {
		this.root = root;
	}
	
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}

	@Override
	public String toString() {
		return (root != null)?root.getText():"";
	}
    
}
