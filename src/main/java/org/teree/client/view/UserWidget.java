package org.teree.client.view;

import org.teree.client.Settings;
import org.teree.shared.data.UserInfo;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Nav;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.constants.Alignment;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class UserWidget extends Composite {

	private Nav container;

	private Button signIn;
	private Button join;
	private NavLink user;
	
	private UserInfo ui;

	public UserWidget() {
		container = new Nav();
		container.setAlignment(Alignment.RIGHT);
		user = new NavLink("User");
		container.add(user);

		signIn = new Button("Sign in");
		join = new Button("Join");
		container.add(signIn);
		container.add(join);
		
		bind();

		initWidget(container);
	}

	private void bind() {
		signIn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.LOGIN_LINK);
			}
		});
		
		join.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.JOIN_LINK);
			}
		});
		
		user.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO: redirect to user's home page
			}
		});
	}
	
	public UserInfo getUser() {
		return ui;
	}
	
	public void setUser(UserInfo ui) {
		this.ui = ui;
		user.setText(ui.getUsername());
	}

}
