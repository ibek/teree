package org.teree.client;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.IOCBeanManager;
import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.MessageCallback;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.event.SchemeReceived;
import org.teree.client.presenter.LoginPage;
import org.teree.client.presenter.SchemeExplorer;
import org.teree.client.presenter.SchemeEditor;
import org.teree.client.presenter.SchemeViewer;
import org.teree.client.presenter.Presenter;
import org.teree.client.presenter.Template;
import org.teree.shared.NodeGenerator;
import org.teree.shared.GeneralService;
import org.teree.shared.data.Scheme;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

@ApplicationScoped
public class TereeController implements ValueChangeHandler<String> {

	@Inject
	private IOCBeanManager manager;

	@Inject
	@Named(value = "eventBus")
	private HandlerManager eventBus;

	@Inject
	private Caller<GeneralService> generalService;

	@Inject
	private Keyboard keyboard;

	private HasWidgets container;

	/**
	 * Bind handlers to eventBus.
	 */
	public void bind() {
		keyboard.init();
		History.addValueChangeHandler(this);

		ErraiBus.get().subscribe("LoginClient", new MessageCallback() {
			@Override
			public void callback(Message message) {
				if (message.getCommandType().equals("FailedAuth")) {
					History.newItem(Settings.FAILED_LOGIN_LINK);
				} else if (message.getCommandType().equals("SuccessfulAuth")) {
					History.newItem(Settings.EXPLORE_LINK); // TODO: should continue with previous task
				} else {
					History.newItem(Settings.LOGIN_LINK);
				}
			}
		});
	}
	
	private void bindPresenter(Presenter presenter) {
		Template temp = presenter.getTemplate();
		
		temp.getCreateLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.CREATE_LINK);
			}
		});
		
		
		temp.getExploreLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.EXPLORE_LINK);
			}
		});
		
	}

	/**
	 * @see org.teree.client.presenter.Presenter
	 */
	public void go(HasWidgets container) {
		this.container = container;
		bind();

		if ("".equals(History.getToken())) {
			History.newItem(Settings.CREATE_LINK);
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		if (token != null) {
			Presenter presenter = null;

			if (token.equals(Settings.HOME_LINK)) {
				/**
				 * IOCBeanDef<MapView> bean = manager.lookupBean(MapView.class);
				 * if (bean != null) { presenter = bean.getInstance();
				 * 
				 * }
				 */
			} else if (token.startsWith(Settings.EXPLORE_LINK)) {
				IOCBeanDef<SchemeExplorer> bean = manager
						.lookupBean(SchemeExplorer.class);
				if (bean != null) {
					presenter = bean.getInstance();
				}
			} else if (token.startsWith(Settings.VIEW_LINK)) {
				IOCBeanDef<SchemeViewer> bean = manager
						.lookupBean(SchemeViewer.class);
				if (bean != null) {
					presenter = bean.getInstance();
					loadScheme(token.substring(Settings.VIEW_LINK.length()));
				}
			} else if (token.startsWith(Settings.CREATE_LINK)) {
				IOCBeanDef<SchemeEditor> bean = manager
						.lookupBean(SchemeEditor.class);
				if (bean != null) {
					presenter = bean.getInstance();
					presenter.go(container);
					Scheme s = new Scheme();
					s.setRoot(NodeGenerator.complex()); // TODO: create map from templates (even user's)
					eventBus.fireEvent(new SchemeReceived(s));
					return;
				}
			} else if (token.startsWith(Settings.EDIT_LINK)) {
				IOCBeanDef<SchemeEditor> bean = manager
						.lookupBean(SchemeEditor.class);
				if (bean != null) {
					presenter = bean.getInstance();
					loadScheme(token.substring(Settings.EDIT_LINK.length()));
				}
			} else if (token.startsWith(Settings.LOGIN_LINK)) {
				IOCBeanDef<LoginPage> bean = manager
						.lookupBean(LoginPage.class);
				if (bean != null) {
					presenter = bean.getInstance();
					
					if (token.startsWith(Settings.FAILED_LOGIN_LINK)) {
						((LoginPage)presenter).fail();
					}
					
				}
			} /**else if (token.startsWith(Settings.TAUTH_LINK)) {
				System.out.println("#oauth");
				MessageBuilder.createMessage("AuthenticationService")
			        .command(SecurityCommands.AuthRequest)
			        .with(MessageParts.ReplyTo, "LoginClient")
			        .with(AuthType.PART, AuthType.OAuth)
			        .done().sendNowWith(ErraiBus.get());
			}*/

			if (presenter != null) {
				presenter.go(container);
				bindPresenter(presenter);
			}

		}
	}

	private void loadScheme(final String oid) {
		generalService.call(new RemoteCallback<Scheme>() {
			@Override
			public void callback(Scheme response) {
				eventBus.fireEvent(new SchemeReceived(response));
			}
		}, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				// TODO inform user about the error - show 404 page
				return false;
			}
		}).getScheme(oid);
	}

}
