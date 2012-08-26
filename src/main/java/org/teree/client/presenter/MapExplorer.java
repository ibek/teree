package org.teree.client.presenter;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.shared.MapService;
import org.teree.shared.data.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class MapExplorer implements Presenter {

	public interface Display {
        HasClickHandlers getNewButton();
        HasClickHandlers getExploreLink();
        HasClickHandlers getHelpLink();
        Widget asWidget();
        void setData(List<Map> maps);
    }
	
	@Inject
	private Caller<MapService> mapService;
    
    @Inject
    private Display display;
	
	public void bind() {
		
        display.getNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("create");
			}
		});
        
        display.getExploreLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("explore");
			}
		});
	}
	
	@Override
	public void go(HasWidgets container) {
		bind();
		loadData();
        container.clear();
        container.add(display.asWidget());
	}
	
	private void loadData() {
		mapService.call(new RemoteCallback<List<Map>>() {
            @Override
            public void callback(List<Map> response) {
                display.setData(response);
            }
        }, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				// TODO inform user about the error - show 404 page
				return false;
			}
		}).getAll();
	}

}
