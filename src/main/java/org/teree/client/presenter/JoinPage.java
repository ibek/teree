/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.client.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.teree.shared.data.UserInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class JoinPage extends Presenter {

	public interface Display extends Template {
        HasClickHandlers getGoogleButton();
        HasClickHandlers getRegisterButton();
        Widget asWidget();
        boolean validate();
        UserInfo getUserInfo();
        String getPassword();
    }
    
    @Inject
    private Display display;
    
    public void bind() {
    	display.getRegisterButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (display.validate()) {
					registerUser(display.getUserInfo(), display.getPassword());
				}
			}
		});
    	
    	display.getGoogleButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(GWT.getHostPageBaseURL()+"/oauth");
			}
		});
    	
    }
    
    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
    }

	@Override
	public Template getTemplate() {
		return display;
	}

	@Override
	public String getTitle() {
		return "Join";
	}

}
