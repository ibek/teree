package org.teree.shared.data.storage;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class ImageInfo {

	private String name;
	
	private String url;

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		name = url.substring(url.lastIndexOf('/')+1);
	}
	
}
