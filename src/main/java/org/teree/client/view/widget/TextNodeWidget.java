package org.teree.client.view.widget;

import org.teree.shared.data.Node;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

public class TextNodeWidget extends NodeWidget {

    private Label content;
    
    private TextArea editContent;
    
    protected TextNodeWidget(Node node) {
        super(node);
    }

}
