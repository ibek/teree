package org.teree.client.view.viewer;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.CurrentPresenter;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.shared.data.common.Connector;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.StructureType;
import org.teree.shared.data.tree.Tree;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

public class ConnectorNodeWidget extends TextNodeWidget {
	
	private boolean firstLoad = true;

	public ConnectorNodeWidget(Node node) {
		super(node);
		init();
		setCollapsed(!(node.getChildNodes() != null && node.getChildNodes().size() > 0));
	}

	private void init() {
		addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (firstLoad) {
					event.stopPropagation();
					firstLoad = false;
					// get scheme from server
					CurrentPresenter.getInstance().getPresenter().getScheme(((Connector)node.getContent()).getOid(), new RemoteCallback<Scheme>() {
						@Override
						public void callback(Scheme response) {
							if (response == null || response.getStructure() == null) {
								CurrentPresenter.getInstance().getPresenter().displayError("Cannot open connected scheme");
							} else if (response.getStructure() == StructureType.Tree) {
								Node root = ((Tree)response).getRoot();
								root.setLocation(node.getLocation());
								node.insertBefore(root);
								node.remove();
								Widget p = ConnectorNodeWidget.this.getParent();
								removeFromParent();
								p.fireEvent(new NodeChanged(root));
							}
						}
					});
				}
			}
		}, ClickEvent.getType());
		
	}
	
}
