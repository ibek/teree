package org.teree.client.view.editor;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class EditPanel extends Composite {

	private FlowPanel container;

	private Button createText;
	
	private Button createImg;
	
	private Button createLink;
	
	private Button bold;
	
	public EditPanel() {
		
		container = new FlowPanel();

		createText = new Button("+");
		createImg = new Button("%");
		createLink = new Button("@");
		bold = new Button("B");

		container.add(createText);
		container.add(createImg);
		container.add(createLink);
		container.add(bold);
		
		initWidget(container);
		
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
