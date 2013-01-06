package org.teree.client.view.viewer;

import java.util.List;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.CurrentPresenter;
import org.teree.client.presenter.SchemeViewer;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.shared.data.scheme.Connector;
import org.teree.shared.data.scheme.Link;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.Scheme;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

public class ConnectorNodeWidget extends TextNodeWidget {

	public ConnectorNodeWidget(Node node) {
		super(node);
		init();
		setCollapsed(!(node.getChildNodes() != null && node.getChildNodes().size() > 0));
	}

	private void init() {
		Connector con = (Connector) node.getContent();
		setIconType(con.getRoot());
		
		addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (node.getChildNodes() == null || node.getChildNodes().isEmpty()) {
					event.stopPropagation();
					// get scheme from server
					((SchemeViewer)CurrentPresenter.getInstance().getPresenter()).getScheme(((Connector)node.getContent()).getOid(), new RemoteCallback<Scheme>() {
						@Override
						public void callback(Scheme response) {
							List<Node> childNodes = response.getRoot().getChildNodes();
							for (int i=0; childNodes != null && i<childNodes.size(); ++i) {
								Node n = childNodes.get(i);
								n.setLocation(node.getLocation());
								node.addChild(n);
							}
							ConnectorNodeWidget.this.getParent().fireEvent(new NodeChanged(node));
						}
					});
				}
			}
		}, ClickEvent.getType());
		
	}
	
}
