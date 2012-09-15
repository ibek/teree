package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.teree.client.Text;
import org.teree.client.event.SchemeReceived;
import org.teree.client.event.SchemeReceivedHandler;
import org.teree.shared.data.Node;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class SchemeViewer implements Presenter {

    public interface Display extends Template {
        Widget asWidget();
        void setRoot(Node root);
        void info(String msg);
    }
    
    @Inject @Named(value="eventBus")
    private HandlerManager eventBus;
    
    @Inject
    private Display display;
    
    public void bind() {
    	
        eventBus.addHandler(SchemeReceived.TYPE, new SchemeReceivedHandler() {
			@Override
			public void received(SchemeReceived event) {
				display.setRoot(event.getScheme().getRoot());
				display.info(Text.LANG.schemeReceived(event.getScheme().getOid()));
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

}
