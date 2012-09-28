package org.teree.client.view.editor.storage;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.Form.SubmitCompleteHandler;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Form.SubmitHandler;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.SubmitButton;
import com.github.gwtbootstrap.client.ui.Form.SubmitCompleteEvent;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;

public class FileUpload extends Composite {
	
	private static final String UPLOAD_TARGET = "upload";

	private FlowPanel container;
	
	private Form form;
	private com.github.gwtbootstrap.client.ui.FileUpload file;
	private Label info;
	private Button upload;
	
	private BrowserRefreshRequestHandler handler;
	
	public FileUpload() {
		
		container = new FlowPanel();
		
		form = new Form();
		form.setAction(UPLOAD_TARGET);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		
		file = new com.github.gwtbootstrap.client.ui.FileUpload();
		file.setName("file");
		form.add(file);
		
		info = new Label();
		info.setVisible(false);
		form.add(info);
		
		upload = new Button("Upload", IconType.UPLOAD_ALT);
		form.add(upload);
		
		container.add(form);
		
		initWidget(container);
		
		bind();
		
	}
	
	public void setBrowserRefreshRequestHandler(BrowserRefreshRequestHandler handler) {
		this.handler = handler;
	}
	
	private void bind() {
		
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(final SubmitCompleteEvent event) {
				
				if (event.getResults() == null || event.getResults().isEmpty()) {
					info.setText("uploaded");
				} else {
					info.setText("exceeds maximum size 1MB");
				}
				info.setVisible(true);
				
				Timer t = new Timer() {
		            @Override
		            public void run() {
		            	info.setText("");
		            	info.setVisible(false);
		            }
		        };
		        t.schedule(5000);
		        
		        handler.refresh();
		        
			}
		});
		
		form.addSubmitHandler(new SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				if (file.getFilename() == null || file.getFilename().isEmpty()) {
					event.cancel();
				}
			}
		});
		
		upload.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				form.submit();
			}
		});
		
	}
	
}
