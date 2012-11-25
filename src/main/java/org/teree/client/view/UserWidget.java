package org.teree.client.view;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.protocols.SecurityCommands;
import org.teree.client.Settings;
import org.teree.shared.data.UserInfo;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.Nav;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.Alignment;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.dom.client.Style.Float;
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
	private DropdownButton lang;
	
	private UserInfo user;
	
	private org.teree.client.text.UserWidget TEXT = org.teree.client.text.UserWidget.LANG;

	public UserWidget() {
		container = new Nav();
		container.setAlignment(Alignment.RIGHT);
		
		userHome = new NavLink("User");
		container.add(userHome);

		signIn = new Button(TEXT.sign_in());
		join = new Button(TEXT.join());
		
		container.add(signIn);
		container.add(join);
		
		lang = new DropdownButton();
		lang.getElement().getStyle().setFloat(Float.RIGHT);
		lang.setIcon(IconType.FLAG);
		NavLink cs = new NavLink("Czech");
		cs.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeLocale("cs");
			}
		});
		lang.add(cs);
		NavLink en = new NavLink("English");
		en.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeLocale("en");
			}
		});
		lang.add(en);
		
		Tooltip lt = new Tooltip(TEXT.set_language());
		lt.setPlacement(Placement.LEFT);
		lt.add(lang);
		container.add(lt);
		
		logout = new Button("", IconType.SIGNOUT);
		logout.getElement().getStyle().setFloat(Float.RIGHT);
		
        Tooltip tl = new Tooltip(TEXT.logout());
        tl.setPlacement(Placement.BOTTOM);
        tl.add(logout);
		container.add(tl);

		settings = new Button("", IconType.WRENCH);
		settings.getElement().getStyle().setFloat(Float.RIGHT);

        Tooltip ts = new Tooltip(TEXT.settings());
        ts.setPlacement(Placement.BOTTOM);
        ts.add(settings);
		container.add(ts);

		initWidget(container);
		
		userHome.setIcon(IconType.HOME);
		
		bind();
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
				History.newItem(Settings.SETTINGS_LINK);
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
				History.newItem(Settings.USERHOME_LINK + user.getUserId());
			}
		});
	}
	
    private native void changeLocale(String newLocale)/*-{
        var currLocation = $wnd.location.toString();
        var noHistoryCurrLocArray = currLocation.split("#");
        var noHistoryCurrLoc = noHistoryCurrLocArray[0];
        var page = ""
        if (noHistoryCurrLocArray.length > 1) {
        	page = "#" + noHistoryCurrLocArray[1];
        }
        if (noHistoryCurrLoc.indexOf("?") >= 0) {
        	if (noHistoryCurrLoc.indexOf("locale=") >= 0) {
        		$wnd.location.href = noHistoryCurrLoc.replace(/locale=([^&#]*)/g, "locale=" + newLocale) + page; 
        	} else {
        		$wnd.location.href = noHistoryCurrLoc + "&locale=" + newLocale + page;
        	} 
        } else {
        	$wnd.location.href = noHistoryCurrLoc + "?locale=" + newLocale + page; 
        }
        
    }-*/;
	
	public UserInfo getCurrentUser() {
		return user;
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
