package org.teree.shared.data.common;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class NodeCategory {
	
	public static final boolean DEFAULT_BOLD = false;
	public static final String DEFAULT_COLOR = "000000";
	public static final String DEFAULT_BACKGROUND = "FFFFFF";

	private String name;
    private String iconType;
    private String color = DEFAULT_COLOR;
    private String background = DEFAULT_BACKGROUND;
	private boolean bold = DEFAULT_BOLD;
	
	public NodeCategory() {
		
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
	
}
