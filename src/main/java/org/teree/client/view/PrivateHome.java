package org.teree.client.view;

import java.util.List;

import javax.annotation.PostConstruct;

import com.github.gwtbootstrap.client.ui.FileUpload;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.view.explorer.PrivatePanel;
import org.teree.client.view.explorer.Scene;
import org.teree.client.view.explorer.event.HasSchemeHandlers;
import org.teree.client.view.explorer.event.ImportSchemeHandler;
import org.teree.client.view.explorer.event.PublishScheme;
import org.teree.client.view.explorer.event.PublishSchemeHandler;
import org.teree.client.view.resource.PageStyle;
import org.teree.shared.data.scheme.Scheme;

public class PrivateHome extends TemplateScene implements org.teree.client.presenter.PrivateHome.Display {

	private static ExplorerBinder uiBinder = GWT.create(ExplorerBinder.class);

    interface ExplorerBinder extends UiBinder<Widget, PrivateHome> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected(); 
	}
    
    @UiField
    Scene scene;
    
    @UiField
    PrivatePanel privatePanel;
    
    @PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
    	scene.enablePublish(true);
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

	@Override
	public HasSchemeHandlers getScene() {
		return scene;
	}

	@Override
	public void setImportSchemeHandler(ImportSchemeHandler handler) {
		privatePanel.setImportSchemeHandler(handler);
	}

}
