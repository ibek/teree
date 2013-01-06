package org.teree.client.view;

import javax.annotation.PostConstruct;

import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.presenter.SchemeEditor;
import org.teree.client.view.editor.EditPanel;
import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.Scene;
import org.teree.client.view.editor.event.SelectedNodeListener;
import org.teree.client.view.resource.PageStyle;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.Scheme;

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
    
    public Editor() {
    	scene = new Scene();
    }
    
    @PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
        bind();
    }
    
    private void bind() {
    	
    	edit.getRefreshButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.update(null);
			}
		});
    	
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
    	
    	edit.getCreateMathExprButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.createMathExpressionChildNode();
			}
		});
    	
    	edit.getCreateConnectorButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.createConnectorChildNode();
			}
		});
    	
    	edit.getMergeConnectorButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.mergeSelectedConnector();
			}
		});
    	
    	edit.getSplitConnectorButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.splitSelectedNode();
			}
		});
    	
    	edit.getBoldButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.changeBoldOfSelectedNode();
			}
		});
    	
    	edit.setSelectIconHandler(new EditPanel.SelectIcon() {
			@Override
			public void select(IconType icon) {
				scene.setSelectedIcon(icon);
			}
		});
    	
    	scene.addSelectedNodeListener(new SelectedNodeListener() {
			@Override
			public void selected(NodeWidget nw) {
				edit.checkSelectedNode(nw);
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
	public void setScheme(Scheme scheme) {
		scene.setScheme(scheme);
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
    public String getSchemeSamplePicture() {
        return scene.getSchemeSamplePicture();
    }

	@Override
	public void bold() {
		scene.changeBoldOfSelectedNode();
	}

}
