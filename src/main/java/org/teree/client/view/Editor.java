package org.teree.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Divider;
import com.github.gwtbootstrap.client.ui.NavHeader;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Thumbnail;
import com.github.gwtbootstrap.client.ui.WellNavList;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.CurrentPresenter;
import org.teree.client.view.editor.EditPanel;
import org.teree.client.view.editor.NodeCategoryDialog;
import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.Scene;
import org.teree.client.view.editor.event.SelectedNodeListener;
import org.teree.shared.data.common.NodeCategory;
import org.teree.shared.data.common.Scheme;

public class Editor extends TemplateScene implements org.teree.client.presenter.Editor.Display {
	
	private static EditorBinder uiBinder = GWT.create(EditorBinder.class);

    interface EditorBinder extends UiBinder<Widget, Editor> {
    }
    
    @UiField(provided = true)
    Scene scene;
    
    @UiField
    EditPanel edit;
    
    @UiField
    Frame tmpFrame;
    
    @UiField
    WellNavList nodeCategory;
    
    private Map<String, NavLink> categories;
    private NavLink activeCategory;
    private NodeCategoryDialog ncg;
    
    public Editor() {
    	scene = new Scene();
    }
    
    @PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
        bind();
        scene.setTmpFrame(tmpFrame);
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
    	
    	edit.getCreatePercentButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.createPercentChildNode();
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
				scene.splitSelectedNode(tmpFrame);
			}
		});
    	
    	edit.getCategoriesButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (categories != null && !categories.isEmpty()) {
					nodeCategory.setVisible(!nodeCategory.isVisible());
				} else {
					CurrentPresenter.getInstance().getPresenter().getNodeCategories(new RemoteCallback<List<NodeCategory>>() {
						@Override
						public void callback(List<NodeCategory> response) {
							if (response != null) {
								setNodeCategories(response);
							}
						}
					});
				}
			}
		});
    	
    	edit.setSelectIconHandler(new EditPanel.SelectIcon() {
			@Override
			public void select(IconType icon) {
				scene.setNodeIcon(icon);
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
    	
    	scene.addSelectedNodeListener(new SelectedNodeListener<NodeWidget>() {
			@Override
			public void selected(NodeWidget nw) {
				edit.checkSelectedNode(nw);
				NodeCategory nc = null;
				if (nw != null) {
					nc = nw.getNode().getCategory();
				}
				setActiveNodeCategory(nc);
			}
		});
    	
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

	private void setNodeCategories(List<NodeCategory> categories) {
		nodeCategory.clear();
		NavHeader header = new NavHeader("Node Categories");
		header.getElement().getStyle().setProperty("textAlign", "center");
		nodeCategory.add(header);
		if (this.categories == null) {
			this.categories = new HashMap<String, NavLink>();
		} else {
			this.categories.clear();
		}
		for (NodeCategory nc : categories) {
			addNodeCategory(nc);
			nodeCategory.add(this.categories.get(nc.getOid()));
		}
		NavLink addNodeCategory = new NavLink();
		addNodeCategory.getElement().getStyle().setProperty("textAlign", "center");
		addNodeCategory.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showNodeCategoryDialog(null);
			}
		});
		addNodeCategory.setBaseIcon(IconType.PLUS);
		nodeCategory.add(addNodeCategory);
		nodeCategory.setVisible(true);
	}
	
	private void addNodeCategory(final NodeCategory nc) {
		NavLink nl = new NavLink(nc.getName());
		nl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				NodeWidget nw = scene.getSelectedNodeWidget();
				if (nw != null) {
					nw.setNodeCategory(nc);
					setActiveNodeCategory(nc);
				}
			}
		});
		NavLink removeIcon = new NavLink();
		removeIcon.setBaseIcon(IconType.REMOVE);
		removeIcon.getElement().getStyle().setFloat(Float.RIGHT);
		removeIcon.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				CurrentPresenter.getInstance().getPresenter().removeNodeCategory(nc, new RemoteCallback<Boolean>() {
	                @Override
	                public void callback(Boolean response) {
	                	if (response == null || response) {
	                		// TODO remove categories from nodes
	                		// TODO remove category from list
	                    	info("Removed node category");
	                	} else {
	                		error("Node category wasn't removed");
	                    }
	                }
	            });
			}
		});
		nl.add(removeIcon);
		NavLink editIcon = new NavLink();
		editIcon.setBaseIcon(IconType.EDIT);
		editIcon.getElement().getStyle().setFloat(Float.RIGHT);
		editIcon.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				showNodeCategoryDialog(nc);
			}
		});
		nl.add(editIcon);
		this.categories.put(nc.getOid(), nl);
	}
    
    private void setActiveNodeCategory(NodeCategory nc) {
		if (activeCategory != null) {
			activeCategory.setActive(false);
			activeCategory = null;
		}
    	if (categories != null && nc != null) {
    		NavLink nl = categories.get(nc.getOid());
    		if (nl != null) {
    			activeCategory = nl;
    			activeCategory.setActive(true);
    		}
    	}
    }
    
    private void showNodeCategoryDialog(final NodeCategory nc) {
		ncg = new NodeCategoryDialog();
		ncg.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final NodeCategory newnc = ncg.getNodeCategory();
				if (newnc.getOid() == null) {
					CurrentPresenter.getInstance().getPresenter().insertNodeCategory(newnc, new RemoteCallback<String>() {
		                @Override
		                public void callback(String response) {
		                	newnc.setOid(response);
		                    addNodeCategory(newnc);
		        			nodeCategory.insert(categories.get(newnc.getOid()), categories.size());
		                    info("Created node category");
		                }
		            });
				} else {
					CurrentPresenter.getInstance().getPresenter().updateNodeCategory(newnc, new RemoteCallback<Void>() {
			            @Override
			            public void callback(Void response) {
							nc.set(newnc);
							// TODO promote the change into node widgets
			                info("Updated node category");
			            }
			        });
				}
				ncg.hide();
			}
		});
    	ncg.setNodeCategory(nc);
    	ncg.show();
    }

}
