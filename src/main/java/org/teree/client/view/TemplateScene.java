package org.teree.client.view;

import org.teree.client.CurrentUser;
import org.teree.client.presenter.Template;
import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;

public abstract class TemplateScene extends Composite implements Template {
	
	@UiField(provided = true)
	Header header;
    
    @UiField
    Alert status;
	
	public TemplateScene() {
		header = new Header();
	}

	@Override
	public void setCurrentUser(CurrentUser user) {
		header.setCurrentUser(user);
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
