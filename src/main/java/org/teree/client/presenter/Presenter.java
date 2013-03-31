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
import org.teree.client.text.UIMessages;
import org.teree.shared.NodeCategoryService;
import org.teree.shared.SchemeService;
import org.teree.shared.SecuredSchemeService;
import org.teree.shared.UserService;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.common.NodeCategory;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.SchemeFilter;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public abstract class Presenter {

    private HandlerManager eventBus;
	
	@Inject
	private Caller<SchemeService> generalService;
	
	@Inject
	private Caller<SecuredSchemeService> securedService;
	
	@Inject
	private Caller<UserService> userService;
	
	@Inject
	private Caller<NodeCategoryService> nodeCategoryService;
	
	public Presenter() {
		
	}
	
	public HandlerManager getEventBus() {
		return eventBus;
	}
	
	public void setEventBus(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}
	
	public Caller<SchemeService> getGeneralService() {
		return generalService;
	}
	
	public Caller<SecuredSchemeService> getSecuredService() {
		return securedService;
	}
	
	public Caller<UserService> getUserService() {
		return userService;
	}
	
    /**
     * Change current scene and generate the new scene into container. 
     * @param container
     */
    public abstract void go(final HasWidgets container);
    
    public abstract Template getTemplate();
    
    public abstract String getTitle();
	
	public void displayError(String msg) {
		getTemplate().error(msg);
	}
	
	public void displayInfo(String msg) {
		getTemplate().info(msg);
	}
	
	public void createScheme(final Scheme scheme) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
            	History.newItem(Settings.EDIT_LINK);
            	eventBus.fireEvent(new SchemeReceived(scheme));
            }
        });
	}
	
	public void getScheme(String oid, RemoteCallback<Scheme> callback) {
    	generalService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(message.toString());
				return false;
			}
		}).getScheme(oid);
    }
	
	public void getSchemeToEdit(String oid, RemoteCallback<Scheme> callback) {
    	securedService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(message.toString());
				return false;
			}
		}).getScheme(oid);
    }
	
	public void getNodeCategories(RemoteCallback<List<NodeCategory>> callback) {
		nodeCategoryService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(message.toString());
				return false;
			}
		}).selectAll();
	}
	
    public void insertScheme(Scheme scheme, RemoteCallback<String> callback) {
    	securedService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(message.toString());
				return false;
			}
		}).insertScheme(scheme);
    }
    
    public void insertNodeCategory(NodeCategory category, RemoteCallback<String> callback) {
    	nodeCategoryService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(message.toString());
				return false;
			}
		}).insertNodeCategory(category);
    }
    
    public void saveScheme(final Scheme scheme) {
    	if (scheme.getOid() == null) {
    		insertScheme(scheme, new RemoteCallback<String>() {
                @Override
                public void callback(String response) {
                    scheme.setOid(response);
                    displayInfo(UIMessages.LANG.schemeCreated(scheme.toString()));
                }
            });
    	} else {
    		securedService.call(new RemoteCallback<Void>() {
	            @Override
	            public void callback(Void response) {
	                displayInfo(UIMessages.LANG.schemeUpdated(scheme.toString()));
	            }
	        }, new ErrorCallback() {
				@Override
				public boolean error(Message message, Throwable throwable) {
					displayError(message.toString());
					return false;
				}
			}).updateScheme(scheme);
    	}
    }
    
    public void updateNodeCategory(final NodeCategory category, RemoteCallback<Void> callback) {
    	nodeCategoryService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(message.toString());
				return false;
			}
		}).updateNodeCategory(category);
    }
	
	public void removeScheme(final Scheme scheme, RemoteCallback<Boolean> callback) {
		securedService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(UIMessages.LANG.connectionIssue());
				return false;
			}
		}).removeScheme(scheme.getOid());
	}
	
	public void removeNodeCategory(final NodeCategory category, RemoteCallback<Boolean> callback) {
		nodeCategoryService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(UIMessages.LANG.connectionIssue());
				return false;
			}
		}).removeNodeCategory(category.getOid());
	}
	
	public void importJSON(String json) {
		generalService.call(new RemoteCallback<Scheme>() {
            @Override
            public void callback(Scheme response) {
            	if (response == null) {
            		displayError("Cannot import the scheme");
            	} else {
            		createScheme(response);
            	}
            }
        }, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(UIMessages.LANG.connectionIssue());
				return false;
			}
		}).importJSON(json);
	}
	
	public void exportJSON(String oid, final String title) {
		getGeneralService().call(new RemoteCallback<String>() {
            @Override
            public void callback(String response) {
            	if (response == null) {
            		displayError(UIMessages.LANG.cannot_export_scheme());
            	} else {
            		getTemplate().sendDownloadRequest(title, "application/json", response);
            	}
            }
        }, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(UIMessages.LANG.connectionIssue());
				return false;
			}
		}).exportJSON(oid);
	}
	
	public void updatePermissions(Scheme scheme) {
		securedService.call(new RemoteCallback<Void>() {
            @Override
            public void callback(Void response) {
                displayInfo("Scheme permissions updated");
            }
        }, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(UIMessages.LANG.connectionIssue());
				return false;
			}
		}).updateSchemePermissions(scheme);
	}
	
	public void selectFrom(String fromOid, SchemeFilter filter, RemoteCallback<List<Scheme>> callback) {
		generalService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(UIMessages.LANG.connectionIssue());
				return false;
			}
		}).selectFrom(fromOid, filter, Settings.SCHEME_COUNT_IN_EXPLORER);
	}
	
	public void selectTo(String toOid, SchemeFilter filter, RemoteCallback<List<Scheme>> callback) {
		generalService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(UIMessages.LANG.connectionIssue());
				return false;
			}
		}).selectTo(toOid, filter, Settings.SCHEME_COUNT_IN_EXPLORER);
	}
	
	/**
	 * Get current user info.
	 * @param callback
	 */
	public void getUserInfo(RemoteCallback<UserInfo> callback) {
		userService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(UIMessages.LANG.connectionIssue());
				return false;
			}
		}).getUserInfo();
	}
	
	public void getUserInfo(String userid, RemoteCallback<UserInfo> callback) {
		userService.call(callback, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(UIMessages.LANG.connectionIssue());
				return false;
			}
		}).getUserInfo(userid);
	}
	
	public void registerUser(UserInfo ui, String password) {
		userService.call(new RemoteCallback<Boolean>() {
			@Override
			public void callback(Boolean response) {
				if (response) {
					History.newItem(Settings.LOGIN_LINK);
				} else {
					displayError("Cannot create user, username and email must be unique.");
				}
			}
		}, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(throwable.getMessage());
				return false;
			}
		}).register(ui, password);
	}
	
	public void updateUser(UserInfo ui) {
		userService.call(new RemoteCallback<Void>() {
			@Override
			public void callback(Void response) {
				displayInfo("Profile has been updated");
				eventBus.fireEvent(new RefreshUserInfo());
			}
		}, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(throwable.getMessage());
				return false;
			}
		}).update(ui);
	}
	
	public void updatePassword(String oldPassword, String newPassword) {
		userService.call(new RemoteCallback<Void>() {
			@Override
			public void callback(Void response) {
				displayInfo("Password has been changed");
			}
		}, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				displayError(throwable.getMessage());
				return false;
			}
		}).updatePassword(oldPassword, newPassword);
	}
	
}
