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

public interface DialogStyle extends ClientBundle {
	
	public final static DialogStyle INSTANCE = GWT.create(DialogStyle.class);
	
	@Source("dialogStyle.css")
	DialogStyleCssResource css();
	
	public interface DialogStyleCssResource extends CssResource {
        String dialog();
    }
	
}
