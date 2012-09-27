package org.teree.client.view.viewer;

import org.teree.shared.data.scheme.Link;
import org.teree.shared.data.scheme.Node;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.Anchor;

public class LinkNodeWidget extends NodeWidget {

	private Anchor content;

	public LinkNodeWidget(Node node) {
		super(node);

		init();

		container.add(content);

	}

	private void init() {
		content = new Anchor();
		content.setTarget("_blank");

		Link link = (Link) node.getContent();
		String url = link.getUrl();
		if (url != null) {
			content.setHref(url);
		}
		String text = link.getText();
		if (text == null || text.isEmpty()) {
			text = "@link";
		}
		content.setText(text);
	}

    @Override
    public void draw(Context2d context, int x, int y) {
    	context.save();
    	context.setFont("14px monospace");
        context.setFillStyle("#000000");
        context.fillText(content.getText(), x, y);
        context.restore();
    }

}
