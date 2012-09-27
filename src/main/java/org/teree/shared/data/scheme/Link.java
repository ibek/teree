package org.teree.shared.data.scheme;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Link {

	private String text;
	
	private String url;
	
	public Link() {
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
