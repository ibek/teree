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

public class SelectNode<T> extends GwtEvent<SelectNodeHandler> {
    
    public static Type<SelectNodeHandler> TYPE = new Type<SelectNodeHandler>();

    private T node;
    
    public SelectNode(T node) {
        this.node = node;
    }
    
    public T getNodeWidget() {
    	return node;
    }
    
    @Override
    public Type<SelectNodeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SelectNodeHandler handler) {
        handler.select(this);
    }
}
