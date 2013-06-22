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
package org.teree.client.view.editor;

import org.teree.client.view.NodeInterface;
import org.teree.client.view.common.NodeWidgetFactory;
import org.teree.client.view.resource.MathExpressionTools;
import org.teree.shared.data.common.Node;

import com.google.gwt.user.client.ui.Widget;

public class EditorNodeWidgetFactory<T extends Widget & NodeInterface> implements NodeWidgetFactory<T> {

	private boolean render = false;
	@Override
	public T create(Node node) {
		NodeWidget nw = null;
    	switch(node.getType()){
    		case IconText: {
    			nw = new TextNodeWidget(node);
    			break;
	        }
	        case ImageLink: {
	        	nw = new ImageNodeWidget(node);
    			break;
	        }
	        case Link: {
	        	nw = new LinkNodeWidget(node);
    			break;
	        }
	        case MathExpression: {
	        	render = MathExpressionTools.initScript();
	        	nw = new MathExpressionNodeWidget(node);
    			break;
	        }
	        case Connector:{
	        	nw = new ConnectorNodeWidget(node);
    			break;
	        }
	        case Percent:{
	        	nw = new PercentNodeWidget(node);
    			break;
	        }
	    }
    	
    	return (T)nw;
    }
	
	@Override
	public boolean needsRender() {
		return render;
	}
	
}
