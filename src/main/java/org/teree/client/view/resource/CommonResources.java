package org.teree.client.view.resource;

import com.github.gwtbootstrap.client.ui.resources.Resources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

public interface CommonResources extends Resources {
	
	public static final CommonResources RESOURCES = GWT.create(CommonResources.class);
	
    ImageResource email();
    
}
