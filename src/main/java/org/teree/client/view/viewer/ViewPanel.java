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
	private Button exportJSON;
	
	private org.teree.client.text.ViewPanel TEXT = org.teree.client.text.ViewPanel.LANG;
	
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
		
		exportAs = new DropdownButton(TEXT.export_as());

		exportImage = new Button(TEXT.image(), IconType.PICTURE);
		exportAs.add(exportImage);

		exportJSON = new Button("JSON");
		exportAs.add(exportJSON);

		exportFreeMind = new Button(TEXT.freemind_map());
		exportAs.add(exportFreeMind);
		
		container.add(exportAs);
		
		initWidget(container);
		
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
