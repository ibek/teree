package org.teree.client.view;

import org.teree.client.presenter.Template;
import org.teree.shared.data.UserInfo;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;

public abstract class TemplateScene extends Composite implements Template {
	
	@UiField(provided = true)
	Header header;
	
	public TemplateScene() {
		header = new Header();
	}

	@Override
	public HasClickHandlers getCreateLink() {
		return header.getCreateLink();
	}

	@Override
	public HasClickHandlers getExploreLink() {
		return header.getExploreLink();
	}

	@Override
	public HasClickHandlers getHelpLink() {
		return header.getHelpLink();
	}

	@Override
	public void setCurrentUser(UserInfo user) {
		header.setCurrentUser(user);
	}
	
}
