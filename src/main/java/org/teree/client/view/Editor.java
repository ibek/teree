package org.teree.client.view;

import javax.annotation.PostConstruct;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.presenter.MapEditor;
import org.teree.client.view.editor.EditPanel;
import org.teree.client.view.editor.Scene;
import org.teree.client.view.resource.ViewStyle;
import org.teree.shared.data.Node;

public class Editor extends Composite implements MapEditor.Display {
	
	private static EditorBinder uiBinder = GWT.create(EditorBinder.class);

    interface EditorBinder extends UiBinder<Widget, Editor> {
    }
    
	static {
		ViewStyle.INSTANCE.css().ensureInjected(); 
	}

    @UiField
    Button btnNew;

    @UiField
    Button btnSave;

    @UiField
    Label linkExplore;

    @UiField
    Label linkHelp;
    
    @UiField(provided = true)
    Scene scene;
    
    @UiField
    EditPanel edit;
    
    /**
     * TODO: make the status floatable on a visible place
     */
    @UiField
    Label status;
    
    @UiField
    UserWidget user;
    
    @PostConstruct
    public void init() {
    	scene = new Scene();
        initWidget(uiBinder.createAndBindUi(this));
        bind();
    }
    
    private void bind() {
    	edit.getCreateTextButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.createTextChildNode();
			}
		});
    	
    	edit.getCreateImgButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.createImageChildNode();
			}
		});
    	
    	edit.getBoldButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.changeBoldOfSelectedNode();
			}
		});
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

	@Override
	public void edit() {
		scene.editSelectedNode();
	}

	@Override
	public void delete() {
		scene.removeSelectedNode();
	}

	@Override
	public void insert() {
		scene.createTextChildNode();
	}

	@Override
	public void copy() {
		scene.copySelectedNode();
	}

	@Override
	public void cut() {
		scene.cutSelectedNode();
	}

	@Override
	public void paste() {
		scene.pasteNode();
	}

	@Override
	public void up() {
		scene.selectUpperNode();
	}

	@Override
	public void down() {
		scene.selectUnderNode();
	}

	@Override
	public void left() {
		scene.selectLeftNode();
	}

	@Override
	public void right() {
		scene.selectRightNode();
	}

	@Override
	public void info(String msg) {
		status.getElement().getStyle().setColor("black");
		setStatus(msg);
	}

	@Override
	public void error(String msg) {
		status.getElement().getStyle().setColor("red");
		setStatus(msg);
	}
	
	private void setStatus(String msg) {
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
	public HasClickHandlers getExploreLink() {
		return linkExplore;
	}

	@Override
	public HasClickHandlers getHelpLink() {
		return linkHelp;
	}

}
