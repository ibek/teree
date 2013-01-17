package org.teree.client.view.editor;

import java.util.Set;

import org.teree.client.text.UIConstants;
import org.teree.client.text.UIMessages;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.client.view.resource.icon.CustomIconType;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.SplitDropdownButton;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Display;
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
	private DropdownButton type;
	private NavLink mindmap;
	private NavLink hierarchicalHorizontal;
	private Button refresh;
	private Button createText;
	private Button createImg;
	private Button createLink;
	private Button createMathExpr;
	private SplitDropdownButton createConnector;
	private NavLink mergeConnector;
	private NavLink splitConnector;
	private Button bold;
	private DropdownButton icon;
	
	private SelectIcon iconHandler;
	
	private static final int ICON_COLUMNS = 7;	
	private static final int ICON_ROWS = 6;
	
	public EditPanel() {
		
		container = new HorizontalPanel();

		save = new Button(UIConstants.LANG.save(), IconType.SAVE);
		Label space = new Label("");
		space.getElement().getStyle().setMarginRight(20, Unit.PX);
		
		refresh = new Button("", IconType.REFRESH);
		Label space2 = new Label("");
		space2.getElement().getStyle().setMarginRight(20, Unit.PX);
		
		type = new DropdownButton(" ");
		type.getElement().getStyle().setDisplay(Display.INLINE);
		type.setBaseIcon(CustomIconType.mindmap);
		mindmap = new NavLink("MindMap");
		mindmap.setBaseIcon(CustomIconType.mindmap);
		mindmap.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				type.setBaseIcon(CustomIconType.mindmap);
			}
		});
		type.add(mindmap);
		hierarchicalHorizontal = new NavLink("Hierarchical Horizontal Tree");
		hierarchicalHorizontal.setBaseIcon(CustomIconType.hierarchicalhorizontal);
		hierarchicalHorizontal.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				type.setBaseIcon(CustomIconType.hierarchicalhorizontal);
			}
		});
		type.add(hierarchicalHorizontal);
		
		createText = new Button("", IconType.PLUS);
		createImg = new Button("", IconType.PICTURE);
		createLink = new Button("", IconType.LINK);
		createMathExpr = new Button("Σ");
		createConnector = new SplitDropdownButton("");
		mergeConnector = new NavLink("Merge connector");
		splitConnector = new NavLink("Split node and connect");
		createConnector.add(mergeConnector);
		createConnector.add(splitConnector);
		
		Label space3 = new Label("");
		space3.getElement().getStyle().setMarginRight(20, Unit.PX);
		
		createConnector.setBaseIcon(CustomIconType.connector);

		Label space4 = new Label("");
		space4.getElement().getStyle().setMarginRight(20, Unit.PX);
		
		bold = new Button("", IconType.BOLD);
		
		icon = new DropdownButton("icon");
		loadIcons();

		container.add(save);
		container.add(space);
		
		container.add(type);
		
		Tooltip tre = new Tooltip(UIConstants.LANG.refresh_scheme());
		tre.add(refresh);
		container.add(tre);
		
		container.add(space2);
		
		Tooltip tct = new Tooltip(UIMessages.LANG.create_text());
		tct.add(createText);
		container.add(tct);

        Tooltip tci = new Tooltip(UIMessages.LANG.create_image());
        tci.add(createImg);
		container.add(tci);

        Tooltip tcl = new Tooltip(UIMessages.LANG.create_link());
        tcl.add(createLink);
		container.add(tcl);

        Tooltip tcme = new Tooltip(UIMessages.LANG.create_math());
        tcme.add(createMathExpr);
		container.add(tcme);
		
		container.add(space3);

        Tooltip tccon = new Tooltip("Create connector");
        tccon.add(createConnector);
		container.add(tccon);
		
		// TODO: add here the split button
		
		container.add(space4);

        Tooltip tcb = new Tooltip(UIMessages.LANG.bold_text());
        tcb.add(bold);
		container.add(tcb);
		
        Tooltip ti = new Tooltip(UIMessages.LANG.choose_icon());
        ti.add(icon);
		container.add(ti);
		
		initWidget(container);
		
		type.setHeight(save.getOffsetHeight()+"px");
		
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
			NavLink ib = new NavLink();
			ib.setIcon(ic);
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
	
	public void checkSelectedNode(NodeWidget nw) {
		mergeConnector.setDisabled(nw == null || !(nw instanceof ConnectorNodeWidget));
		splitConnector.setDisabled(nw == null || !(nw instanceof TextNodeWidget) || nw instanceof ConnectorNodeWidget || 
				!(nw.getNode().getChildNodes() != null && nw.getNode().getChildNodes().size() > 0));
		bold.setEnabled(nw != null && ( 
				(nw instanceof TextNodeWidget && !(nw instanceof ConnectorNodeWidget)) || nw instanceof LinkNodeWidget));
	}
	
	public HasClickHandlers getSaveButton() {
		return save;
	}
	
	public HasClickHandlers getMindMapButton() {
		return mindmap;
	}
	
	public HasClickHandlers getHierarchicalHorizontalButton() {
		return hierarchicalHorizontal;
	}
	
	public HasClickHandlers getRefreshButton() {
		return refresh;
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
	
	public HasClickHandlers getCreateConnectorButton() {
		return createConnector;
	}
	
	public HasClickHandlers getMergeConnectorButton() {
		return mergeConnector;
	}
	
	public HasClickHandlers getSplitConnectorButton() {
		return splitConnector;
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
