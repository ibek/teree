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
