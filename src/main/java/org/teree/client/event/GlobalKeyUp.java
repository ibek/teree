package org.teree.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class GlobalKeyUp extends GwtEvent<GlobalKeyUpHandler> {
    
    public static Type<GlobalKeyUpHandler> TYPE = new Type<GlobalKeyUpHandler>();

    private int key;
    
    public GlobalKeyUp(int key) {
        this.key = key;
    }
    
    public int getKey() {
    	return key;
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
