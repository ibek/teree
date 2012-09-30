package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class UserPackage {

	private String name;
	
	private Long memLimit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getMemLimit() {
		return memLimit;
	}

	public void setMemLimit(Long memLimit) {
		this.memLimit = memLimit;
	}
	
}
