package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Map {

	private String oid;
	
	private Node.NodeType type;
	
	private Object rootContent;

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

	public Object getRootContent() {
		return rootContent;
	}

	public void setRootContent(Object rootContent) {
		this.rootContent = rootContent;
	}
	
}
