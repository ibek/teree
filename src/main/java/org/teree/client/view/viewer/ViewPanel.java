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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.teree.client.text.UIConstants;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.ValueListBox;
import com.github.gwtbootstrap.client.ui.constants.AlternateSize;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class ViewPanel extends Composite {

	private HorizontalPanel container;
	
	private DropdownButton exportAs;
	private NavLink exportImage;
	private NavLink exportFreeMind;
	private NavLink exportJSON;

	private Button share;
	private Button collapseAll;
	private boolean collapsed;
	
	private UIConstants UIC = UIConstants.LANG;
	
	public ViewPanel() {
		
		container = new HorizontalPanel();
		
		collapseAll = new Button();
		setCollapsed(true);
		container.add(collapseAll);
		
		Label space = new Label("");
		space.getElement().getStyle().setMarginRight(20, Unit.PX);
		container.add(space);
		
		exportAs = new DropdownButton(UIC.export_as());

		exportImage = new NavLink(UIC.image());
		exportImage.setIcon(IconType.PICTURE);
		exportAs.add(exportImage);

		exportJSON = new NavLink("JSON");
		exportAs.add(exportJSON);

		exportFreeMind = new NavLink(UIC.freemind_map());
		exportAs.add(exportFreeMind);
		
		container.add(exportAs);
		
		share = new Button(UIC.share());
		container.add(share);
		
		initWidget(container);
		
	}
	
	public boolean isCollapsed() {
		return collapsed;
	}
	
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
		if (collapsed) {
			collapseAll.setText(UIC.uncollapse_all());
		} else {
			collapseAll.setText(UIC.collapse_all());
		}
	}
	
	public Button getCollapseAllButton() {
		return collapseAll;
	}
	
	public HasClickHandlers getExportImageButton() {
		return exportImage;
	}
	
	public HasClickHandlers getExportFreeMindButton() {
		return exportFreeMind;
	}
	
	public HasClickHandlers getExportJSONButton() {
		return exportJSON;
	}
	
	public Button getShareButton() {
		return share;
	}
	
}
