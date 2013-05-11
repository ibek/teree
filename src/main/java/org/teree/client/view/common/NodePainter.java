package org.teree.client.view.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;

import org.teree.client.Settings;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.shared.data.common.NodeCategory;

import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.dom.client.ImageElement;

public class NodePainter {

	private static String font;

	public static void drawTextNode(Context2d context, int x, int y,
			String text, boolean collapsed, int width,
			NodeCategory category) {

		context.save();
		setContextStyle(context, category);

		String[] words = text.split(" ");
		String line = "";
		int lineHeight = 14;
		int mw = width + 10;
		int py = y;
		List<Integer> ly = new ArrayList<Integer>();
		List<String> ls = new ArrayList<String>();
		for (int n = 0; n < words.length; ++n) {
			String[] lines = words[n].split("\n");
			if (lines.length == 1) {
				String testLine = line + words[n] + " ";
				TextMetrics tm = context.measureText(testLine);
				double testWidth = tm.getWidth();
				if (testWidth > mw) {
					ls.add(line);
					ly.add(py);
					line = words[n] + ' ';
					py += lineHeight;
				} else {
					line = testLine;
				}
			} else {
				for (int i = 0; i < lines.length - 1; ++i) {
					ls.add(line + lines[i]);
					ly.add(py);
					line = "";
					py += lineHeight;
				}
				line = lines[lines.length - 1];
			}
		}
		ls.add(line);
		ly.add(py);
		int h = ly.size() * lineHeight;
		int m = h - lineHeight;

		drawBackground(context, category, x, y - h + 2, width, h + 2);

		String icon = category.getIconType();
		if (icon != null) {
			x += Settings.ICON_WIDTH;
		}

		for (int i = 0; i < ly.size(); ++i) {
			text = ls.get(i);
			if (collapsed && i == 0 && !text.startsWith("+")) {
				text = "+" + text;
				x -= context.measureText("+").getWidth();
			}
			context.fillText(text, x, ly.get(i) - m);
		}

		if (icon != null) {
			context.setFont("14px FontAwesome");
			String c = "";
			c += IconTypeContent.get(IconType.valueOf(icon));
			context.fillText(c, x - Settings.ICON_WIDTH, y - m);
			context.setFont(font);
		}

		context.restore();
	}

	public static void drawImageNode(Context2d context, int x, int y,
			ImageElement ielement, int width, int height, NodeCategory category) {

		context.save();
		setContextStyle(context, category);
		drawBackground(context, category, x, y - height + 2, width, height + 2);
		
		String icon = category.getIconType();
		if (icon != null) {
			x += Settings.ICON_WIDTH;
		}
		context.drawImage(ielement, x, y - height);
		
		if (icon != null) {
			context.setFont("14px FontAwesome");
			String c = "";
			c += IconTypeContent.get(IconType.valueOf(icon));
			context.fillText(c, x - Settings.ICON_WIDTH, y - height + Settings.ICON_WIDTH);
			context.setFont(font);
		}
		context.restore();
	}

	public static void drawSingleLine(Context2d context, int x, int y,
			String text, int width, int height, NodeCategory category) {

		context.save();
		setContextStyle(context, category);
		drawBackground(context, category, x, y - height + 2, width, height + 2);
		
		String icon = category.getIconType();
		if (icon != null) {
			x += Settings.ICON_WIDTH;
		}
		context.fillText(text, x, y);
		
		if (icon != null) {
			context.setFont("14px FontAwesome");
			String c = "";
			c += IconTypeContent.get(IconType.valueOf(icon));
			context.fillText(c, x - Settings.ICON_WIDTH, y);
			context.setFont(font);
		}
		context.restore();
	}

	public static void drawPercentNode(Context2d context, int x, int y,
			String text, int percent, boolean collapsed, int pwidth,
			int pheight, int width, int height, NodeCategory category) {

		context.save();
		setContextStyle(context, category);
		drawBackground(context, category, x, y - height + 2, width,
				height + 2);
		int ox = x;

		String icon = category.getIconType();
		if (icon != null) {
			x += Settings.ICON_WIDTH;
		}
		
		int px = x;
		y -= pheight + 5;

		if (collapsed && !text.startsWith("+")) {
			text = "+" + text;
			px -= context.measureText("+").getWidth();
		}
		context.fillText(text, px, y);

		y += 5;

		context.setFillStyle(CssColor.make("#08C"));
		context.fillRect(ox, y, percent / 100.0 * pwidth, pheight);

		context.setFillStyle("#FFFFFF");
		context.fillText(String.valueOf(percent) + "%", ox + 10, y + pheight);
		
		if (icon != null) {
			context.setFont("14px FontAwesome");
			String c = "";
			c += IconTypeContent.get(IconType.valueOf(icon));
			context.fillText(c, ox, y - 5);
			context.setFont(font);
		}

		context.restore();
	}

	private static void setContextStyle(Context2d context, NodeCategory category) {
		if (category.isBold()) {
			font = "bold 14px monospace";
		} else {
			font = "14px monospace";
		}
		context.setFont(font);
		context.setFillStyle(category.getColor());
		context.setGlobalAlpha(category.getTransparency() / 100.0);
	}

	private static void drawBackground(Context2d context,
			NodeCategory category, int x, int y, int w, int h) {
		context.setFillStyle(category.getBackground());
		context.fillRect(x, y, w, h); // background
		context.setFillStyle(category.getColor());
	}

}
