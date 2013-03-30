package org.teree.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Divider;
import com.github.gwtbootstrap.client.ui.NavHeader;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavList;
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
import org.teree.shared.data.common.Node;
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
    
    private Map<String, NavLink> categoryLinks;
    private Map<String, NodeCategory> nodeCategories;
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
				scene.getController().update(null);
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
				scene.getController().mergeConnectorNode();
			}
		});
    	
    	edit.getSplitConnectorButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scene.getController().splitAndConnectNode();
			}
		});
    	
    	edit.getCategoriesButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (categoryLinks != null && !categoryLinks.isEmpty()) {
					nodeCategory.setVisible(!nodeCategory.isVisible());
				} else {
					CurrentPresenter.getInstance().getPresenter().getNodeCategories(new RemoteCallback<List<NodeCategory>>() {
						@Override
						public void callback(List<NodeCategory> response) {
							if (response != null) {
								setCategoryLinks(response);
							}
						}
					});
				}
			}
		});
    	
    	edit.setSelectIconHandler(new EditPanel.SelectIcon() {
			@Override
			public void select(IconType icon) {
				scene.getController().setNodeIcon(icon);
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
    	
    	scene.getController().addSelectedNodeListener(new SelectedNodeListener<NodeWidget>() {
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
    	
    	if (nodeCategories == null) {
    		nodeCategories = new HashMap<String, NodeCategory>();
    	}
    	nodeCategories.clear();
    	setNodeCategories(scheme.getFirst());
    	
	}

	@Override
	public void edit() {
		scene.getController().editNode();
	}

	@Override
	public void delete() {
    	scene.getController().removeNode();
    	scene.getController().update(null);
	}

	@Override
	public void insert() {
		scene.createTextChildNode();
	}

	@Override
	public void copy() {
		scene.getController().copyNode();
	}

	@Override
	public void cut() {
		scene.getController().cutNode();
	}

	@Override
	public void paste() {
		scene.getController().pasteNode();
	}

	@Override
	public void up() {
		scene.getController().selectUpperNode();
	}

	@Override
	public void down() {
		scene.getController().selectUnderNode();
	}

	@Override
	public void left() {
		scene.getController().selectLeftNode();
	}

	@Override
	public void right() {
		scene.getController().selectRightNode();
	}

    @Override
    public String getSchemeSamplePicture() {
        return scene.getSchemeSamplePicture();
    }

	private void setCategoryLinks(List<NodeCategory> categories) {
		nodeCategory.clear();
		NavHeader header = new NavHeader("Node Categories");
		header.getElement().getStyle().setProperty("textAlign", "center");
		nodeCategory.add(header);
		if (this.categoryLinks == null) {
			this.categoryLinks = new HashMap<String, NavLink>();
		} else {
			this.categoryLinks.clear();
		}
		for (NodeCategory nc : categories) {
			addNodeCategory(nc);
			nodeCategory.add(this.categoryLinks.get(nc.getOid()));
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
				NodeWidget nw = scene.getController().getSelectedNode();
				if (nw != null) {
					Node n = nw.getNode();
					if (nc.getOid() != null && n.getCategory() != null && nc.getOid().equals(n.getCategory().getOid())) { // reset
						nw.setNodeCategory(new NodeCategory());
						setActiveNodeCategory(null);
					} else {
						nw.setNodeCategory(nc);
						setActiveNodeCategory(nc);
						if (nw.getNode().getParent() == null) { // root
							scene.getController().update(null);
						}
					}
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
				final String oid = nc.getOid(); 
				CurrentPresenter.getInstance().getPresenter().removeNodeCategory(nc, new RemoteCallback<Boolean>() {
	                @Override
	                public void callback(Boolean response) {
	                	if (response == null || response) {
	                		((NavList)nodeCategory.getWidget(0)).remove(categoryLinks.get(oid)); // remobe category from list
	                		NodeCategory old = nodeCategories.get(oid);
	                		if (old == null) {
	                			old = nc;
	                		} else {
	                			nodeCategories.remove(oid); // remove category from map where are all the categories loaded in scheme
	                		}
                			old.set(new NodeCategory()); // reset category
	                		categoryLinks.remove(oid);
							scene.getController().checkAllNodes();
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
		this.categoryLinks.put(nc.getOid(), nl);
	}
    
    private void setActiveNodeCategory(NodeCategory nc) {
		if (activeCategory != null) {
			activeCategory.setActive(false);
			activeCategory = null;
		}
    	if (categoryLinks != null && nc != null) {
    		NavLink nl = categoryLinks.get(nc.getOid());
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
				if (newnc == null) {
					return;
				}
				if (newnc.getOid() == null) {
					CurrentPresenter.getInstance().getPresenter().insertNodeCategory(newnc, new RemoteCallback<String>() {
		                @Override
		                public void callback(String response) {
		                	newnc.setOid(response);
		                    addNodeCategory(newnc);
		        			nodeCategory.insert(categoryLinks.get(newnc.getOid()), categoryLinks.size());
		                    info("Created node category");
		                }
		            });
				} else {
					CurrentPresenter.getInstance().getPresenter().updateNodeCategory(newnc, new RemoteCallback<Void>() {
			            @Override
			            public void callback(Void response) {
							nc.set(newnc); // update the node category that was newly set
							NodeCategory old = nodeCategories.get(newnc.getOid());
							if (old != null) {
								old.set(newnc); // update another instance of node category set in scheme
							}
							categoryLinks.get(newnc.getOid()).setText(newnc.getName());
							scene.getController().checkAllNodes();
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
    
    private void setNodeCategories(Node root) {
    	NodeCategory nc = root.getCategory();
    	if (nc != null && nc.getName() != null) {
    		NodeCategory nc2 = nodeCategories.get(nc.getOid());
    		if (nc2 == null) {
    			nodeCategories.put(nc.getOid(), nc);
    		}
    	}
    	List<Node> childNodes = root.getChildNodes();
    	for (int i=0; childNodes != null && i<childNodes.size(); ++i) {
    		setNodeCategories(childNodes.get(i));
    	}
    }

}
