package org.teree.shared.data.common;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.teree.shared.data.UserInfo;

@Portable
public class NodeCategory {
	
	public static final boolean DEFAULT_BOLD = false;
	public static final String DEFAULT_COLOR = "#000000";
	public static final String DEFAULT_BACKGROUND = "#ffffff";
	public static final int MAX_TRANSPARENCY = 100;

	private String oid;
	private String name;
    private String iconType;
    private String color = DEFAULT_COLOR;
    private String background = DEFAULT_BACKGROUND;
    private int transparency = MAX_TRANSPARENCY; // 0 - 100%
	private boolean bold = DEFAULT_BOLD;
	
	private UserInfo owner;
	
	public NodeCategory() {
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof NodeCategory)) {
			return false;
		}
		NodeCategory other = (NodeCategory) obj;
		if (oid == null) {
			if (other.oid != null) {
				return false;
			}
		} else if (!oid.equals(other.oid)) {
			return false;
		}
		return true;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(Boolean bold) {
		if (bold != null) {
			this.bold = bold;
		}
	}

	public UserInfo getOwner() {
		return owner;
	}

	public void setOwner(UserInfo owner) {
		this.owner = owner;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if (color != null && !color.isEmpty()) {
			this.color = color;
		}
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		if (background != null && !background.isEmpty()) {
			this.background = background;
		}
	}

	public int getTransparency() {
		return transparency;
	}

	public void setTransparency(Integer transparency) {
		if (transparency != null && transparency >= 0 && transparency <= MAX_TRANSPARENCY) {
			this.transparency = transparency;
		}
	}

	public void set(NodeCategory category) {
		if (category == null) {
			return;
		}
		this.oid = category.getOid();
		this.owner = category.getOwner();
		this.name = category.getName();
		this.bold = category.isBold();
		this.iconType = category.getIconType();
		this.transparency = category.getTransparency();
		this.color = category.getColor();
		this.background = category.getBackground();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
