package org.teree.client.view.editor.storage.event;

import org.teree.client.view.editor.storage.ItemWidget;

import com.google.gwt.event.shared.GwtEvent;

public class BrowserItemDelete extends GwtEvent<BrowserItemDeleteHandler> {
    
    public static Type<BrowserItemDeleteHandler> TYPE = new Type<BrowserItemDeleteHandler>();

    private ItemWidget iw;
    
    public BrowserItemDelete(ItemWidget iw) {
        this.iw = iw;
    }
    
    public ItemWidget getItem() {
		return iw;
	}

	@Override
    public Type<BrowserItemDeleteHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BrowserItemDeleteHandler handler) {
        handler.delete(this);
    }
}
