package org.teree.client.view.editor;

import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.shared.data.ImageLink;
import org.teree.shared.data.Node;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.ImageElement;
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

	private LinkDialog linkDialog;

	private Resources res = GWT.create(Resources.class);
	
	private boolean firstRender = true;

	public ImageNodeWidget(Node node) {
		super(node);

		init();
		update();

		container.add(content);

	}

	private void init() {
		content = new Image(res.noImage());

		content.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
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
				((ImageLink) node.getContent()).setUrl(null);
				content.setResource(res.noImage());
			}
		});
		
		content.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				if (!firstRender) {
					getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
				} else {
					firstRender = false;
				}
			}
		});
		
	}

	public void update() {
		String url = ((ImageLink) node.getContent()).getUrl();
		if (url != null) {
			content.setUrl(url);
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
		if (linkDialog == null) {
			linkDialog = new LinkDialog("Set image link");
			
			linkDialog.getOk().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					((ImageLink)node.getContent()).setUrl(linkDialog.getUrl());
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			            @Override
			            public void execute() {
							update();
			            }
			        });
					linkDialog.hide();
				}
			});
			
		}

		linkDialog.setPopupPosition(getAbsoluteLeft() + content.getWidth()/2 - linkDialog.getOffsetWidth()/2, 
				getAbsoluteTop() + content.getHeight()/2 - linkDialog.getOffsetHeight()/2);
		linkDialog.show();
	}

    @Override
    public void draw(Context2d context, int x, int y) {
        context.drawImage(ImageElement.as(content.getElement()), x, y);
    }

}
