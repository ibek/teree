package org.teree.shared.data.scheme;

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * Global permissions and the permissions for users.
 * 
 * @author ibek
 */
@Portable
public class Permissions {

	private Boolean write; // global permissions, null means that the scheme is not globally readable
	private List<UserPermissions> users;
	// TODO: create GroupPermissions
	
	public Boolean getWrite() {
		return write;
	}
	
	public void setWrite(Boolean write) {
		this.write = write;
	}
	
	public List<UserPermissions> getUsers() {
		return users;
	}
	
	public void setUsers(List<UserPermissions> users) {
		this.users = users;
	} 
	
}
