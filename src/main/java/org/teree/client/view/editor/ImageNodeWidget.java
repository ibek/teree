package org.teree.client.view.editor;

import org.teree.client.Settings;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.shared.data.common.ImageLink;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeStyle;
import org.teree.shared.data.common.Node.NodeLocation;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public class ImageNodeWidget extends NodeWidget {

	interface Resources extends ClientBundle {
		@Source("../resource/img/load_image.png")
		ImageResource noImage();
	}

	private Image content;
	private Resources res = GWT.create(Resources.class);
	private ImageLink nodeContent;
	private ContentDialog contentDialog;

	public ImageNodeWidget(Node node) {
		super(node);
		
		nodeContent = (ImageLink)node.getContent();

		init();
		update();

		container.add(content);

	}

	private void init() {
		content = new Image();
		content.getElement().getStyle().setPadding(5.0, Unit.PX);
        
        content.getElement().setDraggable(Element.DRAGGABLE_TRUE);
        initDragging(content);

		content.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				nodeContent.setUrl(null);
				content.setUrl(res.noImage().getSafeUri());
			}
		});
		
		content.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
			}
		});
		
		content.setUrl(res.noImage().getSafeUri());
		
	}

	@Override
	public void update() {
		if (nodeContent != null) {
			final String url = nodeContent.getUrl();
			if (url != null && !url.isEmpty()) {
				content.setUrl(Settings.HOST + "getImage?url=" + url);
			} else {
				content.setUrl(res.noImage().getSafeUri());
			}
		}
	}

	@Override
	public void edit() {
		if (contentDialog == null) {
			contentDialog = new ContentDialog("Set image url");
			contentDialog.setTextFieldVisible(false);
			
			contentDialog.getOk().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					nodeContent.setUrl(contentDialog.getUrlField());
	            	update();
					contentDialog.hide();
				}
			});
			
		}
		
		contentDialog.setUrlField(nodeContent.getUrl());
		contentDialog.show();

		int x = 0;
		if (node.getLocation() == NodeLocation.LEFT) {
			x = -contentDialog.getOffsetWidth();
		} else {
			x = content.getOffsetWidth();
		}
		
		contentDialog.setPopupPosition(getAbsoluteLeft() + x, 
				getAbsoluteTop() + content.getOffsetHeight()/2 - contentDialog.getOffsetHeight()/2);
	}

    @Override
    public void draw(Context2d context, int x, int y) {
    	context.save();
    	context.drawImage(ImageElement.as(content.getElement()), x, y-content.getHeight());
        context.restore();
    }

	@Override
	public void changeStyle(NodeStyle style) {
		// nothing to be set
	}

}
