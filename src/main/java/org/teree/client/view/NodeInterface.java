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
package org.teree.client.view;

import org.teree.shared.data.common.Node;

import com.google.gwt.canvas.dom.client.Context2d;

public interface NodeInterface {

	public Node getNode();
	
	public int getWidgetWidth();
	
	public int getWidgetHeight();
	
	public void draw(Context2d context, int x, int y);
	
	public void update();
	
	public void setCollapsed(boolean collapsed);
	
	public boolean isCollapsed();
	
	public void edit();
	
	public void select();
	
	public void unselect();
	
}
