package org.teree.client.view;

import javax.annotation.PostConstruct;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.CurrentPresenter;
import org.teree.client.io.FreeMind;
import org.teree.client.io.IOFactory;
import org.teree.client.io.IOType;
import org.teree.client.view.viewer.Scene;
import org.teree.client.view.viewer.ShareDialog;
import org.teree.client.view.viewer.ViewPanel;
import org.teree.shared.data.common.Scheme;

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
        
        bind();
    }
    
    private void bind() {
        view.getExportImageButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CurrentPresenter.getInstance()
					.getPresenter()
					.getTemplate()
					.sendDownloadRequest(scheme.toString(), scene.getSchemePicture());
			}
		});
        
        view.getExportFreeMindButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					IOFactory.getExporter(IOType.FreeMind).exportScheme(scheme);
				} catch (Exception ex) {
					error("Cannot export the scheme");
				}
			}
		});
        
        view.getExportJSONButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					IOFactory.getExporter(IOType.JSON).exportScheme(scheme);
				} catch (Exception ex) {
					error("Cannot export the scheme");
				}
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
    	this.scheme = scheme;
        scene.setScheme(scheme);
        view.getShareButton().setVisible(scheme.getPermissions().getWrite() != null);
    }

}
