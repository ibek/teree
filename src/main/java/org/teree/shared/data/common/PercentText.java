package org.teree.shared.data.common;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class PercentText {

    private String text;
    private int percentage = 50;
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
	
	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return text;
	}
    
}
