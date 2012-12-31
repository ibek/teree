package org.teree.client.view.editor;

import org.teree.client.view.common.TDialog;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConnectorDialog extends TDialog {

	private static final int WIDTH = 186;
	private static final int HEIGHT = 100;
	
	// TODO: add preview of scheme and refresh button
	private Button okButton;
	private Button search;

	private TextBox oid;

	public ConnectorDialog() {
		
		setTitle("Set Connector");

		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMargin(9.0, Unit.PX);
		panel.setWidth(WIDTH+"px");
		panel.setHeight(HEIGHT+"px");

		HorizontalPanel oidhp = new HorizontalPanel();
		oid = new TextBox();
		oid.setPlaceholder("oid");
		oid.setWidth(200+"px");

		search = new Button("", IconType.SEARCH);
		search.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO: open search dialog
			}
		});
		oidhp.add(oid);
		oidhp.add(search);
		panel.add(oidhp);
		
		okButton = new Button("Ok");
		
		Button cancel = new Button("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ConnectorDialog.this.hide();
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
	
	public String getOid() {
		return oid.getText();
	}

	public void setOid(String oid) {
		this.oid.setText(oid);
	}

}
