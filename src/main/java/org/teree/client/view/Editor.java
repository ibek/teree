package org.teree.client.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.presenter.MapEditor;
import org.teree.client.view.Viewer.ViewerBinder;
import org.teree.client.view.editor.Scene;
import org.teree.shared.data.Node;

public class Editor extends Composite implements MapEditor.Display {
	
	private static EditorBinder uiBinder = GWT.create(EditorBinder.class);

    interface EditorBinder extends UiBinder<Widget, Editor> {
    }

    @UiField
    Button btnNew;

    @UiField
    Button btnSave;
    
    @UiField(provided = true)
    Scene scene;
    
    @UiField
    Label status;
    
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
    public HasClickHandlers getNewButton() {
        return btnNew;
    }

    @Override
    public HasClickHandlers getSaveButton() {
        return btnSave;
    }

	@Override
	public void setRoot(Node root) {
		scene.setRoot(root);
	}

}
