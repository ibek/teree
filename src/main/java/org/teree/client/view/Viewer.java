package org.teree.client.view;

import javax.annotation.PostConstruct;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.io.FreeMind;
import org.teree.client.view.viewer.Scene;
import org.teree.client.view.viewer.ShareDialog;
import org.teree.client.view.viewer.ViewPanel;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.tree.Tree;

public class Viewer extends TemplateScene implements org.teree.client.presenter.Viewer.Display {

	private static ViewerBinder uiBinder = GWT.create(ViewerBinder.class);

    interface ViewerBinder extends UiBinder<Widget, Viewer> {
    }
    
    @UiField(provided = true)
    Scene scene;
    
    @UiField
    ViewPanel view;
    
    private ShareDialog shareDialog;
    private Scheme scheme;
    
    public Viewer() {
    	scene = new Scene();
    }
    
    @PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
        
        view.getExportImageButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.sendDownloadRequest(scheme.getTitle(), scene.getSchemePicture());
			}
		});
        
        view.getExportFreeMindButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				sendDownloadRequest(scheme.getTitle(), "freemind", new FreeMind().exportScheme(scheme));
			}
		});
        
        view.getShareButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (shareDialog == null) {
					shareDialog = new ShareDialog();
				}
				shareDialog.setOid(scheme.getOid());
				shareDialog.setPopupPosition(view.getAbsoluteLeft()+view.getOffsetWidth()/2, view.getAbsoluteTop()+view.getOffsetHeight());
				shareDialog.show();
			}
		});
        
        view.getCollapseAllButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean collapse = !view.isCollapsed();
				scene.changeCollapseAll(collapse);
				view.setCollapsed(collapse);
			}
		});
        
    }
    
    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setScheme(Scheme scheme) {
        scene.setScheme(scheme);
        view.getShareButton().setVisible(scheme.getPermissions().getWrite() != null);
    }

	@Override
	public HasClickHandlers getExportJSONButton() {
		return view.getExportJSONButton();
	}

	@Override
	public void sendDownloadRequest(String name, String type, String data) {
		view.sendDownloadRequest(name, type, data);
	}

}
