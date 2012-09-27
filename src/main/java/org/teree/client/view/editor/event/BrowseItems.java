package org.teree.client.view.editor.event;

import org.teree.client.view.editor.storage.ItemType;

import com.google.gwt.event.shared.GwtEvent;

public class BrowseItems extends GwtEvent<BrowseItemsHandler> {
    
    public static Type<BrowseItemsHandler> TYPE = new Type<BrowseItemsHandler>();

    private ItemType type;
    
    public BrowseItems(ItemType type) {
        this.type = type;
    }
    
    public ItemType getType() {
		return type;
	}

	@Override
    public Type<BrowseItemsHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BrowseItemsHandler handler) {
        handler.browse(this);
    }
}
