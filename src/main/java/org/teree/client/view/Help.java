package org.teree.client.view;

import javax.annotation.PostConstruct;

import org.teree.client.presenter.HelpPage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class Help extends TemplateScene implements HelpPage.Display {

	private static HelpBinder uiBinder = GWT.create(HelpBinder.class);

    interface HelpBinder extends UiBinder<Widget, Help> {
    }
	
	@UiField
	Grid keyShortcuts;
	
	@PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
