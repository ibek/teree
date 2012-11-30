package org.teree.client.view.editor;

import org.teree.client.Settings;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.client.view.resource.MathExpressionTools;
import org.teree.shared.data.scheme.IconText;
import org.teree.shared.data.scheme.MathExpression;
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
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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
        if (getOffsetWidth() <= Settings.NODE_MIN_WIDTH) {
        	editContent.setWidth((Settings.NODE_MIN_WIDTH+2)+"px");
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
        String newexpr = editContent.getText();
        
        boolean needUpdate = false;
        if (newexpr.compareTo(nodeContent.getExpression()) != 0) {
        	nodeContent.setExpression(newexpr);
        	needUpdate = true;
        }
        
        view();
        if (needUpdate) {
        	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
        			getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
                }
            });
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
    	context.save();
    	context.setFont("14px monospace");
        context.setFillStyle("#000000");
    	context.fillText(content.getText(), x, y); // TODO: math expression draw doesn't work well
        context.restore();
    }

	@Override
	public void changeStyle(NodeStyle style) {
		// nothing to be set
	}

}
