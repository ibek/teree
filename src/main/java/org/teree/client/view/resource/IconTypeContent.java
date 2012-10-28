package org.teree.client.view.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.gwtbootstrap.client.ui.constants.IconType;

public class IconTypeContent {

	public static final Map<IconType, Character> ICONS = new LinkedHashMap<IconType, Character>();

	static {
		setIcons();
	}
	
	private static void setIcons() {
		ICONS.put(IconType.SIGN_BLANK, (char)0xf0c8);
		ICONS.put(IconType.ASTERISK, (char)0xf069);
		ICONS.put(IconType.BAN_CIRCLE, (char)61640);
		ICONS.put(IconType.BELL, (char)0xf0a2);
		ICONS.put(IconType.BOLT, (char)0xf0e7);
		ICONS.put(IconType.BOOK, (char)0xf02d);
		ICONS.put(IconType.BOOKMARK, (char)0xf02e);
		ICONS.put(IconType.CALENDAR, (char)0xf073);
		ICONS.put(IconType.CHECK, (char)0xf046);
		ICONS.put(IconType.CHECK_EMPTY, (char)0xf096);
		ICONS.put(IconType.COMMENT, (char)0xf075);
		ICONS.put(IconType.EDIT, (char)0xf044);
		ICONS.put(IconType.ENVELOPE, (char)0xf003);
		ICONS.put(IconType.FIRE, (char)0xf06d);
		ICONS.put(IconType.FLAG, (char)0xf024);
		ICONS.put(IconType.GLOBE, (char)0xf0ac);
		ICONS.put(IconType.INFO_SIGN, (char)0xf05a);
		ICONS.put(IconType.KEY, (char)0xf084);
		ICONS.put(IconType.LEAF, (char)0xf06c);
		ICONS.put(IconType.LOCK, (char)0xf023);
		ICONS.put(IconType.UNLOCK, (char)0xf09c);
		ICONS.put(IconType.MINUS, (char)0xf068);
		ICONS.put(IconType.OK, (char)0xf00c);
		ICONS.put(IconType.PICTURE, (char)0xf03e);
		ICONS.put(IconType.PLANE, (char)0xf072);
		ICONS.put(IconType.PLUS, (char)0xf067);
		ICONS.put(IconType.PRINT, (char)0xf02f);
		ICONS.put(IconType.PUSHPIN, (char)0xf08d);
		ICONS.put(IconType.REMOVE, (char)0xf00d);
		ICONS.put(IconType.SEARCH, (char)0xf002);
		ICONS.put(IconType.SHOPPING_CART, (char)0xf07a);
		ICONS.put(IconType.STAR, (char)0xf005);
		ICONS.put(IconType.TAG, (char)0xf02b);
		ICONS.put(IconType.THUMBS_UP, (char)0xf087);
		ICONS.put(IconType.THUMBS_DOWN, (char)0xf088);
		ICONS.put(IconType.TIME, (char)0xf017);
		ICONS.put(IconType.TRUCK, (char)0xf0d1);
		ICONS.put(IconType.WARNING_SIGN, (char)0xf071);
		ICONS.put(IconType.WRENCH, (char)0xf0ad);
		ICONS.put(IconType.CHEVRON_LEFT, (char)0xf053);
		ICONS.put(IconType.CHEVRON_RIGHT, (char)0xf054);
		ICONS.put(IconType.PHONE, (char)0xf095);
	}

	public static char get(IconType icon) {
		if (ICONS.isEmpty()) {
			setIcons();
		}
		return ICONS.get(icon);
	}
	
}
