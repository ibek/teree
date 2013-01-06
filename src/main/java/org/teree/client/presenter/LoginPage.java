package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.teree.client.text.UIMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class LoginPage implements Presenter {

	public interface Display extends Template {
        HasClickHandlers getGoogleButton();
        Widget asWidget();
    }
    
    @Inject
    private Display display;
    
    public void bind() {
        
        display.getGoogleButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(GWT.getHostPageBaseURL()+"/oauth?callback=teree.html#home");
			}
		});
        
    }
    
    public void fail() {
    	display.error(UIMessages.LANG.loginFailed());
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
		return "Login";
	}

}
