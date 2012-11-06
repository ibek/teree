package org.teree.client.view.editor;

import org.teree.client.Settings;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.shared.data.scheme.IconText;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;

/**
 * 
 * Parent of TextNodeWidget has to be AbsolutePanel.
 * 
 * @author ibek
 *
 */
public class TextNodeWidget extends NodeWidget {
	
	private Icon icon;
    private HTML content;
    private TextArea editContent;
    
    private IconText nodeContent;
    
    public TextNodeWidget(Node node) {
        super(node);

        nodeContent = (IconText)node.getContent();
		icon = new Icon();
        
        view();
        
    }
    
    @Override
    public void edit() {
        if (editContent == null) {
            editContent = new TextArea();
            
            editContent.addKeyUpHandler(new KeyUpHandler() {
                @Override
                public void onKeyUp(KeyUpEvent event) {
                    if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.isShiftKeyDown()){
                        confirmChanges();
                    }else if(event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE){
                        view();
                    }
                }
            });
            
            editContent.addKeyDownHandler(new KeyDownHandler() {
                @Override
                public void onKeyDown(KeyDownEvent event) {
                    if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.isShiftKeyDown()){
                        // don't create new line if it means confirmation
                        event.preventDefault();
                    }
                }
            });
            
            editContent.addBlurHandler(new BlurHandler() {
                @Override
                public void onBlur(BlurEvent event) {
                    confirmChanges();
                }
            });
            
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    editContent.setFocus(true);
                }
            });

	        editContent.setStylePrimaryName(resources.css().node());
	        editContent.addStyleName(resources.css().nodeEdit());
            
        }
        
        editContent.setText(nodeContent.getText());
        if (getOffsetWidth() <= Settings.NODE_MIN_WIDTH) {
        	editContent.setWidth((Settings.NODE_MIN_WIDTH+2)+"px");
        } else {
        	editContent.setWidth((getOffsetWidth()+2)+"px");
        }
        editContent.setHeight(getOffsetHeight()+"px");
        
        if (nodeContent.getIconType() != null) {
			editContent.getElement().getStyle().setPaddingLeft(Settings.ICON_WIDTH, Unit.PX);
        } else {
			editContent.getElement().getStyle().setPaddingLeft(0.0, Unit.PX);
        }
        
        // to ensure that the editContent will be focused after all events (key F2)
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                editContent.setFocus(true);
            }
        });
        
        container.remove(content);
        container.add(editContent);
        
        // to prevent the conflict between copy of text and nodes while this node is edited
        getParent().fireEvent(new SelectNode(null));
        
    }
    
    public void view() {
    	if (content == null) {
    		
	        content = new HTML(nodeContent.getText());
	        content.addClickHandler(new ClickHandler() {
	            @Override
	            public void onClick(ClickEvent event) {
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

    	update();
    	
    	if(getOffsetWidth() >= Settings.NODE_MAX_WIDTH){
            content.setWidth(Settings.NODE_MAX_WIDTH+"px");
        }
        
        if (editContent != null) {
            container.remove(editContent);
        }
        
        container.add(content);
        
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
    
    private void confirmChanges() {
        String newtext = editContent.getText();
        
        if (newtext.compareTo(nodeContent.getText()) != 0) {
        	nodeContent.setText(newtext);
        }
        
        view();
        fireSelect();
        
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
				getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
            }
        });
    }
    
    public void update() {
		String text = nodeContent.getText();
		if (text.isEmpty()) {
			text = "[empty]";
		}
		content.setText(text);
		if (nodeContent.getIconType() != null) {
			
			icon.setType(IconType.valueOf(nodeContent.getIconType()));
			
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
    public void draw(Context2d context, int x, int y) {
    	context.save();
    	context.setFont("14px monospace");
        context.setFillStyle("#000000");
    	if (nodeContent.getIconType() != null) {
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
