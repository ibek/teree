package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.teree.client.event.NodeReceived;
import org.teree.client.event.NodeReceivedHandler;
import org.teree.client.event.SelectNode;
import org.teree.client.event.SelectNodeHandler;
import org.teree.client.view.editor.NodeWidget;
import org.teree.shared.data.Node;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class MapView implements Presenter {

    public interface Display {
        Widget asWidget();
        void setRoot(Node root);
    }
    
    @Inject
    private HandlerManager eventBus;
    
    @Inject
    private Display display;
    
    public void bind() {
    	
        eventBus.addHandler(NodeReceived.TYPE, new NodeReceivedHandler() {
			@Override
			public void received(NodeReceived event, Node root) {
				display.setRoot(root);
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
