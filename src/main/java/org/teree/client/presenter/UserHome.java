package org.teree.client.presenter;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.Settings;
import org.teree.client.event.RefreshUserInfo;
import org.teree.client.event.SchemeReceived;
import org.teree.client.text.General;
import org.teree.client.view.explorer.event.HasSchemeHandlers;
import org.teree.client.view.explorer.event.ImportSchemeHandler;
import org.teree.client.view.explorer.event.PublishScheme;
import org.teree.client.view.explorer.event.PublishSchemeHandler;
import org.teree.client.view.explorer.event.RemoveScheme;
import org.teree.client.view.explorer.event.RemoveSchemeHandler;
import org.teree.shared.SchemeService;
import org.teree.shared.SecuredSchemeService;
import org.teree.shared.data.scheme.Scheme;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class UserHome implements Presenter {

	public interface Display extends Template {
        Widget asWidget();
        void setData(List<Scheme> slist);
        HasClickHandlers getNextButton();
        HasClickHandlers getPreviousButton();
        HasSchemeHandlers getScene();
        String getFirstOid();
        String getLastOid();
        void setImportSchemeHandler(ImportSchemeHandler handler);
        void setUser(String userid);
    }
	
    @Inject @Named(value="eventBus")
    private HandlerManager eventBus;
	
	@Inject
	private Caller<SecuredSchemeService> securedService;
	
	@Inject
	private Caller<SchemeService> generalService;
    
    @Inject
    private Display display;
    
    private String userid;
	
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
		
		display.getScene().addRemoveHandler(new RemoveSchemeHandler() {
			@Override
			public void remove(final RemoveScheme event) {
				securedService.call(new RemoteCallback<Boolean>() {
					@Override
					public void callback(Boolean response) {
						if (response) {
							display.info(General.LANG.schemeRemoved(event.getScheme().getOid()));
							Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					            @Override
					            public void execute() {
									eventBus.fireEvent(new RefreshUserInfo());
					            }
					        });
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
		
		display.setImportSchemeHandler(new ImportSchemeHandler() {
			@Override
			public void importScheme(final Scheme scheme) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		            @Override
		            public void execute() {
		            	History.newItem(Settings.CREATE_LINK);
		            	eventBus.fireEvent(new SchemeReceived(scheme));
		            }
		        });
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
	
	public void setUser(String userid) {
		this.userid = userid;
		display.setUser(userid);
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
				display.error(General.LANG.connectionIssue());
				return false;
			}
		}).getAllFromUser(from_oid, Settings.SCHEME_COUNT_IN_EXPLORER, userid);
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
				display.error(General.LANG.connectionIssue());
				return false;
			}
		}).getAllToUser(to_oid, Settings.SCHEME_COUNT_IN_EXPLORER, userid);
	}

}
