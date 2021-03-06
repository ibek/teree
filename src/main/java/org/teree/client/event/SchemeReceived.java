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

import org.teree.shared.data.common.Scheme;

import com.google.gwt.event.shared.GwtEvent;

public class SchemeReceived extends GwtEvent<SchemeReceivedHandler> {
    
    public static Type<SchemeReceivedHandler> TYPE = new Type<SchemeReceivedHandler>();

    private Scheme s;
    
    public SchemeReceived(Scheme s) {
        this.s = s;
    }
    
    public Scheme getScheme() {
        return s;
    }

    @Override
    public Type<SchemeReceivedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SchemeReceivedHandler handler) {
        handler.received(this);
    }
}
