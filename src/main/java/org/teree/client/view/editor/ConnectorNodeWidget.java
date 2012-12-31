package org.teree.client.view.editor;

import org.teree.client.Settings;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.shared.data.scheme.Connector;
import org.teree.shared.data.scheme.IconText;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;
import org.teree.shared.data.scheme.Node.NodeLocation;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

public class ConnectorNodeWidget extends NodeWidget {

	private Icon icon;
    private HTML content;
	private Connector nodeContent;
	private ConnectorDialog connectorDialog;

	public ConnectorNodeWidget(Node node) {
		super(node);
		
		nodeContent = (Connector)node.getContent();
		icon = new Icon();
		icon.getElement().getStyle().setZIndex(100);
        
        view();

		container.add(content);

	}

	public void view() {
    	if (content == null) {
    		
	        content = new HTML(nodeContent.toString());
	        content.addClickHandler(new ClickHandler() {
	            @Override
	            public void onClick(ClickEvent event) {
	            	event.stopPropagation();
	                if (selected) { // second click - edit this node
	                    edit();
	                } else { // first click - select this node
	                    fireSelect();
	                }
	            }
	        });
	        
	        content.getElement().setDraggable(Element.DRAGGABLE_TRUE);
	        initDragging(content);
	        
	        content.setStylePrimaryName(resources.css().node());
	        content.addStyleName(resources.css().nodeView());
        
    	}
    	container.getElement().getStyle().setMarginLeft(0, Unit.PX);
    	container.setWidth("auto");

    	update();
    	
    	if(getOffsetWidth() >= Settings.NODE_MAX_WIDTH){
            content.setWidth(Settings.NODE_MAX_WIDTH+"px");
        }
        
        container.add(content);
        
    }
    
    private void fireSelect() {
    	getParent().fireEvent(new SelectNode(this));
    }
	
    public void update() {
		String text = nodeContent.toString();
		if (text.isEmpty()) {
			text = "[connector]";
		}
		content.setText("╠═" + text);
		IconText root = nodeContent.getRoot();
		if (root != null && root.getIconType() != null) {
			
			icon.setType(IconType.valueOf(root.getIconType()));
			
			if (container.getWidgetIndex(icon) < 0) {
				container.insert(icon, 0, 0, 0);
				content.getElement().getStyle().setPaddingLeft(Settings.ICON_WIDTH, Unit.PX);
			}
		} else {
			if (container.getWidgetIndex(icon) >= 0) {
				container.remove(icon);
				content.getElement().getStyle().setPaddingLeft(0.0, Unit.PX);
			}
		}
    }

	@Override
	public void edit() {
		if (connectorDialog == null) {
			connectorDialog = new ConnectorDialog();
			
			connectorDialog.getOk().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					nodeContent.setOid(connectorDialog.getOid());
					// also update root, the dialog should have it loaded already
					update();
				}
			});
		}
		connectorDialog.setOid(nodeContent.getOid());
		
		connectorDialog.show();

		int x = 0;
		if (node.getLocation() == NodeLocation.LEFT) {
			x = -connectorDialog.getOffsetWidth();
		} else {
			x = content.getOffsetWidth();
		}
		
		connectorDialog.setPopupPosition(getAbsoluteLeft() + x, 
				getAbsoluteTop() + content.getOffsetHeight()/2 - connectorDialog.getOffsetHeight()/2);
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
