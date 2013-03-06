package org.teree.shared.data.common;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * Style of node in specific viewpoint.
 */
@Portable
public class NodeStyle implements Cloneable {

	public static final boolean DEFAULT_VISIBLE = true;
	public static final boolean DEFAULT_BOLD = false;
	public static final boolean DEFAULT_CASCADING = false;
	public static final boolean DEFAULT_COLLAPSED = false;
	public static final String DEFAULT_COLOR = "000000";
	public static final String DEFAULT_BACKGROUND = "FFFFFF";

	private boolean visible = DEFAULT_VISIBLE;
	private boolean bold = DEFAULT_BOLD;
	private boolean cascading = DEFAULT_CASCADING;
	private boolean collapsed = DEFAULT_COLLAPSED;

	public NodeStyle() {

	}

	public NodeStyle clone() {
		NodeStyle ns = new NodeStyle();
		ns.setBold(bold);
		return ns;
	}

	public boolean isDefault() {
		boolean def = true;
		def = def && visible == DEFAULT_VISIBLE;
		def = def && bold == DEFAULT_BOLD;
		def = def && cascading == DEFAULT_CASCADING;
		def = def && collapsed == DEFAULT_COLLAPSED;
		return def;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isCascading() {
		return cascading;
	}

	public void setCascading(boolean cascading) {
		this.cascading = cascading;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

}
