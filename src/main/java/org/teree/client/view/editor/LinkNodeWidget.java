package org.teree.client.view.editor;

import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.shared.data.common.Link;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeStyle;
import org.teree.shared.data.common.Node.NodeLocation;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class LinkNodeWidget extends NodeWidget {

	private Label content;
	private ContentDialog linkDialog;
	private Link nodeContent;

	public LinkNodeWidget(Node node) {
		super(node);
		
		nodeContent = (Link)node.getContent();

		init();
		update();

		container.add(content);

	}

	private void init() {
		content = new Label();
        content.setStylePrimaryName(resources.css().node());
        content.setStyleDependentName("view", true);

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
		
	}

	public void update() {
		String text = nodeContent.getText();
		if (text == null || text.isEmpty()) {
			text = nodeContent.getUrl();
			if (text == null || text.isEmpty()) {
				text = "link";
			}
		}
		content.setText("@"+text);
	}
	
	private void fireSelect() {
    	getParent().fireEvent(new SelectNode(this));
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
    	context.save();
    	context.setFont("14px monospace");
        context.setFillStyle("#000000");
        context.fillText(content.getText(), x, y);
        context.restore();
    }

	@Override
	public void changeStyle(NodeStyle style) {
		if (style == null) {
			return;
		}
		
		NodeStyle ns = node.getStyleOrCreate();
		
		if (style.isBold()) {
			ns.setBold(true);
			getElement().getStyle().setFontWeight(FontWeight.BOLD);
		} else {
			ns.setBold(false);
			getElement().getStyle().setFontWeight(FontWeight.NORMAL);
		}
	}

}
