package org.teree.client.view.common;

import org.teree.shared.data.common.NodeCategory;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class NodeCategoryStyle {

	public static void set(AbsolutePanel panel, NodeCategory nc) {
		if (nc == null) {
			return;
		}
		Style css = panel.getElement().getStyle();
		Boolean bold = nc.isBold();
		if (bold != null) {
			FontWeight fw = (bold)?FontWeight.BOLD:FontWeight.NORMAL;
			css.setFontWeight(fw);
		}
		css.setProperty("opacity", String.valueOf(nc.getTransparency()/100.0));
		css.setColor(nc.getColor());
		
		Widget w = panel.getWidget(0);
		Style wcss = w.getElement().getStyle();
		wcss.setBackgroundColor(nc.getBackground());
	}
	
}
