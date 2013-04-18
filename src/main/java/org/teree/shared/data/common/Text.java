package org.teree.shared.data.common;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Text {

    private String text;
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
	
	@Override
	public String toString() {
		return text;
	}
    
}
