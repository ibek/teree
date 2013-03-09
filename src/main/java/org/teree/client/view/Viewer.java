package org.teree.client.view;

import javax.annotation.PostConstruct;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.CurrentPresenter;
import org.teree.client.CurrentUser;
import org.teree.client.Settings;
import org.teree.client.io.FreeMind;
import org.teree.client.io.IOFactory;
import org.teree.client.io.IOType;
import org.teree.client.view.viewer.Scene;
import org.teree.client.view.viewer.ShareDialog;
import org.teree.client.view.viewer.ViewPanel;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.Viewpoint;

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
    
    @Override
    public void setCurrentUser(CurrentUser user) {
    	super.setCurrentUser(user);
    	setPermissions();
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
        
        view.getViewpoints().addValueChangeHandler(new ValueChangeHandler<Viewpoint>() {
			@Override
			public void onValueChange(ValueChangeEvent<Viewpoint> event) {
				int id = -1;
				if (event.getValue() != null) {
					id = event.getValue().getId();
				}
				scene.changeViewpoint(id);
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

        view.setViewpoints(scheme.getViewpoints());
        
        setPermissions();
    }
    
    private void setPermissions() {
		UserInfo ui = CurrentUser.getInstance().getUserInfo();
		if (scheme != null && ui != null) {
	        boolean permissionsToEdit = (scheme.getAuthor().getUserId().equals(ui.getUserId())) || scheme.getPermissions().canEdit(ui);
	
	        view.getShareButton().setVisible(scheme.getPermissions().getWrite() != null);
			view.getAddViewpointButton().setVisible(permissionsToEdit);
		}
    }

}
