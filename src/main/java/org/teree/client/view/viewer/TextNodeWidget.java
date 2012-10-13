package org.teree.client.view.viewer;

import org.teree.client.Settings;
import org.teree.shared.data.scheme.IconText;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;

public class TextNodeWidget extends NodeWidget {

	private Icon icon;
    private HTML content;
    
    protected TextNodeWidget(Node node) {
        super(node);
        init();
    }
    
    public void init() {
        content = new HTML(node.getContent().toString());
        content.setStylePrimaryName(resources.css().node());
        content.setStyleDependentName("view", true);
        
    	icon = new Icon();
    	String iconType = ((IconText)node.getContent()).getIconType();
    	if (iconType != null) {
	    	icon.setType(IconType.valueOf(iconType));
	    	container.insert(icon, 0, 0, 0);
			content.getElement().getStyle().setPaddingLeft(Settings.ICON_WIDTH, Unit.PX);
    	}
    	
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
        
        container.add(content);
    }

    @Override
    public void draw(Context2d context, int x, int y) {
    	context.save();
    	context.setFont("14px monospace");
        context.setFillStyle("#000000");
        if (icon.getIconType() != null) {
    		// FIXME: draw icon as text with FontAwesome
        	//context.setFont("14px FontAwesome");
        	//context.fillText(new String(new byte[]{0x04, (byte)0xf0}), x, y);
            context.fillText(content.getText(), x+Settings.ICON_WIDTH, y);
    	} else {
            context.fillText(content.getText(), x, y);
    	}
        context.restore();
    }

}
