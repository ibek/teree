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
package org.teree.client.view.viewer.event;

import org.teree.client.view.viewer.NodeWidget;
import com.google.gwt.event.shared.GwtEvent;

public class CollapseNode extends GwtEvent<CollapseNodeHandler> {
    
    public static Type<CollapseNodeHandler> TYPE = new Type<CollapseNodeHandler>();

    private NodeWidget node;
    
    public CollapseNode(NodeWidget node) {
        this.node = node;
    }
    
    public NodeWidget getNode() {
    	return node;
    }
    
    @Override
    public Type<CollapseNodeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CollapseNodeHandler handler) {
        handler.collapse(this);
    }
    
}
