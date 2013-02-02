package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.teree.shared.data.UserInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class SettingsPage extends Presenter {

	public interface Display extends Template {
        Widget asWidget();
        UserInfo getProfileDetails();
        boolean validateChangePassword();
        String getOldPassword();
        String getNewPassword();
        HasClickHandlers getProfileUpdateButton();
        HasClickHandlers getChangePasswordButton();
        HasClickHandlers getDeleteAccountButton();
    }
    
    @Inject
    private Display display;
    
    public void bind() {
    	display.getProfileUpdateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UserInfo ui = display.getProfileDetails();
				if (ui != null) {
					updateUser(ui);
				}
			}
		});
    	
    	display.getChangePasswordButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!display.validateChangePassword()) {
					updatePassword(display.getOldPassword(), display.getNewPassword());
				}
			}
		});
    	
    }
    
    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
    }

	@Override
	public Template getTemplate() {
		return display;
	}

	@Override
	public String getTitle() {
		return "Settings";
	}

}
