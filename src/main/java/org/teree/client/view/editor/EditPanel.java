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

import java.util.Set;

import org.teree.client.text.UIConstants;
import org.teree.client.text.UIMessages;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.client.view.resource.icon.CustomIconType;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.SplitDropdownButton;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class EditPanel extends Composite {

	private HorizontalPanel container;
	private Button save;
	private Button refresh;
	private Button collapse;
	private Button createText;
	private Button createImg;
	private Button createLink;
	private Button createMathExpr;
	private Button createPercent;
	private SplitDropdownButton createConnector;
	private NavLink mergeConnector;
	private NavLink splitConnector;
	//private DropdownButton icon; // TODO: icon for any node independent on node category - maybe put the icon into node
	private Button categories;

	//private SelectIcon iconHandler;

	private boolean collapsed;
	private Tooltip tooltipCollapse;

	public EditPanel() {

		container = new HorizontalPanel();

		save = new Button(UIConstants.LANG.save(), IconType.SAVE);
		Label space = new Label("");
		space.getElement().getStyle().setMarginRight(20, Unit.PX);

		refresh = new Button("", IconType.REFRESH);
		collapse = new Button("", IconType.DOUBLE_ANGLE_RIGHT);
		Label space2 = new Label("");
		space2.getElement().getStyle().setMarginRight(20, Unit.PX);

		createText = new Button("", IconType.PLUS);
		createImg = new Button("", IconType.PICTURE);
		createLink = new Button("", IconType.LINK);
		createMathExpr = new Button("Σ");
		createPercent = new Button("%");
		createConnector = new SplitDropdownButton("");
		mergeConnector = new NavLink(UIConstants.LANG.mergeConnector());
		splitConnector = new NavLink(UIConstants.LANG.splitNodeAndConnect());
		createConnector.add(mergeConnector);
		createConnector.add(splitConnector);

		Label space3 = new Label("");
		space3.getElement().getStyle().setMarginRight(20, Unit.PX);

		createConnector.setBaseIcon(CustomIconType.connector);

		Label space4 = new Label("");
		space4.getElement().getStyle().setMarginRight(20, Unit.PX);

		/**icon = new DropdownButton("icon");
		icon.add(IconTypeContent.loadIcons(new IconTypeContent.SelectIconHandler() {
			@Override
			public void select(IconType icon) {
				selectIcon(icon);
			}
		}));*/

		categories = new Button("", IconType.EYE_OPEN);

		container.add(save);
		container.add(space);

		Tooltip tre = new Tooltip(UIConstants.LANG.refresh_scheme());
		tre.add(refresh);
		container.add(tre);

		tooltipCollapse = new Tooltip();
		tooltipCollapse.add(collapse);
		container.add(tooltipCollapse);
		setCollapsed(true);

		container.add(space2);

		Tooltip tct = new Tooltip(UIMessages.LANG.create_text());
		tct.add(createText);
		container.add(tct);

		Tooltip tci = new Tooltip(UIMessages.LANG.create_image());
		tci.add(createImg);
		container.add(tci);

		Tooltip tcl = new Tooltip(UIMessages.LANG.create_link());
		tcl.add(createLink);
		container.add(tcl);

		Tooltip tcme = new Tooltip(UIMessages.LANG.create_math());
		tcme.add(createMathExpr);
		container.add(tcme);

		Tooltip tcp = new Tooltip(UIMessages.LANG.create_percentage());
		tcp.add(createPercent);
		container.add(tcp);

		container.add(space3);

		Tooltip tccon = new Tooltip(UIConstants.LANG.createConnector());
		tccon.add(createConnector);
		container.add(tccon);

		container.add(space4);

		/**Tooltip ti = new Tooltip(UIMessages.LANG.choose_icon());
		ti.add(icon);
		container.add(ti);*/

		Tooltip tc = new Tooltip("Show categories");
		tc.add(categories);
		container.add(tc);

		initWidget(container);

	}

	public void setEnabled(boolean enabled) {
		checkSelectedNode(null);
		save.setEnabled(enabled);
		refresh.setEnabled(enabled);
	}

	/**private void selectIcon(IconType icon) {
		iconHandler.select(icon);
	}*/

	public void checkSelectedNode(NodeWidget nw) {
		mergeConnector.setDisabled(nw == null
				|| !(nw instanceof ConnectorNodeWidget));
		splitConnector.setDisabled(nw == null
				|| !(nw instanceof TextNodeWidget)
				|| nw instanceof ConnectorNodeWidget
				|| !(nw.getNode().getChildNodes() != null && nw.getNode()
						.getChildNodes().size() > 0));
		disableNewChildNodes(nw == null || nw instanceof ConnectorNodeWidget
				|| nw instanceof LinkNodeWidget);
		//icon.getTriggerWidget().setEnabled(nw != null);
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
		if (collapsed) {
			collapse.setIcon(IconType.DOUBLE_ANGLE_RIGHT);
			tooltipCollapse.setText("Uncollapse all nodes");
		} else {
			collapse.setIcon(IconType.DOUBLE_ANGLE_LEFT);
			tooltipCollapse.setText("Collapse all nodes");
		}
	}

	private void disableNewChildNodes(boolean disable) {
		createText.setEnabled(!disable);
		createLink.setEnabled(!disable);
		createImg.setEnabled(!disable);
		createMathExpr.setEnabled(!disable);
		createPercent.setEnabled(!disable);
		((Button) createConnector.getWidget(0)).setEnabled(!disable);
	}

	public HasClickHandlers getSaveButton() {
		return save;
	}

	public HasClickHandlers getRefreshButton() {
		return refresh;
	}

	public HasClickHandlers getCollapseButton() {
		return collapse;
	}

	public HasClickHandlers getCreateTextButton() {
		return createText;
	}

	public HasClickHandlers getCreateImgButton() {
		return createImg;
	}

	public HasClickHandlers getCreateLinkButton() {
		return createLink;
	}

	public HasClickHandlers getCreateMathExprButton() {
		return createMathExpr;
	}

	public HasClickHandlers getCreatePercentButton() {
		return createPercent;
	}

	public HasClickHandlers getCreateConnectorButton() {
		return createConnector;
	}

	public HasClickHandlers getMergeConnectorButton() {
		return mergeConnector;
	}

	public HasClickHandlers getSplitConnectorButton() {
		return splitConnector;
	}

	public HasClickHandlers getCategoriesButton() {
		return categories;
	}

	/**public void setSelectIconHandler(SelectIcon handler) {
		this.iconHandler = handler;
	}*/

	public static interface SelectIcon {

		public void select(IconType icon);

	}

}
