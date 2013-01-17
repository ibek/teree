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
import org.teree.client.event.GlobalKeyUp;
import org.teree.client.event.RefreshUserInfo;
import org.teree.client.event.RefreshUserInfoHandler;
import org.teree.client.event.SchemeReceived;
import org.teree.client.presenter.ChangeLogsPage;
import org.teree.client.presenter.HelpPage;
import org.teree.client.presenter.HomePage;
import org.teree.client.presenter.JoinPage;
import org.teree.client.presenter.LoginPage;
import org.teree.client.presenter.UserHome;
import org.teree.client.presenter.SchemeExplorer;
import org.teree.client.presenter.SchemeEditor;
import org.teree.client.presenter.SchemeViewer;
import org.teree.client.presenter.Presenter;
import org.teree.client.presenter.SettingsPage;
import org.teree.shared.NodeGenerator;
import org.teree.shared.SchemeService;
import org.teree.shared.SecuredSchemeService;
import org.teree.shared.UserService;
import org.teree.shared.data.AuthType;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.scheme.Scheme;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Event;
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
	private Caller<SchemeService> generalService;
	
	@Inject
	private Caller<SecuredSchemeService> securedScheme;

	@Inject
	private Caller<UserService> userService;

	private Keyboard keyboard;
	
	private CurrentUser currentUser = CurrentUser.getInstance();
	private CurrentPresenter currentPresenter = CurrentPresenter.getInstance();
	
	/**
	 * Current presenter.
	 */
	private Presenter presenter;
	
	private Presenter tmpPresenter = null;

	private HasWidgets container;

	/**
	 * Bind handlers to eventBus.
	 */
	public void bind() {
		keyboard = new Keyboard() {
			@Override
			public void onKeyUp(Event event) {
		    	eventBus.fireEvent(new GlobalKeyUp(event));
			}
		};
		keyboard.init();
		History.addValueChangeHandler(this);

		ErraiBus.get().subscribe("LoginClient", new MessageCallback() {
			@Override
			public void callback(Message message) {
				String type = message.getCommandType();
				switch(SecurityCommands.valueOf(type)) {
					case SuccessfulAuth: {
						
						currentUser.setUserInfo(message.get(UserInfo.class, UserInfo.PART));
						
						if (tmpPresenter != null) {
							History.back();
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
						tmpPresenter = presenter;
						History.newItem(Settings.LOGIN_LINK);
						break;
					}
					case EndSession: {
						currentUser.clear();
						if (History.getToken().equals(Settings.HOME_LINK)) {
							History.fireCurrentHistoryState();
						} else {
							History.newItem(Settings.HOME_LINK);
						}
					}
				}
			}
		});
		
		eventBus.addHandler(RefreshUserInfo.TYPE, new RefreshUserInfoHandler() {
			@Override
			public void refresh(RefreshUserInfo event) {
				loadUserInfoData();
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
					if (tmpPresenter != null) {
						presenter = tmpPresenter;
						tmpPresenter = null;
					} else {
						presenter = bean.getInstance();
						createScheme = true;
					}
				}
			} else if (token.startsWith(Settings.HELP_LINK)) {
				IOCBeanDef<HelpPage> bean = manager
						.lookupBean(HelpPage.class);
				if (bean != null) {
					presenter = bean.getInstance();
				}
			} else if (token.startsWith(Settings.EDIT_LINK)) {
				IOCBeanDef<SchemeEditor> bean = manager
						.lookupBean(SchemeEditor.class);
				if (bean != null) {
					presenter = bean.getInstance();
					loadSchemeToEdit(token.substring(Settings.EDIT_LINK.length()));
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
			} else if (token.startsWith(Settings.USERHOME_LINK)) {
				IOCBeanDef<UserHome> bean = manager
						.lookupBean(UserHome.class);
				if (bean != null) {
					presenter = bean.getInstance();
					((UserHome)presenter).setUser(token.substring(Settings.USERHOME_LINK.length()));
				}
			} else if (token.startsWith(Settings.SETTINGS_LINK)) {
				IOCBeanDef<SettingsPage> bean = manager
						.lookupBean(SettingsPage.class);
				if (bean != null) {
					presenter = bean.getInstance();
					if (currentUser.getUserInfo() == null) { // the user has to be logged to access this page
						tmpPresenter = presenter;
						loadUserInfoData();
						return;
					}
				}
			} else if (token.startsWith(Settings.TAUTH_LINK)) {
				MessageBuilder.createMessage("AuthenticationService")
			        .command(SecurityCommands.AuthRequest)
			        .with(MessageParts.ReplyTo, "LoginClient")
			        .with(AuthType.PART, AuthType.OAuth)
			        .done().sendNowWith(ErraiBus.get());
			} else if (token.equals(Settings.CHANGE_LOGS_LINK)) {
				IOCBeanDef<ChangeLogsPage> bean = manager.lookupBean(ChangeLogsPage.class);
				if (bean != null) { 
					presenter = bean.getInstance();
				}
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
		if (this.presenter == null && currentUser.getUserInfo() == null) { // for page reload
			loadUserInfoData();
		}
		this.presenter = presenter;
		currentPresenter.setPresenter(presenter);
		presenter.go(container);
		
		String sessionId = Cookies.getCookie(Settings.COOKIE_SESSION_ID);
		if (sessionId == null) {
			currentUser.clear();
		}
		presenter.getTemplate().setCurrentUser(currentUser);
	}

	private void loadScheme(final String oid) {
		generalService.call(new RemoteCallback<Scheme>() {
			@Override
			public void callback(Scheme response) {
				if (response == null || response.getRoot() == null || response.getRoot().getContent() == null) {
					History.newItem(Settings.HOME_LINK);
					presenter.getTemplate().error("The scheme does not exist.");
				} else {
					eventBus.fireEvent(new SchemeReceived(response));
				}
			}
		}, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				presenter.getTemplate().error("Error while getting the respond. Please try reload the page.");
				return false;
			}
		}).getScheme(oid);
	}

	private void loadSchemeToEdit(final String oid) {
		securedScheme.call(new RemoteCallback<Scheme>() {
			@Override
			public void callback(Scheme response) {
				if (response == null) {
					History.newItem(Settings.HOME_LINK);
					presenter.getTemplate().error("You don't have any rights to edit the scheme");
				} else {
					eventBus.fireEvent(new SchemeReceived(response));
				}
			}
		}, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				presenter.getTemplate().error("Error while getting the respond. Please try reload the page.");
				return false;
			}
		}).getScheme(oid);
	}
	
	private void loadUserInfoData() {
		
		userService.call(new RemoteCallback<UserInfo>() {
			@Override
			public void callback(UserInfo response) {
				currentUser.setUserInfo(response);
				if (tmpPresenter != null && currentUser.getUserInfo() == null) {
					History.newItem(Settings.LOGIN_LINK);
				}else if (tmpPresenter != null) {
					setPresenter(tmpPresenter);
					tmpPresenter = null;
				} else {
					presenter.getTemplate().setCurrentUser(currentUser);
				}
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
