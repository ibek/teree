package org.teree.client.view.viewer;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.shared.data.scheme.IconText;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.HTML;

public class TextNodeWidget extends NodeWidget {

	protected Icon icon;
    protected HTML content;
    
    protected TextNodeWidget(Node node) {
        super(node);
        init();
    }
    
    private void init() {
        content = new HTML();
        content.setText(node.getContent().toString());
        content.setStylePrimaryName(resources.css().node());
        content.addStyleName(resources.css().nodeView());
        
    	icon = new Icon();
    	if (node.getContent() instanceof IconText) {
    		setIconType((IconText)node.getContent());
    	}
    	
    	content.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.stopPropagation(); // it prevents map moving
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
    
    protected void setIconType(IconText it) {
    	String iconType = it.getIconType();
    	if (iconType != null) {
	    	icon.setType(IconType.valueOf(iconType));
	    	container.insert(icon, 0, 0, 0);
			content.getElement().getStyle().setPaddingLeft(Settings.ICON_WIDTH, Unit.PX);
    	}
    }

    @Override
    public void draw(Context2d context, int x, int y) {
    	context.save();
    	context.setFont("14px monospace");
        context.setFillStyle("#000000");
        
        String text = content.getText();
        String[] words = text.split(" ");
        String line = "";
        int lineHeight = 14;
        int mw = getOffsetWidth() + 10;
        int py = y;
        List<Integer> ly = new ArrayList<Integer>();
        List<String> ls = new ArrayList<String>();
        for (int n=0; n<words.length; ++n) {
        	String testLine = line + words[n] + " ";
        	TextMetrics tm = context.measureText(testLine);
        	double testWidth = tm.getWidth();
        	if (testWidth > mw) {
        		ls.add(line);
        		ly.add(py);
                line = words[n] + ' ';
                py += lineHeight;
        	} else {
                line = testLine;
            }
        }
		ls.add(line);
		ly.add(py);
		int m = ly.size()*lineHeight - lineHeight;
		
		if (icon.getIconType() != null) {
        	x += Settings.ICON_WIDTH;
		}
		
		for (int i=0; i<ly.size(); ++i) {
	        context.fillText(ls.get(i), x, ly.get(i) - m);
		}
		
        if (icon.getIconType() != null) {
        	context.setFont("14px FontAwesome");
        	String c = "";
        	c += IconTypeContent.get(icon.getIconType());
        	context.fillText(c, x - Settings.ICON_WIDTH, y - m);
        	context.setFont("14px monospace");
    	}
        
        context.restore();
    }
    
    @Override
    public void update() {
    	super.update();
    	if(getOffsetWidth() >= Settings.NODE_MAX_WIDTH){
            content.setWidth(Settings.NODE_MAX_WIDTH+"px");
        }
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
