package org.teree.client.view.viewer;

import org.teree.shared.data.Node;

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
        
        content.setStyleName(resources.nodeStyle().view());
        container.add(content);
    }

}
