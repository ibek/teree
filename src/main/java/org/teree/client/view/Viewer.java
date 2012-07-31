package org.teree.client.view;

import javax.annotation.PostConstruct;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.presenter.SchemeViewer;
import org.teree.client.view.resource.PageStyle;
import org.teree.client.view.viewer.Scene;
import org.teree.shared.data.Node;

public class Viewer extends Composite implements SchemeViewer.Display {

	private static ViewerBinder uiBinder = GWT.create(ViewerBinder.class);

    interface ViewerBinder extends UiBinder<Widget, Viewer> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected(); 
	}

    @UiField
    Button btnNew;

    @UiField
    Label linkExplore;

    @UiField
    Label linkHelp;
    
    @UiField(provided = true)
    Scene scene;
    
    @UiField
    Label status;
    
    @UiField
    UserWidget user;
    
    @PostConstruct
    public void init() {
    	scene = new Scene();
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setRoot(Node root) {
        scene.setRoot(root);
    }

	@Override
	public void info(String msg) {
		status.setText(msg);
        Timer t = new Timer() {
            @Override
            public void run() {
                status.setText("");
            }
        };
        t.schedule(5000);
	}

	@Override
	public HasClickHandlers getNewButton() {
		return btnNew;
	}

	@Override
	public HasClickHandlers getExploreLink() {
		return linkExplore;
	}

	@Override
	public HasClickHandlers getHelpLink() {
		return linkHelp;
	}

}
