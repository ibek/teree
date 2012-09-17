package org.teree.client.view.editor;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class EditPanel extends Composite {

	private HorizontalPanel container;
	private Button save;
	private Button createText;
	private Button createImg;
	private Button createLink;
	private Button bold;
	
	public EditPanel() {
		
		container = new HorizontalPanel();

		save = new Button("Save", IconType.SAVE);
		Label space = new Label("");
		space.getElement().getStyle().setMarginRight(20, Unit.PX);
		createText = new Button("", IconType.PLUS);
		createImg = new Button("", IconType.PICTURE);
		createLink = new Button("", IconType.LINK);
		Label space2 = new Label("");
		space2.getElement().getStyle().setMarginRight(20, Unit.PX);
		bold = new Button("", IconType.BOLD);

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
		
		container.add(space2);
		
        Tooltip tcb = new Tooltip("Bold text node");
        tcb.add(bold);
		container.add(tcb);
		
		initWidget(container);
		
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
	
	public HasClickHandlers getBoldButton() {
		return bold;
	}
	
}
