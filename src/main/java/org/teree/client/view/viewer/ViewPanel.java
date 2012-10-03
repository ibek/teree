package org.teree.client.view.viewer;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ViewPanel extends Composite {
	
	private static final String DOWNLOAD_TARGET = "download";

	private HorizontalPanel container;
	
    private Form form;
    private Hidden file;
    private Hidden fileName;
    private Hidden fileType;
    
	private DropdownButton exportAs;
	private Button exportImage;
	private Button exportFreeMind;
	
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
	
	public void sendDownloadRequest(String name, String dataUrl) {
		String type = dataUrl.substring(0, dataUrl.indexOf(';'));
		String base64Data = dataUrl.substring(dataUrl.lastIndexOf(',')+1);
		sendDownloadRequest(name, type, base64Data);
	}
	
	public void sendDownloadRequest(String name, String type, String base64Data) {
		fileName.setValue(name);
		fileType.setValue(type);
		file.setValue(base64Data);
		form.submit();
	}
	
}
