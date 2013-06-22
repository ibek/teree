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

import org.teree.client.view.common.PopupPanel;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContentDialog extends PopupPanel {

	private static final int WIDTH = 186;
	private static final int HEIGHT = 100;
	
	private Button okButton;

	private TextBox textField;
	private TextBox urlField;

	public ContentDialog(String title) {
		
		setTitle(title);

		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMargin(9.0, Unit.PX);
		panel.setWidth(WIDTH+"px");
		panel.setHeight(HEIGHT+"px");
		
		textField = new TextBox();
		textField.setPlaceholder("text");
		textField.setWidth(200+"px");
		urlField = new TextBox();
		urlField.setPlaceholder("url");
		urlField.setWidth(200+"px");
		
		panel.add(textField);
		panel.add(urlField);
		
		okButton = new Button("Ok");
		
		Button cancel = new Button("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ContentDialog.this.hide();
			}
		});
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(okButton);
		buttons.add(cancel);
		
		panel.add(buttons);
		
		setWidget(panel);

	}
	
	public HasClickHandlers getOk() {
		return okButton;
	}
	
	@Override
	public void show() {
		super.show();
	}
	
	public void setTextFieldVisible(boolean visible) {
		textField.setVisible(visible);
	}
	
	public String getTextField() {
		return textField.getText();
	}

	public String getUrlField() {
		String url = urlField.getText();
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://"+url;
		}
		return url;
	}

	public void setUrlField(String urlField) {
		this.urlField.setText(urlField);
	}

	public void setTextField(String textField) {
		this.textField.setText(textField);
	}

}
