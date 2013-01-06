package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.event.RefreshUserInfo;
import org.teree.shared.UserService;
import org.teree.shared.data.UserInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class SettingsPage implements Presenter {

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
    
    @Inject @Named(value="eventBus")
    private HandlerManager eventBus;
	
	@Inject
	private Caller<UserService> userService;
    
    @Inject
    private Display display;
    
    public void bind() {
    	display.getProfileUpdateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UserInfo ui = display.getProfileDetails();
				if (ui != null) {
					userService.call(new RemoteCallback<Void>() {
						@Override
						public void callback(Void response) {
							display.info("Profile has been updated");
							eventBus.fireEvent(new RefreshUserInfo());
						}
					}, new ErrorCallback() {
						@Override
						public boolean error(Message message, Throwable throwable) {
							display.error(throwable.getMessage());
							return false;
						}
					}).update(ui);
				}
			}
		});
    	
    	display.getChangePasswordButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!display.validateChangePassword()) {
					userService.call(new RemoteCallback<Void>() {
						@Override
						public void callback(Void response) {
							display.info("Password has been changed");
						}
					}, new ErrorCallback() {
						@Override
						public boolean error(Message message, Throwable throwable) {
							display.error(throwable.getMessage());
							return false;
						}
					}).updatePassword(display.getOldPassword(), display.getNewPassword());
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
