package org.teree.client.view.editor;

import org.teree.client.view.common.NodePainter;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.shared.data.common.Link;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Node.NodeLocation;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

public class LinkNodeWidget extends NodeWidget {

	private Label content;
	private ContentDialog linkDialog;
	private Link nodeContent;

	public LinkNodeWidget(Node node) {
		super(node);
		
		nodeContent = (Link)node.getContent();

		init();
		container.add(content);
		update();

	}

	private void init() {
		content = new Label();
        content.setStylePrimaryName(resources.css().node());
        content.addStyleName(resources.css().nodeView());
        
        content.getElement().setDraggable(Element.DRAGGABLE_TRUE);
        initDragging(content);
	}

	@Override
	public void update() {
    	super.update();
		String text = nodeContent.getText();
		if (text == null || text.isEmpty()) {
			text = nodeContent.getUrl();
			if (text == null || text.isEmpty()) {
				text = "link";
			}
		}
		content.setText("@"+text);
	}

	@Override
	public void edit() {
		if (linkDialog == null) {
			linkDialog = new ContentDialog("Set link");
			
			linkDialog.getOk().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					nodeContent.setText(linkDialog.getTextField());
					nodeContent.setUrl(linkDialog.getUrlField());
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			            @Override
			            public void execute() {
							update();
							getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
			            }
			        });
					linkDialog.hide();
				}
			});
			
		}

		linkDialog.setUrlField(nodeContent.getUrl());
		linkDialog.setTextField(nodeContent.getText());
		
		linkDialog.show();

		int x = 0;
		if (node.getLocation() == NodeLocation.LEFT) {
			x = -linkDialog.getOffsetWidth();
		} else {
			x = content.getOffsetWidth();
		}
		
		linkDialog.setPopupPosition(getAbsoluteLeft() + x, 
				getAbsoluteTop() + content.getOffsetHeight()/2 - linkDialog.getOffsetHeight()/2);
	}

    @Override
    public void draw(Context2d context, int x, int y) {
		NodePainter.drawSingleLine(context, x, y, content.getText(),
				getOffsetWidth(), getOffsetHeight(), node.getCategory());
    }

}
