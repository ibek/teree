package org.teree.client.viewer.ui.widget;

import org.teree.client.viewer.ui.widget.event.EditMode;
import org.teree.client.viewer.ui.widget.event.SelectNode;
import org.teree.client.viewer.ui.widget.event.ViewMode;
import org.teree.shared.data.NodeContent;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TextContentWidget extends ContentWidget {

	protected Widget _contentWidget;
	
	public TextContentWidget(NodeContent content, EditMode elistener, ViewMode vlistener, boolean editable){
		super(content, elistener, vlistener, editable);
		
		_contentWidget = getContentWidget();
        initWidget(_contentWidget);
	}
	
	private Widget getContentWidget() {
		Widget w = null;
		
		if(isEdited() && _content.getWidth() > 0){
		    
            final TextArea ftb = new TextArea();
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    ftb.setFocus(true);
                }
            });
            w = ftb;
            ftb.addBlurHandler(new BlurHandler() { // lost focus
                @Override
                public void onBlur(BlurEvent event) {
                    confirmChanges(ftb);
                }
            });
            ftb.addKeyUpHandler(new KeyUpHandler() {
                @Override
                public void onKeyUp(KeyUpEvent event) {
                    if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.isShiftKeyDown()){
                        confirmChanges(ftb);
                    }else if(event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE){
                        _viewEvent.view();
                    }
                }
            });
            ftb.addKeyDownHandler(new KeyDownHandler() {
				@Override
				public void onKeyDown(KeyDownEvent event) {
					if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.isShiftKeyDown()){
                        // don't create new line
                        event.preventDefault(); 
					}
				}
			});
            ftb.setText(_content.getText());
            DOM.setStyleAttribute(ftb.getElement(), "fontFamily", "monospace");
            Style s = w.getElement().getStyle();
            s.setFontSize(14.0, Unit.PX);
            s.setBorderWidth(0.0, Unit.PX);
            s.setPadding(0.0, Unit.PX);
            //s.setBackgroundColor("transparent");
            ftb.setWidth((_content.getWidth()+2)+"px");
            ftb.setHeight(_content.getHeight()+"px");
        } else {
            
            /**Label l = new Label(_content.getText());
            l.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if(_editEvent != null && _editable){
                        _editEvent.edit();
                    }
                }
            });
            w = l;*/
            HTML html = new HTML(_content.getText());
            html.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if(_editEvent != null && _editable){
                        _editEvent.edit();
                    }
                }
            });
            DOM.setStyleAttribute(html.getElement(), "fontFamily", "monospace");
            DOM.setStyleAttribute(html.getElement(), "whiteSpace", "pre");
            html.getElement().getStyle().setFontSize(14.0, Unit.PX);
            w = html;
            
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
	
}
