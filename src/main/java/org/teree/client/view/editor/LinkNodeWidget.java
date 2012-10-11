package org.teree.client.view.editor;

import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.shared.data.scheme.Link;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;
import org.teree.shared.data.scheme.Node.NodeLocation;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class LinkNodeWidget extends NodeWidget {

	private Label content;

	private LinkDialog linkDialog;

	public LinkNodeWidget(Node node) {
		super(node);

		init();
		update();

		container.add(content);

	}

	private void init() {
		content = new Label();
		

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
		
	}

	public void update() {
		Link link = (Link) node.getContent();
		String text = link.getText();
		if (text == null || text.isEmpty()) {
			text = "link";
		}
		content.setText("@"+text);
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
			linkDialog = new LinkDialog("Set link");
			
			linkDialog.getOk().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Link link = (Link)node.getContent();
					link.setText(linkDialog.getTextField());
					link.setUrl(linkDialog.getUrl());
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

		int x = 0;
		if (node.getLocation() == NodeLocation.LEFT) {
			x = -linkDialog.getOffsetWidth() - content.getOffsetWidth();
		} else {
			x = content.getOffsetWidth();
		}
		linkDialog.setPopupPosition(getAbsoluteLeft() + x, 
				getAbsoluteTop() - content.getOffsetHeight()/2 - linkDialog.getOffsetHeight()/2);
		linkDialog.show();
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
