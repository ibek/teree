package org.teree.client.view.editor;

import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.shared.data.Node;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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

public class TextNodeWidget extends NodeWidget {

    private HTML content;
    
    private TextArea editContent;
    
    public TextNodeWidget() {
    	
    }
    
    protected TextNodeWidget(Node node) {
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
            
            editContent.setStylePrimaryName(resources.nodeStyle().edit());
            
        }
        
        editContent.setText(node.getContent().toString());
        
        container.remove(content);
        container.add(editContent);
        
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
	        
	        content.setStylePrimaryName(resources.basicNodeStyle().view());
        
    	}
    	
    	content.setText(node.getContent().toString());
        
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
        if(newtext.compareTo(node.getContent().toString()) != 0){
            node.setContent(newtext);
            getParent().fireEvent(new NodeChanged(this));
        }
        view();
    }
    
    @Override
    public void update() {
    	content.setText(node.getContent().toString());
    }

}
