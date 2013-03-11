package org.teree.client.view.editor.event;

import com.google.gwt.event.shared.GwtEvent;

public class CheckNode extends GwtEvent<CheckNodeHandler> {
    
    public static Type<CheckNodeHandler> TYPE = new Type<CheckNodeHandler>();
    
    public CheckNode() {
    	
    }
    
    @Override
    public Type<CheckNodeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CheckNodeHandler handler) {
        handler.check(this);
    }
    
}
