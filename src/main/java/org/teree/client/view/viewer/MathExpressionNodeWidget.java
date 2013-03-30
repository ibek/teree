package org.teree.client.view.viewer;

import org.teree.client.view.common.NodePainter;
import org.teree.client.view.resource.MathExpressionTools;
import org.teree.shared.data.common.Node;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;

public class MathExpressionNodeWidget extends NodeWidget {

    private HTML content;
    
    protected MathExpressionNodeWidget(Node node) {
        super(node);
        init();
        update();
    }
    
    public void init() {
        content = new HTML();
        
		content.getElement().setInnerHTML(SafeHtmlUtils.htmlEscape("\\["+node.getContent()+"\\]"));
		MathExpressionTools.renderLatexResult(content.getElement());
		
        content.setStylePrimaryName(resources.css().node());
    	
    	content.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.stopPropagation(); // it prevents map moving
			}
		});
        
        container.add(content);
    }

    @Override
    public void draw(Context2d context, int x, int y) {
        NodeList<Element> nl = content.getElement().getElementsByTagName("div");
        if (nl.getLength() > 0) {
        	Element e = nl.getItem(0);
			NodePainter.drawSingleLine(context, x, y, e.getInnerText(),
					getOffsetWidth(), getOffsetHeight(), node.getCategory());
        }
    }

}
