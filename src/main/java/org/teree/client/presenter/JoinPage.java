package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.Settings;
import org.teree.shared.UserService;
import org.teree.shared.data.UserInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class JoinPage implements Presenter {

	public interface Display extends Template {
        HasClickHandlers getGoogleButton();
        HasClickHandlers getRegisterButton();
        Widget asWidget();
        boolean validate();
        UserInfo getUserInfo();
        String getPassword();
    }
	
	@Inject
	private Caller<UserService> userService;
    
    @Inject
    private Display display;
    
    public void bind() {
    	display.getRegisterButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (display.validate()) {
					userService.call(new RemoteCallback<Boolean>() {
						@Override
						public void callback(Boolean response) {
							if (response) {
								History.newItem(Settings.LOGIN_LINK);
							} else {
								display.error("Cannot create user, username and email must be unique.");
							}
						}
					}, new ErrorCallback() {
						@Override
						public boolean error(Message message, Throwable throwable) {
							display.error(throwable.getMessage());
							return false;
						}
					}).register(display.getUserInfo(), display.getPassword());
				}
			}
		});
    	
    	display.getGoogleButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(GWT.getHostPageBaseURL()+"/oauth");
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
		return "Join";
	}

}
