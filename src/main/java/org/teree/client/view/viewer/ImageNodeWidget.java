/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.client.view.viewer;

import org.teree.client.Settings;
import org.teree.client.view.common.NodePainter;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.shared.data.common.ImageLink;
import org.teree.shared.data.common.Node;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Unit;
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

	public ImageNodeWidget() {

	}

	public ImageNodeWidget(Node node) {
		super(node);

		init();
		container.add(content);
		update();

	}

	private void init() {
		content = new Image();
        content.setStylePrimaryName(resources.css().node());
		content.getElement().getStyle().setPadding(5.0, Unit.PX);

		content.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				((ImageLink) node.getContent()).setUrl(null);
				content.setUrl(res.noImage().getSafeUri());
			}
		});
		
		content.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
			}
		});
	}
	
	@Override
	public void update() {
		super.update();
		
		final String url = ((ImageLink) node.getContent()).getUrl();
		if (url != null && !url.isEmpty()) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	            @Override
	            public void execute() {
	    			content.setUrl(Settings.HOST + "getImage?url=" + url);
	            }
	        });
		} else {
			content.setUrl(res.noImage().getSafeUri());
		}
	}

    @Override
    public void draw(Context2d context, int x, int y) {
		NodePainter.drawImageNode(context, x, y,
				ImageElement.as(content.getElement()), getOffsetWidth(), content.getHeight(),
				node.getCategory());
    }

}
