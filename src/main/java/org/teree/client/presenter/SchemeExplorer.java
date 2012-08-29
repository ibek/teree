package org.teree.client.presenter;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.Settings;
import org.teree.shared.GeneralService;
import org.teree.shared.data.Scheme;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class SchemeExplorer implements Presenter {

	public interface Display {
        HasClickHandlers getNewButton();
        HasClickHandlers getExploreLink();
        HasClickHandlers getHelpLink();
        Widget asWidget();
        void setData(List<Scheme> slist);
    }
	
	@Inject
	private Caller<GeneralService> generalService;
    
    @Inject
    private Display display;
	
	public void bind() {
		
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
		loadData();
        container.clear();
        container.add(display.asWidget());
	}
	
	private void loadData() {
		generalService.call(new RemoteCallback<List<Scheme>>() {
            @Override
            public void callback(List<Scheme> response) {
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
