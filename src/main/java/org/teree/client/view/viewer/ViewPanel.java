package org.teree.client.view.viewer;

import org.teree.client.text.UIConstants;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class ViewPanel extends Composite {
	
	private static final String DOWNLOAD_TARGET = "download";

	private HorizontalPanel container;
	
    private Form form;
    private Hidden file;
    private Hidden fileName;
    private Hidden fileType;
    
	private DropdownButton exportAs;
	private NavLink exportImage;
	private NavLink exportFreeMind;
	private NavLink exportJSON;

	private Button share;
	private Button collapseAll;
	private boolean collapsed;
	
	private UIConstants UIC = UIConstants.LANG;
	
	public ViewPanel() {
		
		container = new HorizontalPanel();
		
		form = new Form();
		fileName = new Hidden("fileName");
		form.add(fileName);
		fileType = new Hidden("fileType");
		form.add(fileType);
		file = new Hidden("file");
		form.add(file);
		form.setAction(DOWNLOAD_TARGET);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		
		collapseAll = new Button();
		setCollapsed(true);
		container.add(collapseAll);
		
		Label space = new Label("");
		space.getElement().getStyle().setMarginRight(20, Unit.PX);
		container.add(space);
		
		exportAs = new DropdownButton(UIC.export_as());

		exportImage = new NavLink(UIC.image());
		exportImage.setIcon(IconType.PICTURE);
		exportAs.add(exportImage);

		exportJSON = new NavLink("JSON");
		exportAs.add(exportJSON);

		exportFreeMind = new NavLink(UIC.freemind_map());
		exportAs.add(exportFreeMind);
		
		container.add(exportAs);
		
		share = new Button(UIC.share());
		container.add(share);
		
		initWidget(container);
		
	}
	
	public boolean isCollapsed() {
		return collapsed;
	}
	
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
		if (collapsed) {
			collapseAll.setText(UIC.uncollapse_all());
		} else {
			collapseAll.setText(UIC.collapse_all());
		}
	}
	
	public Button getCollapseAllButton() {
		return collapseAll;
	}
	
	public HasClickHandlers getExportImageButton() {
		return exportImage;
	}
	
	public HasClickHandlers getExportFreeMindButton() {
		return exportFreeMind;
	}
	
	public HasClickHandlers getExportJSONButton() {
		return exportJSON;
	}
	
	public Button getShareButton() {
		return share;
	}
	
	public void sendDownloadRequest(String name, String dataUrl) {
		String type = dataUrl.substring(0, dataUrl.indexOf(';'));
		String base64Data = dataUrl.substring(dataUrl.lastIndexOf(',')+1);
		sendDownloadRequest(name, type, base64Data);
	}
	
	public void sendDownloadRequest(String name, String type, String data) {
		fileName.setValue(name);
		fileType.setValue(type);
		file.setValue(data);
		form.submit();
	}
	
}
