package org.teree.client.view.editor;

import org.teree.client.Settings;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
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

    private HTML content;
    
    private TextArea editContent;
    
    public TextNodeWidget(Node node) {
        super(node);
        
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
        
        editContent.setText(node.getContent().toString());
        if (getOffsetWidth() <= Settings.MIN_WIDTH) {
        	editContent.setWidth((Settings.MIN_WIDTH+2)+"px");
        } else {
        	editContent.setWidth((getOffsetWidth()+2)+"px");
        }
        editContent.setHeight(getOffsetHeight()+"px");
        
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
    		
	        content = new HTML(node.getContent().toString());
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
	        
	        content.addDragStartHandler(new DragStartHandler() {
				@Override
				public void onDragStart(DragStartEvent event) {
					dragData(event);
				}
			});
	        
	        content.addDragOverHandler(new DragOverHandler() {
				@Override
				public void onDragOver(DragOverEvent event) {
					
				}
			});
	        
	        content.addDragEnterHandler(new DragEnterHandler() {
				@Override
				public void onDragEnter(DragEnterEvent event) {
					content.getElement().getStyle().setBackgroundColor("#ffa");
				}
			});
	        
	        content.addDragLeaveHandler(new DragLeaveHandler() {
				@Override
				public void onDragLeave(DragLeaveEvent event) {
					content.getElement().getStyle().setBackgroundColor("white");
				}
			});
	        
	        content.addDropHandler(new DropHandler() {
				@Override
				public void onDrop(DropEvent event) {
			        content.getElement().getStyle().setBackgroundColor("white");
	                dropData(event);
				}
			});
	        
	        content.setStylePrimaryName(resources.css().node());
	        content.addStyleName(resources.css().nodeView());
        
    	}

    	content.setText(node.getContent().toString());
    	
    	if(getOffsetWidth() >= Settings.MAX_WIDTH){
            content.setWidth(Settings.MAX_WIDTH+"px");
        }
        
        if (editContent != null) {
            container.remove(editContent);
        }
        
        container.add(content);
        
    }
    
    private void dragData(DragStartEvent event) {
    	if (node.getParent() == null) { // cannot drag root
    		event.stopPropagation();
    		return;
    	}
    	event.setData("id", String.valueOf(((AbsolutePanel)getParent()).getWidgetIndex(this)));
        
        event.getDataTransfer().setDragImage(content.getElement(), 10, 10);
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
        if(newtext.compareTo(node.getContent().toString()) != 0){
            node.setContent(newtext);
            getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
        }
        view();
        fireSelect();
    }
    
    public void update() {
    	content.setText(node.getContent().toString());
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
