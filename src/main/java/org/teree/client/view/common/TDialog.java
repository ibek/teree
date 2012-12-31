package org.teree.client.view.common;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DialogBox;

public class TDialog extends DialogBox {

	public TDialog() {
		
		addStyleName("popover");
		getElement().getStyle().setDisplay(Display.BLOCK);
		getElement().getStyle().setPadding(0.0, Unit.PX);
		
		getCaption().asWidget().addStyleName("popover-title");
		getCaption().asWidget().getElement().getStyle().setMarginRight(-3.0, Unit.PX);
		setAutoHideEnabled(true);

	}
	
	@Override
	public void setTitle(String title) {
		setText(title);
	}

}
