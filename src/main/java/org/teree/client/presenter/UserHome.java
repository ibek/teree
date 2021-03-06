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

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.text.UIMessages;
import org.teree.client.view.explorer.event.HasSchemeHandlers;
import org.teree.client.view.explorer.event.RemoveScheme;
import org.teree.client.view.explorer.event.RemoveSchemeHandler;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.SchemeFilter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class UserHome extends Presenter {

	public interface Display extends Template {
        Widget asWidget();
        void setData(List<Scheme> slist);
        HasClickHandlers getNextButton();
        HasClickHandlers getPreviousButton();
        HasSchemeHandlers getScene();
        String getFirstOid();
        String getLastOid();
        void setUser(UserInfo ui);
    }
    
    @Inject
    private Display display;
    
    private String userid;
	
	public void bind() {
		display.getNextButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String from = display.getLastOid();
				if (from != null) {
					selectFrom(from);
				}
			}
		});
		
		display.getPreviousButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String to = display.getFirstOid();
				if (to != null) {
					selectTo(to);
				}
			}
		});
		
		display.getScene().addRemoveHandler(new RemoveSchemeHandler() {
			@Override
			public void remove(final RemoveScheme event) {
				removeScheme(event.getScheme());
			}
		});
		
	}
	
	@Override
	public void go(HasWidgets container) {
		bind();
		selectFrom(null);
        container.clear();
        container.add(display.asWidget());
	}

	@Override
	public Template getTemplate() {
		return display;
	}
	
	public void setUser(String userid) {
		this.userid = userid;
		loadUserInfo();
	}
	
	public void selectFrom(String fromOid) {
		selectFrom(fromOid, new SchemeFilter(), new RemoteCallback<List<Scheme>>() {
            @Override
            public void callback(List<Scheme> response) {
                display.setData(response);
            }
        });
	}
	
	public void selectTo(String toOid) {
		selectTo(toOid, new SchemeFilter(), new RemoteCallback<List<Scheme>>() {
            @Override
            public void callback(List<Scheme> response) {
                display.setData(response);
            }
        });
	}
	
	public void removeScheme(final Scheme scheme) {
		removeScheme(scheme, new RemoteCallback<Boolean>() {
			@Override
			public void callback(Boolean response) {
				if (response) {
					display.info(UIMessages.LANG.schemeRemoved(scheme.getOid()));
					selectFrom(null);
				} else {
					display.error("You are not author of the scheme.");
				}
			}
		});
	}
	
	public void loadUserInfo() {
		getUserInfo(userid, new RemoteCallback<UserInfo>() {
			@Override
			public void callback(UserInfo response) {
				display.setUser(response);
			}
		});
	}

	@Override
	public String getTitle() {
		return "User Home";
	}

}
