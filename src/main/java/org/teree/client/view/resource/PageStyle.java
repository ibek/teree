package org.teree.client.view.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface PageStyle extends ClientBundle {
	
	public final static PageStyle INSTANCE = GWT.create(PageStyle.class);
	
	@Source("pageStyle.css")
	PageStyleCssResource css();

	
	public interface PageStyleCssResource extends CssResource {
	    String scene();
	    String status();
	}
	
}
