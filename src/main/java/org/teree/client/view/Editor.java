package org.teree.client.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.presenter.MapEditor;

public class Editor extends Composite implements MapEditor.Display {

    @Inject
    UiBinder<Panel, Editor> uiBinder;

    @UiField
    Button btnNew;

    @UiField
    Button btnSave;
    
    @PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public HasClickHandlers getNewButton() {
        return btnNew;
    }

    @Override
    public HasClickHandlers getSaveButton() {
        return btnSave;
    }

}
