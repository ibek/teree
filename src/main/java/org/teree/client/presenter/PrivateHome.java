package org.teree.client.presenter;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.Text;
import org.teree.client.view.explorer.event.HasSchemeHandlers;
import org.teree.client.view.explorer.event.PublishScheme;
import org.teree.client.view.explorer.event.PublishSchemeHandler;
import org.teree.client.view.explorer.event.RemoveScheme;
import org.teree.client.view.explorer.event.RemoveSchemeHandler;
import org.teree.shared.SecuredSchemeService;
import org.teree.shared.data.scheme.Scheme;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class PrivateHome implements Presenter {

	public interface Display extends Template {
        Widget asWidget();
        void setData(List<Scheme> slist);
        HasClickHandlers getNextButton();
        HasClickHandlers getPreviousButton();
        HasSchemeHandlers getScene();
        String getFirstOid();
        String getLastOid();
    }
	
	@Inject
	private Caller<SecuredSchemeService> securedService;
    
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
		
		display.getScene().addPublishHandler(new PublishSchemeHandler() {
			@Override
			public void publish(final PublishScheme event) {
				securedService.call(new RemoteCallback<Void>() {
					@Override
					public void callback(Void response) {
						display.info(Text.LANG.schemePublished(event.getScheme().getOid()));
						loadData(null);
					}
				}, new ErrorCallback() {
					@Override
					public boolean error(Message message, Throwable throwable) {
						// TODO Auto-generated method stub
						return false;
					}
				}).publishScheme(event.getScheme().getOid());
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
		
		display.getScene().addRemoveHandler(new RemoveSchemeHandler() {
			@Override
			public void remove(final RemoveScheme event) {
				securedService.call(new RemoteCallback<Boolean>() {
					@Override
					public void callback(Boolean response) {
						if (response) {
							display.info(Text.LANG.schemeRemoved(event.getScheme().getOid()));
							loadData(null);
						} else {
							display.error("You are not owner or author of the scheme.");
						}
					}
				}, new ErrorCallback() {
					@Override
					public boolean error(Message message, Throwable throwable) {
						// TODO Auto-generated method stub
						return false;
					}
				}).removeScheme(event.getScheme().getOid());
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
		securedService.call(new RemoteCallback<List<Scheme>>() {
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
		}).getPrivateSchemesFrom(from_oid, 3);
	}
	
	private void loadPreviousData(String to_oid) {
		securedService.call(new RemoteCallback<List<Scheme>>() {
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
		}).getPrivateSchemesTo(to_oid, 3);
	}

}
