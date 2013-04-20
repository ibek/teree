package org.teree.shared.data.common;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class PercentText {

    private String text;
    private double percentage = 50;
    private int group = -1;
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
	
	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return text;
	}
    
}
