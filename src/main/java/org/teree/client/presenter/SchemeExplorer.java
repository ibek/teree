package org.teree.client.presenter;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.Text;
import org.teree.shared.GeneralService;
import org.teree.shared.data.Scheme;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class SchemeExplorer implements Presenter {

	public interface Display extends Template {
        Widget asWidget();
        void setData(List<Scheme> slist);
        HasClickHandlers getNextButton();
        HasClickHandlers getPreviousButton();
        String getFirstOid();
        String getLastOid();
    }
	
	@Inject
	private Caller<GeneralService> generalService;
    
    @Inject
    private Display display;
	
	public void bind() {
		display.getNextButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String from = display.getLastOid();
				if (from != null) {
					loadData(from);
				}
			}
		});
		
		display.getPreviousButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String to = display.getFirstOid();
				if (to != null) {
					loadPreviousData(to);
				}
			}
		});
	}
	
	@Override
	public void go(HasWidgets container) {
		bind();
		loadData(null);
        container.clear();
        container.add(display.asWidget());
	}

	@Override
	public Template getTemplate() {
		return display;
	}
	
	private void loadData(String from_oid) {
		generalService.call(new RemoteCallback<List<Scheme>>() {
            @Override
            public void callback(List<Scheme> response) {
                display.setData(response);
            }
        }, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				display.error(Text.LANG.connectionIssue());
				return false;
			}
		}).getAllFrom(from_oid, 3);
	}
	
	private void loadPreviousData(String to_oid) {
		generalService.call(new RemoteCallback<List<Scheme>>() {
            @Override
            public void callback(List<Scheme> response) {
                display.setData(response);
            }
        }, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				display.error(Text.LANG.connectionIssue());
				return false;
			}
		}).getAllTo(to_oid, 3);
	}

}
