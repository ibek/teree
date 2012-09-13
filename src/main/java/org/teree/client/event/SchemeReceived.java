package org.teree.client.event;

import org.teree.shared.data.Scheme;

import com.google.gwt.event.shared.GwtEvent;

public class SchemeReceived extends GwtEvent<SchemeReceivedHandler> {
    
    public static Type<SchemeReceivedHandler> TYPE = new Type<SchemeReceivedHandler>();

    private Scheme s;
    
    public SchemeReceived(Scheme s) {
        this.s = s;
    }
    
    public Scheme getScheme() {
        return s;
    }

    @Override
    public Type<SchemeReceivedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SchemeReceivedHandler handler) {
        handler.received(this);
    }
}
