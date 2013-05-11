package org.teree.client.view.resource;

import com.github.gwtbootstrap.client.ui.resources.Resources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

public interface HelpResources extends Resources {
	
	public static final HelpResources RESOURCES = GWT.create(HelpResources.class);

	@Source("img/features.png")
    ImageResource features();
	
	@Source("img/email.png")
    ImageResource email();
	
	@Source("img/text.png")
    ImageResource text();
	
	@Source("img/image.png")
    ImageResource image();
	
	@Source("img/link.png")
    ImageResource link();
	
	@Source("img/mathexpr.png")
    ImageResource mathexpr();
	
	@Source("img/connector.png")
    ImageResource connector();
	
	@Source("img/connectorDialog.png")
    ImageResource connectorDialog();
	
	@Source("img/connectorView.png")
    ImageResource connectorView();
	
	@Source("img/percentageForProject.png")
    ImageResource percentageForProject();
	
	@Source("img/percentageDistribution.png")
    ImageResource percentageDistribution();
    
}
