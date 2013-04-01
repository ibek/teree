package org.teree.client.view.editor;

import org.teree.client.view.resource.icon.CreateIcons;
import org.teree.client.view.resource.icon.CustomIcons;
import org.teree.shared.NodeGenerator;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.StructureType;
import org.teree.shared.data.tree.Tree;
import org.teree.shared.data.tree.TreeType;

import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.Tab;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.github.gwtbootstrap.client.ui.ThumbnailLink;
import com.github.gwtbootstrap.client.ui.Thumbnails;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.Bootstrap.Tabs;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

public class CreateDialog extends Composite {

	private Modal window;

	private TabPanel tabPanel;
	private Tab tree;
	private Thumbnails treeContainer;
	private StructureType filter;

	private CreateSchemeHandler handler;

	private CreateIcons createIcons = GWT.create(CreateIcons.class);

	public CreateDialog() {
		window = new Modal(false);
		window.setTitle("Create Scheme");

		tabPanel = new TabPanel(Tabs.ABOVE);
		tree = new Tab();
		tree.setActive(true);
		tree.setIcon(IconType.SITEMAP);
		tree.setHeading("Tree");

		treeContainer = new Thumbnails();
		initTreeContainer();
		tree.add(treeContainer);

		window.add(tabPanel);

	}

	public void show() {
		show(null);
	}
	
	public void show(StructureType filter) {
		tabPanel.clear();
		if (filter == null || filter == StructureType.Tree) {
			tabPanel.add(tree);
		}
		window.show();
	}

	public void hide() {
		window.hide();
	}

	public void setCreateSchemeHandler(CreateSchemeHandler handler) {
		this.handler = handler;
	}

	private void initTreeContainer() {
		treeContainer.clear();
		Style css = treeContainer.getElement().getStyle();
		css.setPaddingLeft(35, Unit.PX);
		css.setPaddingRight(5, Unit.PX);

		SchemeTypeWidget mindmap = new SchemeTypeWidget(getMindMapScheme(),
				createIcons.mindmap());
		treeContainer.add(mindmap);
		
		SchemeTypeWidget horizontalHierarchy = new SchemeTypeWidget(getHorizontalHierarchy(),
				createIcons.horizontalHierarchy());
		treeContainer.add(horizontalHierarchy);
		
		/**SchemeTypeWidget verticalHierarchy = new SchemeTypeWidget(getVerticalHierarchy(),
				createIcons.horizontalHierarchy());
		treeContainer.add(verticalHierarchy);*/

	}

	private Scheme getMindMapScheme() {
		Tree mm = new Tree();
		mm.setRoot(NodeGenerator.mindmap());
		mm.setVisualization(TreeType.MindMap);
		return mm;
	}

	private Scheme getHorizontalHierarchy() {
		Tree mm = new Tree();
		mm.setRoot(NodeGenerator.horizontalHierarchy());
		mm.setVisualization(TreeType.HorizontalHierarchy);
		return mm;
	}

	private Scheme getVerticalHierarchy() {
		Tree mm = new Tree();
		mm.setRoot(NodeGenerator.horizontalHierarchy());
		mm.setVisualization(TreeType.VerticalHierarchy);
		return mm;
	}

	private class SchemeTypeWidget extends Composite {

		private static final int WIDTH = 100;
		private static final int HEIGHT = 100;

		private ThumbnailLink th;

		private Scheme scheme;
		private Image image;

		public SchemeTypeWidget(Scheme scheme, ImageResource image) {
			this.scheme = scheme;
			th = new ThumbnailLink();
			th.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					handler.create(SchemeTypeWidget.this.scheme);
					hide();
				}
			});
			th.setSize(WIDTH + "px", HEIGHT + "px");
			th.getElement().getStyle().setMargin(5.0, Unit.PX);
			th.getAnchor().getElement().getStyle().setBackgroundColor("white");
			th.getAnchor().getElement().getStyle()
					.setProperty("textAlign", "center");

			this.image = new Image(image);
			th.add(this.image);

			initWidget(th);
			th.getAnchor().setSize(WIDTH + "px", HEIGHT + "px");
		}

	}

	public static interface CreateSchemeHandler {
		public void create(Scheme scheme);
	}

}
