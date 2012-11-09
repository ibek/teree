package org.teree.client.view;

import javax.annotation.PostConstruct;

import org.teree.client.presenter.ChangeLogsPage;
import org.teree.client.view.resource.PageStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class ChangeLogs extends TemplateScene implements ChangeLogsPage.Display {

	private static ChangeLogsBinder uiBinder = GWT.create(ChangeLogsBinder.class);

    interface ChangeLogsBinder extends UiBinder<Widget, ChangeLogs> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected();
	}
	
	@PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
