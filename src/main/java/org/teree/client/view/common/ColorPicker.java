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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class ColorPicker extends PopupPanel implements ClickHandler {

	private String selectedColor = "";
	private List<ColorHandler> listeners = new ArrayList<ColorHandler>();

	String[][] colors = new String[][] {
			// black to white
			{ "#000000", "#444444", "#888888", "#bbbbbb", "#ffffff" },
			// blue
			{ "#002851", "#006bd6", "#3399FF", "#70b7ff", "#b7dbff" },
			// red
			{ "#5b0000", "#c10000", "#FF0000", "#ff3d3d", "#ff8e8e" },
			// orange
			{ "#c17500", "#ea8d00", "#ffa214", "#ffba51", "#ffd28e" },
			// yellow
			{ "#7a7a00", "#d6d600", "#FFFF00", "#feff70", "#fefeb7" },
			// green
			{ "#004700", "#00b700", "#00FF00", "#47ff47", "#a3fea3" },
			// purple
			{ "#73165b", "#b72391", "#d62dab", "#e169c3", "#efadde" }

	};

	public ColorPicker() {
		Grid grid = new Grid(colors.length, colors[0].length);
		grid.setCellSpacing(0);
		grid.setCellPadding(0);
		for (int i = 0; i < colors.length; i++) {

			for (int j = 0; j < colors[0].length; j++) {
				String color = colors[i][j];
				grid.setWidget(i, j, new ColorLabel(color, this));

			}
		}
		setModal(true);
		setAutoHideEnabled(true);
		setWidget(grid);
		getElement().getStyle().setZIndex(1100);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() instanceof ColorLabel) {
			ColorLabel label = (ColorLabel) event.getSource();
			synchronized (listeners) {
				for (ColorHandler handler : listeners) {
					handler.newColorSelected(label.getColor());
				}
			}
			hide();
		}
	}

	public void addColorHandler(ColorHandler handler) {
		synchronized (listeners) {
			listeners.add(handler);
		}

	}

	public void removeColorHandler(ColorHandler handler) {
		synchronized (listeners) {
			listeners.remove(handler);
		}
	}

	public String getSelectedColor() {
		return selectedColor;
	}

	private class ColorLabel extends HTML {

		private String color;

		public ColorLabel(String color, ClickHandler handler) {
			this.color = color;
			setTitle(color);
			this.getElement().getStyle().setBackgroundColor(color);
			this.getElement().getStyle().setCursor(Cursor.POINTER);
			setWidth("17px");
			setHeight("17px");
			this.addClickHandler(handler);
		}

		public String getColor() {
			return color;
		}
	}

	public static interface ColorHandler {
		void newColorSelected(String color);
	}

}
