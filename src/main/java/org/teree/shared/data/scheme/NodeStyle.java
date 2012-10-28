package org.teree.shared.data.scheme;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class NodeStyle implements Cloneable {
	
	public static final boolean DEFAULT_BOLD = false;

	private boolean bold = DEFAULT_BOLD;
	
	public NodeStyle() {
		
	}
	
	@Override
	public NodeStyle clone() {
		NodeStyle ns = new NodeStyle();
		ns.setBold(bold);
		return ns;
	}
	
	public boolean isDefault() {
		boolean def = true;
		def = def && bold == DEFAULT_BOLD;
		return def;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
}
