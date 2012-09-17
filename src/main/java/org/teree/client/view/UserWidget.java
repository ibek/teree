package org.teree.client.view;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.protocols.SecurityCommands;
import org.teree.client.Settings;
import org.teree.shared.data.UserInfo;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Nav;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.Alignment;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;

public class UserWidget extends Composite {

	private Nav container;

	private Button signIn;
	private Button join;
	private Button settings;
	private Button logout;
	private NavLink userHome;
	
	private UserInfo user;

	public UserWidget() {
		container = new Nav();
		container.setAlignment(Alignment.RIGHT);
		userHome = new NavLink("User");
		userHome.setIcon(IconType.HOME);
		container.add(userHome);

		signIn = new Button("Sign in");
		join = new Button("Join");

		settings = new Button("", IconType.WRENCH);
		
		logout = new Button("",IconType.SIGNOUT);
		container.add(signIn);
		container.add(join);

        Tooltip ts = new Tooltip("Settings");
        ts.setPlacement(Placement.BOTTOM);
        ts.add(settings);
		container.add(ts);
		
        Tooltip tl = new Tooltip("Logout");
        tl.setPlacement(Placement.BOTTOM);
        tl.add(logout);
		container.add(tl);
		
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
		
		settings.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO: settings for user
			}
		});
		
		logout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MessageBuilder.createMessage("AuthenticationService")
			        .command(SecurityCommands.EndSession)
			        .done().sendNowWith(ErraiBus.get());
			}
		});
		
		userHome.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.PRIVATE_LINK);
			}
		});
	}
	
	public void setCurrentUser(UserInfo ui) {
		this.user = ui;
		boolean logged = ui != null && ui.getName() != null;
		
		userHome.setVisible(logged);
		settings.setVisible(logged);
		logout.setVisible(logged);
		
		signIn.setVisible(!logged);
		join.setVisible(!logged);
		
		if (logged) {
			userHome.setText(ui.getName());
		}
	}

}
