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
import org.teree.client.presenter.Presenter;
import org.teree.client.presenter.HelpPage;
import org.teree.client.presenter.HomePage;
import org.teree.client.presenter.JoinPage;
import org.teree.client.presenter.LoginPage;
import org.teree.client.presenter.UserHome;
import org.teree.client.presenter.Explorer;
import org.teree.client.presenter.Editor;
import org.teree.client.presenter.Viewer;
import org.teree.client.presenter.SettingsPage;
import org.teree.shared.NodeGenerator;
import org.teree.shared.SchemeService;
import org.teree.shared.SecuredSchemeService;
import org.teree.shared.UserService;
import org.teree.shared.data.AuthType;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.StructureType;
import org.teree.shared.data.tree.Tree;
import org.teree.shared.data.tree.TreeType;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

@ApplicationScoped
public class TereeController implements ValueChangeHandler<String> {
	
    private static HandlerManager eventBus = new HandlerManager(null);

	@Inject
	private IOCBeanManager manager;

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
	 * @see org.teree.client.presenter.PresenterA
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
				IOCBeanDef<Explorer> bean = manager
						.lookupBean(Explorer.class);
				if (bean != null) {
					presenter = bean.getInstance();
					if (token.startsWith(Settings.SEARCH_LINK)) {
						((Explorer)presenter).setSearchText(token.substring(Settings.SEARCH_LINK.length()));
					}
				}
			} else if (token.startsWith(Settings.VIEW_LINK)) {
				IOCBeanDef<Viewer> bean = manager
						.lookupBean(Viewer.class);
				if (bean != null) {
					presenter = bean.getInstance();
					presenter.getScheme(token.substring(Settings.VIEW_LINK.length()), new RemoteCallback<Scheme>() {
						@Override
						public void callback(Scheme response) {
							if (response == null || response.getOid() == null) {
								History.newItem(Settings.HOME_LINK);
								TereeController.this.presenter.getTemplate().error("The scheme does not exist.");
							} else {
								eventBus.fireEvent(new SchemeReceived(response));
							}
						}
					});
				}
			} else if (token.startsWith(Settings.CREATE_LINK)) {
				IOCBeanDef<Editor> bean = manager
						.lookupBean(Editor.class);
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
				IOCBeanDef<Editor> bean = manager
						.lookupBean(Editor.class);
				if (bean != null) {
					presenter = bean.getInstance();
					presenter.getSchemeToEdit(token.substring(Settings.EDIT_LINK.length()), new RemoteCallback<Scheme>() {
						@Override
						public void callback(Scheme response) {
							if (response == null) {
								History.newItem(Settings.HOME_LINK);
								TereeController.this.presenter.getTemplate().error("You don't have any rights to edit the scheme");
							} else {
								eventBus.fireEvent(new SchemeReceived(response));
							}
						}
					});
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
					Tree s = new Tree();
					s.setRoot(NodeGenerator.complex());
					s.setVisualization(TreeType.MindMap); // TODO: choose visualization type
					s.setStructure(StructureType.Tree); // TODO: choose structure type
					eventBus.fireEvent(new SchemeReceived(s));
				}
			}

		}
	}
	
	private void setPresenter(Presenter presenter) {
		presenter.setEventBus(eventBus);
		if (this.presenter == null && currentUser.getUserInfo() == null) { // for page reload
			this.presenter = presenter;
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
	
	private void loadUserInfoData() {
		if (presenter != null) {
			presenter.getUserInfo(new RemoteCallback<UserInfo>() {
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
		});
		}
	}

}
