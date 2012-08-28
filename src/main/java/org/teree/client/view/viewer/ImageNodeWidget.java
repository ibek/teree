package org.teree.client.view.viewer;

import org.teree.shared.data.ImageLink;
import org.teree.shared.data.Node;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public class ImageNodeWidget extends NodeWidget {

	interface Resources extends ClientBundle {
		@Source("../resource/load_image.png")
		ImageResource noImage();
	}

	private Image content;

	private Resources res = GWT.create(Resources.class);

	public ImageNodeWidget() {

	}

	public ImageNodeWidget(Node node) {
		super(node);

		init();

		container.add(content);

	}

	private void init() {
		content = new Image();

		content.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				((ImageLink) node.getContent()).setUrl(null);
				content.setResource(res.noImage());
			}
		});
		
		String url = ((ImageLink) node.getContent()).getUrl();
		if (url != null) {
			content.setUrl(url);
		} else {
			content.setResource(res.noImage());
		}
		
	}

}
