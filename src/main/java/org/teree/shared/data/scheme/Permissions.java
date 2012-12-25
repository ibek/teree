package org.teree.shared.data.scheme;

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.teree.shared.data.UserInfo;

/**
 * Global permissions and the permissions for users.
 * 
 * @author ibek
 */
@Portable
public class Permissions {

	private Boolean write = null; // global permissions, null means that the scheme is not globally readable
	private List<UserPermissions> users;
	// TODO: create GroupPermissions
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Permissions)) {
			return false;
		}
		Permissions p = (Permissions)obj;
		if (write != p.write || !users.equals(p.users)) {
			return false;
		}
		return true;
	}
	
	public boolean canEdit(UserInfo ui) {
		if (write != null && write) {
			return true;
		}
		if (users != null) {
			for (UserPermissions up : users) {
				if (up.getUser().getUserId().equals(ui.getUserId()) && up.getWrite()) {
					return true;
				}
			}
		}
		return false;
	}
	
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
