package org.teree.client.view.viewer;

import org.teree.client.view.common.NodePainter;
import org.teree.shared.data.common.Link;
import org.teree.shared.data.common.Node;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.Anchor;

public class LinkNodeWidget extends NodeWidget {

	private Anchor content;

	public LinkNodeWidget(Node node) {
		super(node);

		init();

		container.add(content);
		update();

	}

	private void init() {
		content = new Anchor();
		content.setTarget("_blank");
		content.setStylePrimaryName(resources.css().node());
		content.setStyleDependentName("view", true);

		Link link = (Link) node.getContent();
		String url = link.getUrl();
		if (url != null) {
			content.setHref(url);
		}
		String text = link.getText();
		if (text == null || text.isEmpty()) {
			if (url != null && !url.isEmpty()) {
				text = "@" + url;
			} else {
				text = "@link";
			}
		}
		content.setText(text);
	}

	@Override
	public void draw(Context2d context, int x, int y) {
		NodePainter.drawSingleLine(context, x, y, content.getText(),
				getOffsetWidth(), getOffsetHeight(), node.getCategory());
	}

}
