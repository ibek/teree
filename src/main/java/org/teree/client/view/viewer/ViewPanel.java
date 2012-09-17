package org.teree.client.view.viewer;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class ViewPanel extends Composite {

	private HorizontalPanel container;
	private DropdownButton exportAs;
	private Button exportImage;
	private Button exportFreeMind;
	
	public ViewPanel() {
		
		container = new HorizontalPanel();
		
		exportAs = new DropdownButton("Export as...");

		exportImage = new Button("Image", IconType.PICTURE);
		exportAs.add(exportImage);

		exportFreeMind = new Button("FreeMind map");
		exportAs.add(exportFreeMind);
		
		container.add(exportAs);
		
		initWidget(container);
		
	}
	
	public HasClickHandlers getExportImageButton() {
		return exportImage;
	}
	
}
