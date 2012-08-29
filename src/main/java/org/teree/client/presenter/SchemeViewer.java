package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.teree.client.Settings;
import org.teree.client.Text;
import org.teree.client.event.SchemeReceived;
import org.teree.client.event.SchemeReceivedHandler;
import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.editor.event.SelectNodeHandler;
import org.teree.shared.data.Node;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class SchemeViewer implements Presenter {

    public interface Display {
        HasClickHandlers getNewButton();
        HasClickHandlers getExploreLink();
        HasClickHandlers getHelpLink();
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
		
        display.getNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.CREATE_LINK);
			}
		});
        
        display.getExploreLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.EXPLORE_LINK);
			}
		});
        
    }
    
    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
    }

}
