package org.teree.client.view.explorer.event;

import org.teree.shared.data.common.Scheme;
import com.google.gwt.event.shared.GwtEvent;

public class RemoveScheme extends GwtEvent<RemoveSchemeHandler> {
    
    public static Type<RemoveSchemeHandler> TYPE = new Type<RemoveSchemeHandler>();

    private Scheme s;
    
    public RemoveScheme(Scheme s) {
        this.s = s;
    }
    
    public Scheme getScheme() {
    	return s;
    }
    
    @Override
    public Type<RemoveSchemeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RemoveSchemeHandler handler) {
        handler.remove(this);
    }
}
