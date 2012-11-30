package org.teree.client.view.explorer.event;

import org.teree.shared.data.scheme.Scheme;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateSchemePermissions extends GwtEvent<UpdateSchemePermissionsHandler> {
    
    public static Type<UpdateSchemePermissionsHandler> TYPE = new Type<UpdateSchemePermissionsHandler>();

    private Scheme s;
    
    public UpdateSchemePermissions(Scheme s) {
        this.s = s;
    }
    
    public Scheme getScheme() {
    	return s;
    }
    
    @Override
    public Type<UpdateSchemePermissionsHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UpdateSchemePermissionsHandler handler) {
        handler.updatePermissions(this);
    }
}
