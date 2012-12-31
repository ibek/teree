package org.teree.client.view.editor;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.Settings;
import org.teree.client.controller.GeneralController;
import org.teree.client.event.SchemeReceived;
import org.teree.client.view.common.TDialog;
import org.teree.shared.data.scheme.IconText;
import org.teree.shared.data.scheme.Scheme;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConnectorDialog extends TDialog {

	private static final int WIDTH = 226;
	private static final int HEIGHT = 100;
	
	// TODO: add preview of scheme and refresh button
	private Button okButton;
	private Button search;

	private Image preview;
	private TextBox oid;
	
	private Scheme scheme;

	public ConnectorDialog() {
		
		setTitle("Set Connector");

		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMargin(0.0, Unit.PX);
		//panel.setWidth(WIDTH+"px");
		panel.setHeight(HEIGHT+"px");
		
		preview = new Image();
		//preview.setWidth(Settings.SAMPLE_MAX_WIDTH + "px");
		//preview.setHeight(Settings.SAMPLE_MAX_HEIGHT + "px");
		panel.add(preview);

		HorizontalPanel oidhp = new HorizontalPanel();
		oid = new TextBox();
		oid.setPlaceholder("oid");
		oid.setWidth(200+"px");

		search = new Button("", IconType.SEARCH);
		search.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String t = oid.getText();
				if (t.length() == 25 && !t.contains(" ")) { // it is oid
					load();
				} else {
					
				}
			}
		});
		oidhp.add(oid);
		oidhp.add(search);
		panel.add(oidhp);
		
		okButton = new Button();
		okButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!isReady()) {
					event.stopPropagation();
					if (oid.getText().isEmpty()) {
						// error - oid should be set
					} else {
						load();
					}
				}
			}
		});
		setOkButton();
		
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
	
	private void setOkButton() {
		if (isReady()) {
			okButton.setText("Ok");
		} else {
			okButton.setText("Check");
		}
	}
	
	private void load() {
		String o = oid.getText();
		if (!o.isEmpty()) {
			GeneralController.getInstance().getGeneralService().call(new RemoteCallback<Scheme>() {
				@Override
				public void callback(Scheme response) {
					scheme = response;
					if (response != null) {
						preview.setUrl(scheme.getSchemePicture());
					}
					setOkButton();
				}
			}, new ErrorCallback() {
				@Override
				public boolean error(Message message, Throwable throwable) {
					
					return false;
				}
			}).getScheme(o);
		}
	}
	
	public boolean isReady() {
		return scheme != null && scheme.getRoot() != null;
	}
	
	public HasClickHandlers getOk() {
		return okButton;
	}
	
	public IconText getRoot() {
		return (scheme == null || scheme.getRoot() == null)?null:(IconText)scheme.getRoot().getContent();
	}
	
	public String getOid() {
		return oid.getText();
	}

	public void setOid(String oid) {
		this.oid.setText(oid);
		load();
	}

}
