package org.teree.client.view.viewer;

import org.teree.shared.data.scheme.Connector;
import org.teree.shared.data.scheme.Link;
import org.teree.shared.data.scheme.Node;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.Anchor;

public class ConnectorNodeWidget extends NodeWidget {

	private Anchor content;

	public ConnectorNodeWidget(Node node) {
		super(node);

		init();

		container.add(content);

	}

	private void init() {
		content = new Anchor();
        content.setStylePrimaryName(resources.css().node());
        content.setStyleDependentName("view", true);

		Connector con = (Connector) node.getContent();
		String text = con.toString();
		if (text == null || text.isEmpty()) {
			text = "connector";
		}
		content.setText("╚╦"+text);
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
