package org.teree.client.view.common;

import org.teree.shared.data.common.NodeCategory;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.Widget;

public class NodeCategoryStyle {

	public static void set(Widget w, NodeCategory nc) {
		if (nc == null || nc.getName() == null) {
			return;
		}
		Boolean bold = nc.isBold();
		System.out.println(nc.getName() + " " + bold);
		if (bold != null) {
			FontWeight fw = (bold)?FontWeight.BOLD:FontWeight.NORMAL;
			w.getElement().getStyle().setFontWeight(fw);
		}
	}
	
}
