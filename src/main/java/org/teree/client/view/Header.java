package org.teree.client.view;

import org.teree.client.CurrentUser;
import org.teree.client.Settings;
import org.teree.client.presenter.HeaderTemplate;
import org.teree.shared.data.UserInfo;

import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Header extends Composite implements HeaderTemplate {

	private static HeaderBinder uiBinder = GWT.create(HeaderBinder.class);

    interface HeaderBinder extends UiBinder<Widget, Header> {
    }

    @UiField
    Brand home;

    @UiField
    NavLink create;

    @UiField
    NavLink explore;

    @UiField
    NavLink help;

    @UiField
    UserWidget user;
    
    public Header() {
        initWidget(uiBinder.createAndBindUi(this));
        bind();
    }
    
    private void bind() {
		
		home.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.HOME_LINK);
			}
		});
		
		create.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.CREATE_LINK);
			}
		});
		
		explore.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.EXPLORE_LINK);
			}
		});
		
    }
    
    public UserInfo getCurrentUser() {
    	return user.getCurrentUser();
    }

	@Override
	public void setCurrentUser(CurrentUser user) {
		this.user.setCurrentUser(user);
	}

}
