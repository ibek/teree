package org.teree.client.view.editor;

import org.teree.client.Settings;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.shared.data.scheme.ImageLink;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;
import org.teree.shared.data.scheme.Node.NodeLocation;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
		@Source("../resource/load_image.png")
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

		content.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				if (selected) {
					edit();
				} else { // first click - select this node
	                fireSelect();
	            }
			}
		});

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

	public void update() {
		final String url = nodeContent.getUrl();
		if (url != null) {
			content.setUrl(Settings.HOST + "getImage?url=" + url);
		}
	}
	
	private void fireSelect() {
    	getParent().fireEvent(new SelectNode(this));
    }
    
    @Override
    public NodeWidget select() {
    	content.getElement().getStyle().setBackgroundColor("grey");
    	return super.select();
    }
    
    @Override
    public NodeWidget unselect() {
    	content.getElement().getStyle().setBackgroundColor(null);
    	return super.unselect();
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

		int x = 0;
		if (node.getLocation() == NodeLocation.LEFT) {
			x = -contentDialog.getOffsetWidth() - content.getOffsetWidth();
		} else {
			x = content.getOffsetWidth();
		}
		contentDialog.setPopupPosition(getAbsoluteLeft() + x, 
				getAbsoluteTop() - content.getOffsetHeight()/2 - contentDialog.getOffsetHeight()/2);
		
		contentDialog.setUrlField(nodeContent.getUrl());
		
		contentDialog.show();
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
