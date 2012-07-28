package org.teree.client.viewer.ui.widget;

import org.teree.shared.data.NodeContent;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ContentWidget extends Composite {

    private NodeContent _content;
    
    
    public ContentWidget(NodeContent content){
        _content = content;
        Widget w = getContent();
        if (w != null) {
            initWidget(w);
        }
    }
    
    private Widget getContent() {
        Widget w = null;
        if (_content.getText() != null) {
            w = new Label(_content.getText());
            //w.setWidth(_content.getWidth()+"px");
            //w.setHeight(_content.getHeight()+"px");
        }
        return w;
    }
    
    public int getWidgetWidth() {
        return getWidget().getOffsetWidth();
    }
    
    public int getWidgetHeight() {
        return getWidget().getOffsetHeight();
    }
    
}
