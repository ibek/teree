package org.teree.client.view.editor.storage;

import com.google.gwt.event.shared.GwtEvent;

public class BrowserItemSelected extends GwtEvent<BrowserItemSelectedHandler> {
    
    public static Type<BrowserItemSelectedHandler> TYPE = new Type<BrowserItemSelectedHandler>();

    private ItemWidget iw;
    
    public BrowserItemSelected(ItemWidget iw) {
        this.iw = iw;
    }
    
    public ItemWidget getItem() {
		return iw;
	}

	@Override
    public Type<BrowserItemSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BrowserItemSelectedHandler handler) {
        handler.selected(this);
    }
}
