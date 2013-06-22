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
package org.teree.client.view.common;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DialogBox;

public class PopupPanel extends DialogBox {

	public PopupPanel() {
		
		addStyleName("popover");
		Style css = getElement().getStyle();
		css.setDisplay(Display.BLOCK);
		css.setPadding(0.0, Unit.PX);
		css.setProperty("width", "auto");
		
		getCaption().asWidget().addStyleName("popover-title");
		setAutoHideEnabled(true);

	}
	
	@Override
	public void setTitle(String title) {
		setText(title);
	}

}
