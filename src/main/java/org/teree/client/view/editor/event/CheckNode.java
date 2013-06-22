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
package org.teree.client.view.editor.event;

import com.google.gwt.event.shared.GwtEvent;

public class CheckNode extends GwtEvent<CheckNodeHandler> {
    
    public static Type<CheckNodeHandler> TYPE = new Type<CheckNodeHandler>();
    
    public CheckNode() {
    	
    }
    
    @Override
    public Type<CheckNodeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CheckNodeHandler handler) {
        handler.check(this);
    }
    
}
