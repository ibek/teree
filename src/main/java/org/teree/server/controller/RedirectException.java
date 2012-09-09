package org.teree.server.controller;

public class RedirectException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private final String url;

	public RedirectException(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
