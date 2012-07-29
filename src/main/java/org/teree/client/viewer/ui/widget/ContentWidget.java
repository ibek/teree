package org.teree.client.viewer.ui.widget;

import javax.enterprise.event.Event;

import org.teree.client.viewer.ui.widget.event.ContentChanged;
import org.teree.shared.data.NodeContent;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ContentWidget extends Composite {

    private NodeContent _content;
    
    private boolean _editable;
    
    private ContentChanged _changeEvent;

    public static final int MIN_WIDTH = 18;
    public static final int MIN_HEIGHT = 18;
    public static final int MAX_WIDTH = 100;
    
    public ContentWidget(NodeContent content, boolean editable){
        _content = content;
        _editable = editable;
        Widget w = getContentWidget();
        if (w != null) {
            initWidget(w);
        }
    }
    
    private Widget getContentWidget() {
        Widget w = null;
        if (_content.getText() != null) {
            if(_editable && _content.getWidth() > 0){
                final TextArea ftb = new TextArea();
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                    @Override
                    public void execute() {
                        ftb.setFocus(true);
                    }
                });
                w = ftb;
                ftb.addBlurHandler(new BlurHandler() {
                    @Override
                    public void onBlur(BlurEvent event) {
                        confirmChanges(ftb);
                    }
                });
                ftb.addKeyUpHandler(new KeyUpHandler() {
                    @Override
                    public void onKeyUp(KeyUpEvent event) {
                        if(event.getNativeKeyCode() == 13 && !event.isShiftKeyDown()){
                            confirmChanges(ftb);
                        }
                    }
                });
                ftb.setText(_content.getText());
                Style s = w.getElement().getStyle();
                s.setBorderWidth(0.0, Unit.PX);
                s.setPadding(0.0, Unit.PX);
                //s.setBackgroundColor("transparent");
                ftb.setWidth(_content.getWidth()+"px");
                ftb.setHeight(_content.getHeight()+"px");
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
                            cw.setContentChangeListener(_changeEvent);
                            p.add(cw);
                        }
                    }
                }, ClickEvent.getType());
            }

            // fix for resize minimal node, look at MapType:prepare
            if(_content.getWidth() == MIN_WIDTH){
                w.setWidth(MIN_WIDTH+"px");
            }else if(_content.getWidth() >= MAX_WIDTH){
                w.setWidth(MAX_WIDTH+"px");
            }
            
            if(_content.getHeight() == MIN_HEIGHT){
                w.setHeight(MIN_HEIGHT+"px");
            }
            
        }
        return w;
    }
    
    private void confirmChanges(TextArea ta) {
        Widget parent = getParent();
        if(parent instanceof Panel){
            Panel p = (Panel)parent;
            String newtext = ta.getText();
            if(newtext.compareTo(_content.getText()) != 0){
                _content.setText(newtext);
                if(_changeEvent != null){
                    _changeEvent.changed(_content);
                }
            }else{
                remove(p);
                ContentWidget cw = new ContentWidget(_content, false);
                cw.setContentChangeListener(_changeEvent);
                p.add(cw);
            }
        }
    }
    
    public NodeContent getNodeContent() {
        return _content;
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
    
    public ContentChanged getContentChangeListener() {
        return _changeEvent;
    }
    
    public void setContentChangeListener(ContentChanged listener) {
        this._changeEvent = listener;
    }
    
}
