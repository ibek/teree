/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.client.view.editor;

import org.teree.shared.data.common.Connector;
import org.teree.shared.data.common.Text;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Node.NodeLocation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class ConnectorNodeWidget extends TextNodeWidget {

	private Connector connector;
	private ConnectorDialog connectorDialog;

	public ConnectorNodeWidget(Node node) {
		super(node);
		
		connector = (Connector)node.getContent();
		nodeContent = connector.getRoot();
		
		view();

	}
	
	@Override
	public void view() {
		super.view();
		update();
	}
	
	@Override
    public void update() {
		super.update();
		String text = connector.toString();
		if (text == null || text.isEmpty()) {
			text = "[connector]";
		}
		content.setText("╠>" + text);
    }

	@Override
	public void edit() {
		if (connectorDialog == null) {
			connectorDialog = new ConnectorDialog();
			
			connectorDialog.getOk().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Text it = connectorDialog.getRoot();
					if (it != null) {
						connector.setOid(connectorDialog.getOid());
						connector.setRoot(it);
						update();
						connectorDialog.hide();
					}
				}
			});
		}
		connectorDialog.clearDialog();
		connectorDialog.setOid(connector.getOid());
		
		connectorDialog.show();

		int x = 0;
		if (node.getLocation() == NodeLocation.LEFT) {
			x = -connectorDialog.getOffsetWidth();
		} else {
			x = content.getOffsetWidth();
		}
		
		connectorDialog.setPopupPosition(getAbsoluteLeft() + x, 
				getAbsoluteTop() + content.getOffsetHeight()/2 - connectorDialog.getOffsetHeight()/2);
	}
	
	@Override
	public void setCollapsed(boolean collapsed) {
		if (collapsed) {
			content.setText("+ "+node.getContent().toString());
		} else {
			content.setText("╠>" + node.getContent().toString());
		}
		this.collapsed = collapsed;
	}

}
