package org.teree.client.view.common;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DialogBox;

public class TDialog extends DialogBox {

	public TDialog() {
		
		addStyleName("popover");
		getElement().getStyle().setDisplay(Display.BLOCK);
		getElement().getStyle().setPadding(0.0, Unit.PX);
		getElement().getStyle().setProperty("width", "auto");
		
		getCaption().asWidget().addStyleName("popover-title");
		setAutoHideEnabled(true);

	}
	
	@Override
	public void setTitle(String title) {
		setText(title);
	}

}
