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
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.protocols.SecurityCommands;
import org.jboss.errai.common.client.protocols.MessageParts;
import org.teree.client.event.SchemeReceived;
import org.teree.client.presenter.HomePage;
import org.teree.client.presenter.JoinPage;
import org.teree.client.presenter.LoginPage;
import org.teree.client.presenter.PrivateHome;
import org.teree.client.presenter.SchemeExplorer;
import org.teree.client.presenter.SchemeEditor;
import org.teree.client.presenter.SchemeViewer;
import org.teree.client.presenter.Presenter;
import org.teree.shared.NodeGenerator;
import org.teree.shared.GeneralService;
import org.teree.shared.UserService;
import org.teree.shared.data.AuthType;
import org.teree.shared.data.Scheme;
import org.teree.shared.data.UserInfo;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
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
	private Caller<UserService> userService;

	@Inject
	private Keyboard keyboard;
	
	@Inject @Named("currentUser")
	private UserInfo currentUser;
	
	/**
	 * Current presenter.
	 */
	private Presenter presenter;
	
	private Presenter tmpPresenter;

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
				String type = message.getCommandType();
				switch(SecurityCommands.valueOf(type)) {
					case SuccessfulAuth: {
						
						currentUser.set(message.get(UserInfo.class, UserInfo.PART));
						
						if (tmpPresenter != null) {
							setPresenter(tmpPresenter);
							tmpPresenter = null;
						} else {
							History.newItem(Settings.HOME_LINK);
						}
						
						break;
					}
					case FailedAuth: {
						History.newItem(Settings.FAILED_LOGIN_LINK);
						break;
					}
					case SecurityChallenge: {
						System.out.println("auth is required");
						tmpPresenter = presenter;
						History.newItem(Settings.LOGIN_LINK);
						break;
					}
					case EndSession: {
						currentUser.clear();
						History.newItem(Settings.HOME_LINK);
					}
				}
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
			History.newItem(Settings.HOME_LINK);
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		if (token != null) {
			Presenter presenter = null;
			
			boolean createScheme = false;

			if (token.equals(Settings.HOME_LINK)) {
				IOCBeanDef<HomePage> bean = manager.lookupBean(HomePage.class);
				if (bean != null) { 
					presenter = bean.getInstance();
				}
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
					createScheme = true;
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
			} else if (token.startsWith(Settings.JOIN_LINK)) {
				IOCBeanDef<JoinPage> bean = manager
						.lookupBean(JoinPage.class);
				if (bean != null) {
					presenter = bean.getInstance();
				}
			} else if (token.startsWith(Settings.PRIVATE_LINK)) {
				IOCBeanDef<PrivateHome> bean = manager
						.lookupBean(PrivateHome.class);
				if (bean != null) {
					presenter = bean.getInstance();
				}
			} else if (token.startsWith(Settings.TAUTH_LINK)) {
				MessageBuilder.createMessage("AuthenticationService")
			        .command(SecurityCommands.AuthRequest)
			        .with(MessageParts.ReplyTo, "LoginClient")
			        .with(AuthType.PART, AuthType.OAuth)
			        .done().sendNowWith(ErraiBus.get());
			}

			if (presenter != null) {
				
				setPresenter(presenter);
				
				if (createScheme) {
					Scheme s = new Scheme();
					s.setRoot(NodeGenerator.complex()); // TODO: create map from templates (even user's)
					eventBus.fireEvent(new SchemeReceived(s));
				}
			}

		}
	}
	
	private void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.go(container);
		
		String sessionId = Cookies.getCookie(Settings.COOKIE_SESSION_ID);
		if (sessionId == null) {
			currentUser.clear();
		} else if (currentUser.getName() == null) {
			loadUserInfoData();
		}
		presenter.getTemplate().setCurrentUser(currentUser);
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
	
	private void loadUserInfoData() {
		
		userService.call(new RemoteCallback<UserInfo>() {
			@Override
			public void callback(UserInfo response) {
				currentUser.set(response);
				presenter.getTemplate().setCurrentUser(currentUser);
			}
		}, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				// TODO Auto-generated method stub
				return false;
			}
		}).getUserInfo();
		
	}

}
