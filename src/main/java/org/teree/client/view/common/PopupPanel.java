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
