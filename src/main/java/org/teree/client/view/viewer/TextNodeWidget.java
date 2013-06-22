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
package org.teree.client.view.viewer;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.common.NodeCategoryStyle;
import org.teree.client.view.common.NodePainter;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.client.view.viewer.event.CollapseNode;
import org.teree.shared.data.common.Text;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeCategory;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.HTML;

public class TextNodeWidget extends NodeWidget {

	protected HTML content;

	protected TextNodeWidget(Node node) {
		super(node);
		init();
		bind();
	}

	private void init() {
		content = new HTML();
		content.setText(node.getContent().toString());
		content.setStylePrimaryName(resources.css().node());
		content.addStyleName(resources.css().nodeView());

		content.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.stopPropagation(); // it prevents map moving
			}
		});

		container.add(content);

		update();

	}

	private void bind() {
		addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Object src = event.getSource();
				if (src instanceof TextNodeWidget) {
					TextNodeWidget.this.getParent().fireEvent(
							new CollapseNode(TextNodeWidget.this));
				}
			}
		}, ClickEvent.getType());
	}

	@Override
	public void draw(Context2d context, int x, int y) {
		NodePainter.drawTextNode(context, x, y, content.getText(), collapsed, getOffsetWidth(),
				node.getCategory());
	}

	@Override
	public void update() {
		super.update();
		if (getOffsetWidth() >= Settings.NODE_MAX_WIDTH) {
			content.setWidth(Settings.NODE_MAX_WIDTH + "px");
		}
	}

	@Override
	public void setCollapsed(boolean collapsed) {
		if (collapsed) {
			content.setText("+ " + node.getContent().toString());
		} else {
			content.setText(node.getContent().toString());
		}
		this.collapsed = collapsed;
	}

}
