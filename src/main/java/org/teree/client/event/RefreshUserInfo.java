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

public class RefreshUserInfo extends GwtEvent<RefreshUserInfoHandler> {
    
    public static Type<RefreshUserInfoHandler> TYPE = new Type<RefreshUserInfoHandler>();
    
    public RefreshUserInfo() {

    }

    @Override
    public Type<RefreshUserInfoHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RefreshUserInfoHandler handler) {
        handler.refresh(this);
    }
}
