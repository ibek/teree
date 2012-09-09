package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public enum AuthType {

	Database,
	OAuth;
	
	public static final String PART = "AuthType";
	
}
