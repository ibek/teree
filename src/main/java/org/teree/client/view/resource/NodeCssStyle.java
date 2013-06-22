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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface NodeCssStyle extends ClientBundle {
	
	public final static NodeCssStyle INSTANCE = GWT.create(NodeCssStyle.class);
	
	@Source("nodeStyle.css")
	NodeStyleCssResource css();

	
	public interface NodeStyleCssResource extends CssResource {
		
	    String node();

	    @ClassName("view")
	    String nodeView();
	    
	    @ClassName("edit")
	    String nodeEdit();
	}
	
}
