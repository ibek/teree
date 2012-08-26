package org.teree.client.view.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface ViewStyle extends ClientBundle {
	
	public final static ViewStyle INSTANCE = GWT.create(ViewStyle.class);
	
	@Source("viewStyle.css")
	ViewStyleCssResource css();

	
	public interface ViewStyleCssResource extends CssResource {
	    String header();
	    String middle_header();
	    String logo();
	    String middle_header_element();
	    String middle_header_link();
	    String header_login();
	    String header_login_user();
	    String status();
	}
	
}
