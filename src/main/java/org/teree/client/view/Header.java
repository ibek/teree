package org.teree.client.view;

import org.teree.client.presenter.HeaderTemplate;
import org.teree.client.presenter.Template;
import org.teree.shared.data.UserInfo;

import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Header extends Composite implements HeaderTemplate {

	private static HeaderBinder uiBinder = GWT.create(HeaderBinder.class);

    interface HeaderBinder extends UiBinder<Widget, Header> {
    }

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
    }
	
	@Override
	public HasClickHandlers getCreateLink() {
		return create;
	}

	@Override
	public HasClickHandlers getExploreLink() {
		return explore;
	}

	@Override
	public HasClickHandlers getHelpLink() {
		return help;
	}

	@Override
	public void setCurrentUser(UserInfo user) {
		this.user.setCurrentUser(user);
	}

}
