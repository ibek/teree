package org.teree.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * 
 * TODO: implement login and through this widget enable sign in, join, logout ...
 *
 */
public class UserWidget extends Composite {

	private HorizontalPanel container;
	
	private Label user;
	
	public UserWidget() {
		container = new HorizontalPanel();
		user = new Label("user");
		container.add(user);
		initWidget(container);
	}
	
}
