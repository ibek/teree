package org.teree.client.view;

import org.teree.client.CurrentUser;
import org.teree.client.Settings;
import org.teree.client.presenter.HeaderTemplate;
import org.teree.shared.data.UserInfo;

import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Form.SubmitHandler;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavSearch;
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
    NavSearch search;
    
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

        search.getTextBox().getElement().getStyle().setProperty("borderRadius", "0px");
        search.getTextBox().getElement().getStyle().setProperty("MozBorderRadius", "0px");
        search.getTextBox().getElement().getStyle().setProperty("WebkitBorderRadius", "0px");
        
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
		
		search.addSubmitHandler(new SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				System.out.println("search " + search.getTextBox().getText());
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
