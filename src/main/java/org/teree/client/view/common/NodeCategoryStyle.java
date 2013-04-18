package org.teree.client.view.common;

import org.teree.client.Settings;
import org.teree.shared.data.common.NodeCategory;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
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
		if (w instanceof Icon) {
			w = panel.getWidget(1);
		}
		Style wcss = w.getElement().getStyle();
		wcss.setBackgroundColor(nc.getBackground());

		if (nc.getIconType() != null) {

			Icon icon = null;
			if (panel.getWidget(0) instanceof Icon) {
				icon = (Icon)panel.getWidget(0);
			} else {
				icon = new Icon();
				icon.getElement().getStyle().setZIndex(100);

				panel.insert(icon, 0, 5, 0);
				w.getElement().getStyle()
						.setPaddingLeft(Settings.ICON_WIDTH, Unit.PX);
			}

			icon.setType(IconType.valueOf(nc.getIconType()));
		} else if (panel.getWidget(0) instanceof Icon) {
			panel.remove(0);
			w.getElement().getStyle().setPaddingLeft(0.0, Unit.PX);
		}
		
	}
	
}
