package org.teree.client.view.explorer.event;

import org.teree.shared.data.Scheme;

import com.google.gwt.event.shared.GwtEvent;

public class PublishScheme extends GwtEvent<PublishSchemeHandler> {
    
    public static Type<PublishSchemeHandler> TYPE = new Type<PublishSchemeHandler>();

    private Scheme s;
    
    public PublishScheme(Scheme s) {
        this.s = s;
    }
    
    public Scheme getScheme() {
    	return s;
    }
    
    @Override
    public Type<PublishSchemeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PublishSchemeHandler handler) {
        handler.select(this);
    }
}
