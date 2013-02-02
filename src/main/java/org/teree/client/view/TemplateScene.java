package org.teree.client.view;

import org.teree.client.CurrentPresenter;
import org.teree.client.CurrentUser;
import org.teree.client.presenter.Presenter;
import org.teree.client.presenter.Template;
import org.teree.client.view.resource.PageStyle;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;

public abstract class TemplateScene extends Composite implements Template {
	
	@UiField
	Header header;
    
    @UiField
    Alert status;
    
	static {
		PageStyle.INSTANCE.css().ensureInjected();
	}
	
	public TemplateScene() {
		
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
