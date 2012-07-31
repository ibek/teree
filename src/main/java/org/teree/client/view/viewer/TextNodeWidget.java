package org.teree.client.view.viewer;

import org.teree.shared.data.Node;
import org.teree.shared.data.NodeStyle;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;

public class TextNodeWidget extends NodeWidget {

    private HTML content;
    
    protected TextNodeWidget(Node node) {
        super(node);
        init();
    }
    
    public void init() {
        content = new HTML(node.getContent().toString());
        content.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO: add support for collapse
            }
        });

        content.setStylePrimaryName(resources.css().node());
        content.setStyleDependentName("view", true);
    	
        // set node style
		NodeStyle ns = node.getStyleOrCreate();
		
		if (ns.isBold()) {
			getElement().getStyle().setFontWeight(FontWeight.BOLD);
		} else {
			getElement().getStyle().setFontWeight(FontWeight.NORMAL);
		}
        
        container.add(content);
    }

    @Override
    public void draw(Context2d context, int x, int y) {
    	context.save();
    	context.setFont("14px monospace");
        context.setFillStyle("#000000");
        context.fillText(content.getText(), x, y);
        context.restore();
    }

}
