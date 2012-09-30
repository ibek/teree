package org.teree.client.view.editor.storage;

import org.teree.client.view.editor.storage.event.BrowserRefreshRequestHandler;
import org.teree.shared.data.UserInfo;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.Form.SubmitCompleteHandler;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Form.SubmitHandler;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.Form.SubmitCompleteEvent;
import com.github.gwtbootstrap.client.ui.base.ProgressBarBase.Color;
import com.github.gwtbootstrap.client.ui.constants.FormType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;

public class FileUpload extends Composite {
	
	private static final String UPLOAD_TARGET = "upload";

	private FlowPanel container;
	
	private ProgressBar memory;
	
	private Form form;
	private com.github.gwtbootstrap.client.ui.FileUpload file;
	private Label info;
	private Button upload;
	
	private BrowserRefreshRequestHandler handler;
	
	public FileUpload() {
		
		container = new FlowPanel();
		
		memory = new ProgressBar();
		memory.setColor(Color.DEFAULT);
		container.add(memory);
		
		form = new Form();
		form.setType(FormType.INLINE);
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
		upload.getElement().getStyle().setFloat(Style.Float.RIGHT);
		form.add(upload);
		
		container.add(form);
		
		initWidget(container);
		
		bind();
		
	}
	
	private void bind() {
		
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(final SubmitCompleteEvent event) {
				
				boolean uploaded;
				if (uploaded = (event.getResults() == null || event.getResults().isEmpty())) {
					info.setText("uploaded");
				} else {
					info.setText("exceeds maximum file size or storage limit");
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
		        
		        if (uploaded) {
		        	handler.refresh();
		        }
		        
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
	
	public void setBrowserRefreshRequestHandler(BrowserRefreshRequestHandler handler) {
		this.handler = handler;
	}
	
	public void setUserInfo(UserInfo ui) {
		if (ui == null || ui.getUserPackage() == null) {
			return;
		}
		double toMB = 1024*1024;
		memory.setPercent((int)(((double)ui.getMemUsed()/ui.getUserPackage().getMemLimit())*100));
		memory.setText(""+Math.round(ui.getMemUsed()/toMB*100)/100.0+"MB / "+Math.round(ui.getUserPackage().getMemLimit()/toMB*100)/100.0+"MB");
	}
	
}
