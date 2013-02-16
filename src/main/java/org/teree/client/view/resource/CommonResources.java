package org.teree.client.view.resource;

import com.github.gwtbootstrap.client.ui.resources.Resources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

public interface CommonResources extends Resources {
	
	public static final CommonResources RESOURCES = GWT.create(CommonResources.class);

	@Source("img/features.png")
    ImageResource features();
	
	@Source("img/email.png")
    ImageResource email();
	
	@Source("img/texticon.png")
    ImageResource texticon();
	
	@Source("img/image.png")
    ImageResource image();
	
	@Source("img/link.png")
    ImageResource link();
	
	@Source("img/mathexpr.png")
    ImageResource mathexpr();
    
}
