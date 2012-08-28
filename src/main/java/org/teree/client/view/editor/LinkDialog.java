package org.teree.client.view.editor;

import org.teree.client.view.editor.ImageNodeWidget.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LinkDialog extends DialogBox {
	
	interface Resources extends ClientBundle {
		@Source("../resource/dialogStyle.css")
		DialogStyle css();
		
		public interface DialogStyle extends CssResource {
            String dialog();
        }
		
	}

	private Resources res = GWT.create(Resources.class);

	private static final int WIDTH = 180;
	private static final int HEIGHT = 50;
	
	private Button ok;
	
	private TextBox url;

	public LinkDialog(String title) {
		res.css().ensureInjected();
		addStyleName(res.css().dialog());
		
		setText(title);
		setAutoHideEnabled(true);

		VerticalPanel panel = new VerticalPanel();
		panel.setWidth(WIDTH+"px");
		panel.setHeight(HEIGHT+"px");
		
		url = new TextBox();
		
		panel.add(url);
		
		ok = new Button("Ok");
		
		Button cancel = new Button("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LinkDialog.this.hide();
			}
		});
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(ok);
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
		return ok;
	}
	
	public String getUrl() {
		return url.getText();
	}

}
