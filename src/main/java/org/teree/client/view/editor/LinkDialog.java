package org.teree.client.view.editor;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LinkDialog extends DialogBox {

	private static final int WIDTH = 186;
	private static final int HEIGHT = 100;
	
	private Button okButton;

	private TextBox textField;
	private TextBox urlField;

	public LinkDialog(String title) {
		
		addStyleName("popover");
		getElement().getStyle().setDisplay(Display.BLOCK);
		getElement().getStyle().setPadding(0.0, Unit.PX);
		
		setText(title);
		getCaption().asWidget().addStyleName("popover-title");
		getCaption().asWidget().getElement().getStyle().setMarginRight(-3.0, Unit.PX);
		setAutoHideEnabled(true);

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
	
	@Override
	public void show() {
		super.show();
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
