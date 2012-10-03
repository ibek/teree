package org.teree.client.view;

import javax.annotation.PostConstruct;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.presenter.SchemeViewer;
import org.teree.client.view.resource.PageStyle;
import org.teree.client.view.viewer.Scene;
import org.teree.client.view.viewer.ViewPanel;
import org.teree.shared.data.scheme.Node;

public class Viewer extends TemplateScene implements SchemeViewer.Display {

	private static ViewerBinder uiBinder = GWT.create(ViewerBinder.class);

    interface ViewerBinder extends UiBinder<Widget, Viewer> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected(); 
	}
    
    @UiField(provided = true)
    Scene scene;
    
    @UiField
    ViewPanel view;
    
    public Viewer() {
    	scene = new Scene();
    }
    
    @PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
        
        view.getExportImageButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.sendDownloadRequest(scene.getRoot().getContent().toString(), scene.getSchemePicture());
			}
		});
        
    }
    
    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setRoot(Node root) {
        scene.setRoot(root);
    }

}
