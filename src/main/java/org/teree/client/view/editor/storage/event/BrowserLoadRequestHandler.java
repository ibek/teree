package org.teree.client.view.editor.storage.event;

import org.teree.client.view.editor.storage.ItemType;


public interface BrowserLoadRequestHandler {

	public void loadRequest(ItemType type, boolean publicStorage);
	
}
