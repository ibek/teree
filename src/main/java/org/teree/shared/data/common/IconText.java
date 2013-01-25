package org.teree.shared.data.common;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class IconText {

    private String text;
    private String iconType;
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}
	
	@Override
	public String toString() {
		return text;
	}
    
}
