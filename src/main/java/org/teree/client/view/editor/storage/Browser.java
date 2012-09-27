package org.teree.client.view.editor.storage;

import java.util.List;

import org.teree.shared.data.storage.ImageInfo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Panel with items from Amazon Storage.
 * TODO: support for any item types (image, pdf, txt, ...)  
 */
public class Browser extends Composite {

	private FlowPanel container;
	
	public Browser() {
		
		container = new FlowPanel();
		
		initWidget(container);
		
	}
	
	public void setBrowserItems(List<?> items, ItemType type) {
		switch (type) {
			case Image: {
				loadImages((List<ImageInfo>) items);
				break;
			}
		}
	}
	
	private void loadImages(List<ImageInfo> images) {
		container.clear();
		for (int i=0; i<images.size(); ++i) {
			ImageInfo ii = images.get(i);
			ImageWidget iw = new ImageWidget();
			iw.setImageInfo(ii);
			container.add(iw);
		}
	}
	
}
