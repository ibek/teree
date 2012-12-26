package org.teree.client.view.viewer;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ShareDialog extends DialogBox {

	private static final int WIDTH = 210;
	private static final int HEIGHT = 200;
	
	private VerticalPanel panel;
	private Button okButton;
	private Button sa;
	private TextArea data;

	public ShareDialog() {
		
		addStyleName("popover");
		getElement().getStyle().setDisplay(Display.BLOCK);
		getElement().getStyle().setPadding(0.0, Unit.PX);
		
		setText("Share scheme");
		getCaption().asWidget().addStyleName("popover-title");
		getCaption().asWidget().getElement().getStyle().setMarginRight(2.0, Unit.PX);
		setAutoHideEnabled(true);

		panel = new VerticalPanel();
		panel.getElement().getStyle().setMargin(9.0, Unit.PX);
		panel.setWidth(WIDTH+"px");
		panel.setHeight(HEIGHT+"px");
		
		init();
		
		setWidget(panel);

	}
	
	private void init() {
		
		data = new TextArea();
		data.setReadOnly(true);
		data.setHeight("160px");
		data.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				data.selectAll();
			}
		});
		panel.add(data);
		
		okButton = new Button("Ok");
		okButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(okButton);
		panel.add(buttons);
		
	}
	
	@Override
	public int getOffsetWidth() {
		return WIDTH;
	}
	
	@Override
	public int getOffsetHeight() {
		return HEIGHT;
	}
	
	public void setOid(String oid) {
		data.setText("<a href=\""+GWT.getHostPageBaseURL()+"teree.html#view/oid="+oid+"\">\n<img src=\""+GWT.getHostPageBaseURL()+"share?oid="+oid+"\"/>\n</a>");
	}

}
