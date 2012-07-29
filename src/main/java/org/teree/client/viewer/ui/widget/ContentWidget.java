package org.teree.client.viewer.ui.widget;

import javax.enterprise.event.Event;

import org.teree.shared.data.NodeContent;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ContentWidget extends Composite {

    private NodeContent _content;
    
    private boolean _editable;
    
    public ContentWidget(NodeContent content, boolean editable){
        _content = content;
        _editable = editable;
        Widget w = getContent();
        if (w != null) {
            initWidget(w);
        }
    }
    
    private Widget getContent() {
        Widget w = null;
        if (_content.getText() != null) {
            if(_editable && _content.getWidth() > 0){
                final TextBox ftb = new TextBox();
                ftb.setFocus(true);
                w = ftb;
                ((TextBox)w).addBlurHandler(new BlurHandler() {
                    @Override
                    public void onBlur(BlurEvent event) {
                        Widget parent = getParent();
                        if(parent instanceof Panel){
                            Panel p = (Panel)parent;
                            String newtext = ((TextBox)ftb).getText();
                            if(newtext.compareTo(_content.getText()) != 0){
                                _content.setText(newtext);
                                //fireChangeEvent();
                            }else{
                                remove(p);
                                ContentWidget cw = new ContentWidget(_content, false);
                                p.add(cw);
                            }
                        }
                    }
                });
                ((TextBox)w).setText(_content.getText());
                Style s = w.getElement().getStyle();
                s.setBorderWidth(0.0, Unit.PX);
                s.setPadding(0.0, Unit.PX);
                s.setBackgroundColor("transparent");
                s.setWidth(_content.getWidth(), Unit.PX);
                s.setHeight(_content.getHeight(), Unit.PX);
            } else {
                w = new Label(_content.getText());
                w.addDomHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        Widget parent = getParent();
                        if(parent instanceof Panel){
                            Panel p = (Panel)parent;
                            remove(p);
                            ContentWidget cw = new ContentWidget(_content, true);
                            p.add(cw);
                        }
                    }
                }, ClickEvent.getType());
            }
        }
        return w;
    }
    
    private void remove(Panel p){
        p.remove(this);
    }
    
    public void setEditable(boolean editable) {
        _editable = editable;
    }
    
    public int getWidgetWidth() {
        return getWidget().getOffsetWidth();
    }
    
    public int getWidgetHeight() {
        return getWidget().getOffsetHeight();
    }
    
}
