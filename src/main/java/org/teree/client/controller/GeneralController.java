package org.teree.client.controller;

import org.jboss.errai.ioc.client.api.Caller;
import org.teree.shared.SchemeService;
import org.teree.shared.SecuredSchemeService;
import org.teree.shared.UserService;

import com.google.gwt.event.shared.HandlerManager;

public class GeneralController {

	private static GeneralController instance;
	
	private Caller<SchemeService> generalService;
	private Caller<SecuredSchemeService> securedScheme;
	private Caller<UserService> userService;
	
	private HandlerManager eventBus;
	
	private GeneralController() {
		
	}
	
	public static GeneralController getInstance() {
		if (instance == null) {
			instance = new GeneralController();
		}
		return instance;
	}

	public Caller<SchemeService> getGeneralService() {
		return generalService;
	}

	public void setGeneralService(Caller<SchemeService> generalService) {
		this.generalService = generalService;
	}

	public Caller<SecuredSchemeService> getSecuredScheme() {
		return securedScheme;
	}

	public void setSecuredScheme(Caller<SecuredSchemeService> securedScheme) {
		this.securedScheme = securedScheme;
	}

	public Caller<UserService> getUserService() {
		return userService;
	}

	public void setUserService(Caller<UserService> userService) {
		this.userService = userService;
	}

	public HandlerManager getEventBus() {
		return eventBus;
	}

	public void setEventBus(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}
	
}
