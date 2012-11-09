package org.teree.client.view;

import javax.annotation.PostConstruct;

import org.teree.client.Settings;
import org.teree.client.presenter.HomePage;
import org.teree.client.view.resource.PageStyle;

import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

public class Home extends TemplateScene implements HomePage.Display {

	private static HomeBinder uiBinder = GWT.create(HomeBinder.class);

    interface HomeBinder extends UiBinder<Widget, Home> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected();
	}
	
	@UiField
	Anchor changeLogs;
	
	@PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
        
        changeLogs.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.CHANGE_LOGS_LINK);
			}
		});
        
    }

}
