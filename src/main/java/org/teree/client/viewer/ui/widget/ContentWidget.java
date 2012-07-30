package org.teree.client.viewer.ui.widget;

import org.teree.client.viewer.ui.widget.event.ContentChanged;
import org.teree.client.viewer.ui.widget.event.EditMode;
import org.teree.client.viewer.ui.widget.event.ViewMode;
import org.teree.shared.data.NodeContent;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class ContentWidget extends Composite {

    private NodeContent _content;
    
    private ContentChanged _changeEvent;
    
    private EditMode _editEvent;
    
    private ViewMode _viewEvent;

    public static final int MIN_WIDTH = 30;
    public static final int MIN_HEIGHT = 18;
    public static final int MAX_WIDTH = 400;
    
    public ContentWidget(NodeContent content, EditMode elistener, ViewMode vlistener){
        _content = content;
        _editEvent = elistener;
        _viewEvent = vlistener;
        Widget w = getContentWidget();
        if (w != null) {
            initWidget(w);
        }
    }
    
    private Widget getContentWidget() {
        Widget w = null;
        if (_content.getText() != null) {
            if(isEdited() && _content.getWidth() > 0){
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
                        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.isShiftKeyDown()){
                            // don't create new line
                            event.preventDefault(); 
                            event.stopPropagation();
                            confirmChanges(ftb);
                        }else if(event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE){
                            _viewEvent.view();
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
                        edit();
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
        String newtext = ta.getText();
        if(newtext.compareTo(_content.getText()) != 0){
            _content.setText(newtext);
            if(_changeEvent != null){
                _changeEvent.changed(_content);
            }
        }
        _viewEvent.view();
    }
    
    /**
     * Switch into editable mode.
     */
    public void edit(){
        _editEvent.edit();
    }
    
    public NodeContent getNodeContent() {
        return _content;
    }
    
    public boolean isEdited(){
        return _editEvent == null;
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
