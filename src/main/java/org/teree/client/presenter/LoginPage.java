package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.teree.client.Settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class LoginPage implements Presenter {

	public interface Display extends Template {
        //HasClickHandlers getGoogleButton();
        Widget asWidget();
        void fail();
    }
    
    @Inject @Named(value="eventBus")
    private HandlerManager eventBus;
    
    @Inject
    private Display display;
    
    public void bind() {
        
        /**display.getGoogleButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("/teree/oauth?callback=teree.html#explore"); // TODO: change to home
			}
		});*/
        
    }
    
    public void fail() {
    	display.fail();
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

}
