/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
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
    
}
