package org.teree.client.view.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface DialogStyle extends ClientBundle {
	
	public final static DialogStyle INSTANCE = GWT.create(DialogStyle.class);
	
	@Source("dialogStyle.css")
	DialogStyleCssResource css();
	
	public interface DialogStyleCssResource extends CssResource {
        String dialog();
    }
	
}
