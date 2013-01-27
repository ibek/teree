package org.teree.client.view.editor;

import org.teree.client.view.NodeInterface;
import org.teree.client.view.common.NodeWidgetFactory;
import org.teree.client.view.resource.MathExpressionTools;
import org.teree.shared.data.common.Node;

import com.google.gwt.user.client.ui.Widget;

public class EditorNodeWidgetFactory<T extends Widget & NodeInterface> implements NodeWidgetFactory<T> {

	private boolean initMathScript = true;
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
	        	if (initMathScript) {
	        		initMathScript = false;
	        		MathExpressionTools.initScript();
	        	}
	        	nw = new MathExpressionNodeWidget(node);
    			break;
	        }
	        case Connector:{
	        	nw = new ConnectorNodeWidget(node);
    			break;
	        }
	    }
    	
    	return (T)nw;
    }
	
}
