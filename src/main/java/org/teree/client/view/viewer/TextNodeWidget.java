package org.teree.client.view.viewer;

import org.teree.client.Settings;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.shared.data.scheme.IconText;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.HTML;

public class TextNodeWidget extends NodeWidget {

	private Icon icon;
    private HTML content;
    private boolean collapsed;
    
    protected TextNodeWidget(Node node) {
        super(node);
        init();
    }
    
    public void init() {
        content = new HTML();
        content.setText(node.getContent().toString());
        content.setStylePrimaryName(resources.css().node());
        content.setStyleDependentName("view", true);
        
    	icon = new Icon();
    	String iconType = ((IconText)node.getContent()).getIconType();
    	if (iconType != null) {
	    	icon.setType(IconType.valueOf(iconType));
	    	container.insert(icon, 0, 0, 0);
			content.getElement().getStyle().setPaddingLeft(Settings.ICON_WIDTH, Unit.PX);
    	}
    	
    	content.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.stopPropagation(); // it prevents map moving
			}
		});
    	
        content.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO: add support for collapse
            }
        });
    	
        // set node style
		NodeStyle ns = node.getStyleOrCreate();
		
		if (ns.isBold()) {
			getElement().getStyle().setFontWeight(FontWeight.BOLD);
		} else {
			getElement().getStyle().setFontWeight(FontWeight.NORMAL);
		}
		
		// at start all nodes are collapsed
		if (node.getParent() != null && node.getParent().getParent() == null && node.getNumberOfChildNodes() > 0) {
			setCollapsed(true);
		}
        
        container.add(content);
    }

    @Override
    public void draw(Context2d context, int x, int y) {
    	context.save();
    	context.setFont("14px monospace");
        context.setFillStyle("#000000");
        if (icon.getIconType() != null) {
        	context.setFont("14px FontAwesome");
        	String c = "";
        	c += IconTypeContent.get(icon.getIconType());
        	context.fillText(c, x, y);
        	context.setFont("14px monospace");
            context.fillText(content.getText(), x+Settings.ICON_WIDTH, y);
    	} else {
            context.fillText(content.getText(), x, y);
    	}
        context.restore();
    }
	
	@Override
	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		if (collapsed) {
			content.setText("+ "+node.getContent().toString());
		} else {
			content.setText(node.getContent().toString());
		}
		this.collapsed = collapsed;
	}

}
