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
package org.teree.client.view.viewer;

import org.teree.client.view.NodeInterface;
import org.teree.client.view.common.NodeCategoryStyle;
import org.teree.client.view.resource.NodeCssStyle;
import org.teree.shared.data.common.Node;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public abstract class NodeWidget extends Composite implements NodeInterface {
    
    protected NodeCssStyle resources = NodeCssStyle.INSTANCE;
    
    protected Node node;
    
    protected AbsolutePanel container;
    
    protected boolean collapsed = false;
    
    public NodeWidget() {
    	
    }
    
    public NodeWidget(Node node) {
        this.node = node;
        container = new AbsolutePanel();
        resources.css().ensureInjected();
        
        initWidget(container);
    }
    
    @Override
	public void update() {
    	NodeCategoryStyle.set(container, node.getCategory());
    }

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public int getWidgetWidth() {
		return getWidget().getOffsetWidth();
	}

	@Override
	public int getWidgetHeight() {
		return getWidget().getOffsetHeight();
	}
	
	@Override
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
	
	@Override
	public boolean isCollapsed() {
		return collapsed;
	}

	@Override
	public void edit() {
		// not needed
	}

	@Override
	public void select() {
		// TODO Auto-generated method stub
	}

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
	}

}
