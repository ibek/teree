package org.teree.client.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.presenter.MapView;
import org.teree.client.view.widget.Scene;
import org.teree.shared.data.Node;

public class Viewer extends Composite implements MapView.Display {

    @Inject
    UiBinder<Panel, Viewer> uiBinder;
    
    @UiField
    Scene scene;
    
    @UiField
    Label status;
    
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
        
    }

}
