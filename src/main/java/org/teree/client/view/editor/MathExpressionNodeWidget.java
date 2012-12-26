package org.teree.client.view.editor;

import org.teree.client.Settings;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.resource.MathExpressionTools;
import org.teree.shared.data.scheme.MathExpression;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;

public class MathExpressionNodeWidget extends NodeWidget {
	
    private HTML content;
    private TextArea editContent;
    
    private MathExpression nodeContent;
    
    public MathExpressionNodeWidget(Node node) {
        super(node);

        nodeContent = (MathExpression)node.getContent();
        
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
        
        editContent.setText(nodeContent.getExpression());
        
        if (getOffsetWidth() > Settings.NODE_DEFAULT_WIDTH) {
        	editContent.setWidth((getOffsetWidth()+4)+"px");
        } else {
        	editContent.setWidth(Settings.NODE_DEFAULT_WIDTH+"px");
        }
        if (getOffsetHeight() > Settings.NODE_DEFAULT_HEIGHT) {
            editContent.setHeight(getOffsetHeight()+"px");
        } else {
            editContent.setHeight(Settings.NODE_DEFAULT_HEIGHT+"px");
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
    		
	        content = new HTML();
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
    
    private void confirmChanges() {
        String newexpr = editContent.getText();
        
        boolean needUpdate = false;
        if (newexpr.compareTo(nodeContent.getExpression()) != 0) {
        	nodeContent.setExpression(newexpr);
        	needUpdate = true;
        }
        
        view();
        if (needUpdate) {
    		Timer t = new Timer() {
				@Override
				public void run() {
					getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
				}
			};
			t.schedule(1000);
        }
        fireSelect();
    }
    
    public void update() {
		String expr = nodeContent.getExpression();
		if (expr.isEmpty()) {
			expr = "[empty]";
		}
		content.getElement().setInnerHTML(SafeHtmlUtils.htmlEscape("\\["+expr+"\\]"));
		MathExpressionTools.renderLatexResult(content.getElement());
    }

    @Override
    public void draw(Context2d context, int x, int y) {
        NodeList<Element> nl = content.getElement().getElementsByTagName("div");
        if (nl.getLength() > 0) {
        	Element e = nl.getItem(0);
	    	context.save();
	    	context.setFont("14px monospace");
	        context.setFillStyle("#000000");
	        context.fillText(e.getInnerText(), x, y);
	        context.restore();
        }
    }

	@Override
	public void changeStyle(NodeStyle style) {
		// nothing to be set
	}

}
