package org.teree.client.view.editor;

import java.util.Set;

import org.teree.client.view.resource.IconTypeContent;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class EditPanel extends Composite {

	private HorizontalPanel container;
	private Button save;
	private Button createText;
	private Button createImg;
	private Button createLink;
	private Button createMathExpr;
	private Button bold;
	private DropdownButton icon;
	
	private SelectIcon iconHandler;
	
	private static final int ICON_COLUMNS = 7;	
	private static final int ICON_ROWS = 6;
	
	
	public EditPanel() {
		
		container = new HorizontalPanel();

		save = new Button("Save", IconType.SAVE);
		Label space = new Label("");
		space.getElement().getStyle().setMarginRight(20, Unit.PX);
		createText = new Button("", IconType.PLUS);
		createImg = new Button("", IconType.PICTURE);
		createLink = new Button("", IconType.LINK);
		createMathExpr = new Button("", IconType.PENCIL);
		Label space2 = new Label("");
		space2.getElement().getStyle().setMarginRight(20, Unit.PX);
		bold = new Button("", IconType.BOLD);
		
		icon = new DropdownButton("icon");
		icon.getElement().getStyle().setZIndex(-100);
		loadIcons();

		container.add(save);
		container.add(space);
		
		Tooltip tct = new Tooltip("Create text child node");
		tct.add(createText);
		container.add(tct);

        Tooltip tci = new Tooltip("Create image child node");
        tci.add(createImg);
		container.add(tci);

        Tooltip tcl = new Tooltip("Create link child node");
        tcl.add(createLink);
		container.add(tcl);

        Tooltip tcme = new Tooltip("Create math expression child node");
        tcme.add(createMathExpr);
		container.add(tcme);
		
		container.add(space2);

        Tooltip tcb = new Tooltip("Bold text node");
        tcb.add(bold);
		container.add(tcb);
		
        Tooltip ti = new Tooltip("Choose icon for node");
        ti.add(icon);
		container.add(ti);
		
		initWidget(container);
		
	}
	
	private void loadIcons() {
		Grid g = new Grid(ICON_ROWS, ICON_COLUMNS);
		Set<IconType> is = IconTypeContent.ICONS.keySet();
		IconType[] icons = new IconType[is.size()];
		icons = is.toArray(icons);
		for (int i=0; i<icons.length; ++i) {
			if (i/ICON_COLUMNS > ICON_ROWS-1) {
				break;
			}
			final IconType ic = icons[i];
			Button ib = new Button("", ic);
			ib.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					selectIcon(ic);
				}
			});
			g.setWidget(i/ICON_COLUMNS, i%ICON_COLUMNS, ib);
		}
		icon.add(g);
	}
	
	private void selectIcon(IconType icon) {
		iconHandler.select(icon);
	}
	
	public HasClickHandlers getSaveButton() {
		return save;
	}
	
	public HasClickHandlers getCreateTextButton() {
		return createText;
	}
	
	public HasClickHandlers getCreateImgButton() {
		return createImg;
	}
	
	public HasClickHandlers getCreateLinkButton() {
		return createLink;
	}
	
	public HasClickHandlers getCreateMathExprButton() {
		return createMathExpr;
	}
	
	public HasClickHandlers getBoldButton() {
		return bold;
	}
	
	public void setSelectIconHandler(SelectIcon handler) {
		this.iconHandler = handler;
	}
	
	public static interface SelectIcon {
		
		public void select(IconType icon);
		
	}
	
}
