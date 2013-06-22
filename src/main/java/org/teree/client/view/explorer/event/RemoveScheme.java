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
package org.teree.client.view.explorer.event;

import org.teree.shared.data.common.Scheme;
import com.google.gwt.event.shared.GwtEvent;

public class RemoveScheme extends GwtEvent<RemoveSchemeHandler> {
    
    public static Type<RemoveSchemeHandler> TYPE = new Type<RemoveSchemeHandler>();

    private Scheme s;
    
    public RemoveScheme(Scheme s) {
        this.s = s;
    }
    
    public Scheme getScheme() {
    	return s;
    }
    
    @Override
    public Type<RemoveSchemeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RemoveSchemeHandler handler) {
        handler.remove(this);
    }
}
