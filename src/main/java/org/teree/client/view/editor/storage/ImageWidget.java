package org.teree.client.view.editor.storage;

import org.teree.shared.data.storage.ImageInfo;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.ui.Image;

public class ImageWidget extends ItemWidget {

	private ImageInfo imageInfo;
	
	private Image image;
	
	public ImageWidget() {
		image = new Image();
		image.getElement().getStyle().setCursor(Cursor.POINTER);
		container.add(image);
	}

	public ImageInfo getImageInfo() {
		return imageInfo;
	}

	public void setImageInfo(ImageInfo imageInfo) {
		this.imageInfo = imageInfo;
		image.setUrl(imageInfo.getUrl());
		loaded();
	}

	@Override
	public String getItemTitle() {
		return imageInfo.getName();
	}
	
}
