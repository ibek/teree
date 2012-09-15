package org.teree.client.view;

import java.util.List;

import javax.annotation.PostConstruct;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.view.explorer.Scene;
import org.teree.client.view.resource.PageStyle;
import org.teree.shared.data.Scheme;

public class PrivateHome extends TemplateScene implements org.teree.client.presenter.PrivateHome.Display {

	private static ExplorerBinder uiBinder = GWT.create(ExplorerBinder.class);

    interface ExplorerBinder extends UiBinder<Widget, PrivateHome> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected(); 
	}
    
    @UiField
    Scene scene;
    
    @PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @Override
    public Widget asWidget() {
        return this;
    }

	@Override
	public void setData(List<Scheme> slist) {
		scene.setData(slist);
	}

	@Override
	public HasClickHandlers getNextButton() {
		return scene.getNextButton();
	}

	@Override
	public HasClickHandlers getPreviousButton() {
		return scene.getPreviousButton();
	}

	@Override
	public String getFirstOid() {
		return scene.getFirstOid();
	}

	@Override
	public String getLastOid() {
		return scene.getLastOid();
	}

}