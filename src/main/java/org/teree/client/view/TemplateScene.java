package org.teree.client.view;

import org.teree.client.CurrentPresenter;
import org.teree.client.CurrentUser;
import org.teree.client.presenter.Presenter;
import org.teree.client.presenter.Template;
import org.teree.client.view.resource.PageStyle;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;

public abstract class TemplateScene extends Composite implements Template {
	
	private static final String DOWNLOAD_TARGET = "download";
	
	@UiField
	Header header;
    
    @UiField
    Alert status;
	
    private Form form;
    private Hidden file;
    private Hidden fileName;
    private Hidden fileType;
    
	static {
		PageStyle.INSTANCE.css().ensureInjected();
	}
	
	public TemplateScene() {
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
	}

	@Override
	public void setCurrentUser(CurrentUser user) {
		header.setCurrentUser(user);
		
		Presenter cp = CurrentPresenter.getInstance().getPresenter();
		if (cp != null) {
			Window.setTitle("teree - " + cp.getTitle());
		}
	}

	@Override
	public void info(String msg) {
		status.setType(AlertType.INFO);
		setStatus(msg);
	}

	@Override
	public void error(String msg) {
		status.setType(AlertType.ERROR);
		setStatus(msg);
	}
	
	@Override
	public void sendDownloadRequest(String name, String dataUrl) {
		String type = dataUrl.substring(0, dataUrl.indexOf(';'));
		String base64Data = dataUrl.substring(dataUrl.lastIndexOf(',')+1);
		sendDownloadRequest(name, type, base64Data);
	}
	
	@Override
	public void sendDownloadRequest(String name, String type, String data) {
		fileName.setValue(name);
		fileType.setValue(type);
		file.setValue(data);
		form.submit();
	}
	
	private void setStatus(String msg) {
		status.getElement().getStyle().setZIndex(100); // in front of all elements
		status.setText(msg);
		status.setVisible(true);
		Timer t = new Timer() {
            @Override
            public void run() {
            	status.setVisible(false);
            }
        };
        t.schedule(5000);
	}
	
}
