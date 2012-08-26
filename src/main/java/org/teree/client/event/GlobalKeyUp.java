package org.teree.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Event;

public class GlobalKeyUp extends GwtEvent<GlobalKeyUpHandler> {
    
    public static Type<GlobalKeyUpHandler> TYPE = new Type<GlobalKeyUpHandler>();

    private Event event;
    
    public GlobalKeyUp(Event event) {
        this.event = event;
    }
    
    public Event getEvent() {
    	return event;
    }
    
    @Override
    public Type<GlobalKeyUpHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GlobalKeyUpHandler handler) {
        handler.onKeyUp(this);
    }
}
