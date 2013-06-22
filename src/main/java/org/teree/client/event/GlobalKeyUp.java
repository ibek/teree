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
package org.teree.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Event;

public class GlobalKeyUp extends GwtEvent<GlobalKeyUpHandler> {
    
    public static Type<GlobalKeyUpHandler> TYPE = new Type<GlobalKeyUpHandler>();

    private Event event;
    
    public GlobalKeyUp(Event event) {
        this.event = event;
    }
    
    public Event getEvent() {
    	return event;
    }
    
    @Override
    public Type<GlobalKeyUpHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GlobalKeyUpHandler handler) {
        handler.onKeyUp(this);
    }
}
