package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.teree.client.Text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
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
				String path = Window.Location.getPath();
				int ls = path.lastIndexOf('/');
				if (ls > 0) {
					path = path.substring(0, ls);
				}
				System.out.println(path);
				Window.Location.replace(path+"/oauth");
			}
		});
        
    }
    
    public void fail() {
    	display.error(Text.LANG.loginFailed());
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
