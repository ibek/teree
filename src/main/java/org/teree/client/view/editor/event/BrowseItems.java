package org.teree.client.view.editor.event;

import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.storage.ItemType;

import com.google.gwt.event.shared.GwtEvent;

public class BrowseItems extends GwtEvent<BrowseItemsHandler> {
    
    public static Type<BrowseItemsHandler> TYPE = new Type<BrowseItemsHandler>();

    private ItemType type;
    private NodeWidget edited;
    
    public BrowseItems(ItemType type, NodeWidget edited) {
        this.type = type;
        this.edited = edited;
    }
    
    public ItemType getType() {
		return type;
	}
    
    public NodeWidget getEditedNodeWidget() {
    	return edited;
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
