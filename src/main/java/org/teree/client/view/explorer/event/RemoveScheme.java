package org.teree.client.view.explorer.event;

import org.teree.shared.data.tree.Tree;

import com.google.gwt.event.shared.GwtEvent;

public class RemoveScheme extends GwtEvent<RemoveSchemeHandler> {
    
    public static Type<RemoveSchemeHandler> TYPE = new Type<RemoveSchemeHandler>();

    private Tree s;
    
    public RemoveScheme(Tree s) {
        this.s = s;
    }
    
    public Tree getScheme() {
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
