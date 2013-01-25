package org.teree.client.view.viewer;

import java.util.List;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.CurrentPresenter;
import org.teree.client.presenter.Viewer;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.shared.data.common.Connector;
import org.teree.shared.data.common.Link;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.StructureType;
import org.teree.shared.data.tree.Tree;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

public class ConnectorNodeWidget extends TextNodeWidget {

	public ConnectorNodeWidget(Node node) {
		super(node);
		init();
		setCollapsed(!(node.getChildNodes() != null && node.getChildNodes().size() > 0));
		bind();
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
					((Viewer)CurrentPresenter.getInstance().getPresenter()).getScheme(((Connector)node.getContent()).getOid(), new RemoteCallback<Scheme>() {
						@Override
						public void callback(Scheme response) {
							if (response.getStructure() == StructureType.Tree) {
								List<Node> childNodes = ((Tree)response).getRoot().getChildNodes();
								for (int i=0; childNodes != null && i<childNodes.size(); ++i) {
									Node n = childNodes.get(i);
									n.setLocation(node.getLocation());
									node.addChild(n);
								}
								ConnectorNodeWidget.this.getParent().fireEvent(new NodeChanged(node));
							}
						}
					});
				}
			}
		}, ClickEvent.getType());
		
	}
	
	private void bind() {
		addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Object src = event.getSource();
				if (src instanceof ConnectorNodeWidget) {
					//changeCollapseNode((ConnectorNodeWidget) src); // TODO: change collapse node - new event!
				}
			}
		}, ClickEvent.getType());
	}
	
}
