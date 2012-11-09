package org.teree.client.view;

import javax.annotation.PostConstruct;

import org.teree.client.presenter.HomePage;
import org.teree.client.view.resource.PageStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class Home extends TemplateScene implements HomePage.Display {

	private static HomeBinder uiBinder = GWT.create(HomeBinder.class);

    interface HomeBinder extends UiBinder<Widget, Home> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected();
	}
	
	@PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
