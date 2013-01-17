package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class HelpPage implements Presenter {

	public interface Display extends Template {
        Widget asWidget();
    }
    
    @Inject
    private Display display;
    
    public void bind() {
        
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
		return "Help";
	}

}
