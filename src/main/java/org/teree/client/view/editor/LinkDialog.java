package org.teree.client.view.editor;

import org.teree.client.view.resource.DialogStyle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LinkDialog extends DialogBox {

	private DialogStyle res = DialogStyle.INSTANCE;

	private static final int WIDTH = 180;
	private static final int HEIGHT = 100;
	
	private Button okButton;

	private TextBox textField;
	private TextBox urlField;

	public LinkDialog(String title) {
		res.css().ensureInjected();
		addStyleName(res.css().dialog());
		
		setText(title);
		setAutoHideEnabled(true);

		VerticalPanel panel = new VerticalPanel();
		panel.setWidth(WIDTH+"px");
		panel.setHeight(HEIGHT+"px");
		
		textField = new TextBox();
		urlField = new TextBox();
		
		panel.add(new Label("text:"));
		panel.add(textField);
		panel.add(new Label("url:"));
		panel.add(urlField);
		
		okButton = new Button("Ok");
		
		Button cancel = new Button("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LinkDialog.this.hide();
			}
		});
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(okButton);
		buttons.add(cancel);
		
		panel.add(buttons);
		
		setWidget(panel);

	}
	
	@Override
	public int getOffsetWidth() {
		return WIDTH;
	}
	
	@Override
	public int getOffsetHeight() {
		return HEIGHT;
	}
	
	public HasClickHandlers getOk() {
		return okButton;
	}
	
	public String getUrl() {
		String url = urlField.getText();
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://"+url;
		}
		return url;
	}
	
	public String getTextField() {
		return textField.getText();
	}

}
