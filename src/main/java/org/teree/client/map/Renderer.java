package org.teree.client.map;

import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.NodeInterface;
import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.TextNodeWidget;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * The new nodes won't be here inserted ... it has to be done in Scene 
 * 
 * @author ibek
 *
 */
public abstract class Renderer<T extends Widget & NodeInterface> {

    public void renderEditor(final Canvas canvas, final List<T> nodes, final Node root) {
    	prepare(nodes);
    	Scheduler.get().scheduleDeferred(new ScheduledCommand() { // to ensure that widget automatically resized size is already set
            @Override
            public void execute() {
                boolean succ = resize(nodes);
                if(!succ){ // some node is too wide
                    resize(nodes);
                }
            	render(canvas, nodes, root, true);
            }
        });
    }

    public void renderViewer(final Canvas canvas, final List<T> nodes, final Node root) {
    	prepare(nodes);
    	Scheduler.get().scheduleDeferred(new ScheduledCommand() { // to ensure that widget automatically resized size is already set
            @Override
            public void execute() {
                boolean succ = resize(nodes);
                if(!succ){ // some node is too wide
                    resize(nodes);
                }
            	render(canvas, nodes, root, false);
            }
        });
    }
    
    /**
     * Generate map.
     * 
     * @param panel
     * @param root
     */
    protected abstract void render(Canvas canvas, List<T> nodes, Node root, boolean editable);

    /**
     * Get location for root child node.
     * Default location is RIGHT.
     * 
     * @param root
     * @return
     */
    public NodeLocation getRootChildNodeLocation(Node root) {
    	return NodeLocation.RIGHT;
    }

    /**
     * Add nodes into panel to get size of labels. (workaround)
     * 
     * @param panel
     * @param root
     */
    protected void prepare(List<T> nodes) {

    	for (int i=0; i<nodes.size(); ++i) {
    		T node = nodes.get(i);
        	
            // fix for resize minimal and maximal nodes
            if (node.getOffsetWidth() == Settings.MIN_WIDTH || 
            	node.getOffsetWidth() == Settings.MAX_WIDTH) {
                node.setWidth("auto");
            }
            if (node.getOffsetHeight() == Settings.MIN_HEIGHT) {
                node.setHeight("auto");
            }
    	}
   
    }
    
    protected boolean resize(List<T> nodes) {
    	boolean succ = true;
    	for(int i=0; i<nodes.size(); ++i){
    		T node = nodes.get(i);
    		
	        if (node.getWidgetWidth() < Settings.MIN_WIDTH) {
	            node.setWidth(Settings.MIN_WIDTH+"px");
	        } else if (node.getWidgetWidth() > Settings.MAX_WIDTH) {
	            succ = false; // to set correct width in next cycle
	            node.setWidth(Settings.MAX_WIDTH+"px");// fix change from max size to smaller
	        }
	
	        if (node.getWidgetHeight() < Settings.MIN_HEIGHT) {
	        	node.setHeight(Settings.MIN_HEIGHT+"px");
	        }
    	}
    	return succ;
    }

}
