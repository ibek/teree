package org.teree.client.viewer.ui.widget;

import org.teree.client.viewer.ui.widget.event.ContentChanged;
import org.teree.client.viewer.ui.widget.event.EditMode;
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
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * TODO: use SelectNode event to select the clicked node
 * 
 * @author ibek
 *
 */
public class ContentWidget extends Composite {

    protected NodeContent _content;
    protected boolean _editable;
    
    protected ContentChanged _changeEvent;
    protected EditMode _editEvent;
    protected ViewMode _viewEvent;

    public static final int MIN_WIDTH = 30;
    public static final int MIN_HEIGHT = 18;
    public static final int MAX_WIDTH = 400;
    
    protected ContentWidget(NodeContent content, EditMode elistener, ViewMode vlistener, boolean editable){
        _content = content;
        _editable = editable;
        _editEvent = elistener;
        _viewEvent = vlistener;
    }
    
    public static ContentWidget create(NodeContent content, EditMode elistener, ViewMode vlistener, boolean editable) {
    	if(content.getText() != null){
    		return new TextContentWidget(content, elistener, vlistener, editable);
    	}
    	return null;
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
