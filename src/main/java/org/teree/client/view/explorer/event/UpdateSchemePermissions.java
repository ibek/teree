package org.teree.client.view.explorer.event;

import org.teree.shared.data.tree.Tree;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateSchemePermissions extends GwtEvent<UpdateSchemePermissionsHandler> {
    
    public static Type<UpdateSchemePermissionsHandler> TYPE = new Type<UpdateSchemePermissionsHandler>();

    private Tree s;
    
    public UpdateSchemePermissions(Tree s) {
        this.s = s;
    }
    
    public Tree getScheme() {
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
