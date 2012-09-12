package org.teree.client.view;

import javax.annotation.PostConstruct;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.Text;
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
	Header header;
    
    @UiField(provided = true)
    Scene scene;
    
    @UiField
    Alert status;
    
    public Viewer() {
    	scene = new Scene();
    }
    
    @PostConstruct
    public void init() {
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
		status.setType(AlertType.INFO);
		status.setText(msg);
		status.setVisible(true);
		Timer t = new Timer() {
            @Override
            public void run() {
            	status.setVisible(false);
            }
        };
        t.schedule(5000);
	}

	@Override
	public HasClickHandlers getCreateLink() {
		return header.getCreateLink();
	}

	@Override
	public HasClickHandlers getExploreLink() {
		return header.getExploreLink();
	}

	@Override
	public HasClickHandlers getHelpLink() {
		return header.getHelpLink();
	}

}