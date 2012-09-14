package org.teree.client.view;

import javax.annotation.PostConstruct;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
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

import org.teree.client.Text;
import org.teree.client.presenter.SchemeEditor;
import org.teree.client.view.editor.EditPanel;
import org.teree.client.view.editor.Scene;
import org.teree.client.view.resource.PageStyle;
import org.teree.shared.data.Node;

public class Editor extends TemplateScene implements SchemeEditor.Display {
	
	private static EditorBinder uiBinder = GWT.create(EditorBinder.class);

    interface EditorBinder extends UiBinder<Widget, Editor> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected(); 
	}
    
    @UiField(provided = true)
    Scene scene;
    
    @UiField
    EditPanel edit;
    
    @UiField
    Alert status;
    
    public Editor() {
    	scene = new Scene();
    }
    
    @PostConstruct
    public void init() {
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
    	
    	edit.getCreateLinkButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.createLinkChildNode();
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
    public HasClickHandlers getSaveButton() {
        return edit.getSaveButton();
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
		status.setType(AlertType.INFO);
		setStatus(msg);
	}

	@Override
	public void error(String msg) {
		status.setType(AlertType.ERROR);
		setStatus(msg);
	}
	
	private void setStatus(String msg) {
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
    public String getSchemeSamplePicture() {
        return scene.getSchemeSamplePicture();
    }

	@Override
	public void bold() {
		scene.changeBoldOfSelectedNode();
	}

}
