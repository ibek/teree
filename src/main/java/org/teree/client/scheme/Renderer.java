package org.teree.client.scheme;

import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.NodeInterface;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.Node.NodeLocation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
            	render(canvas, nodes, root, false, true);
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
                render(canvas, nodes, root, false, false);
            }
        });
    }

    public void renderPicture(Canvas canvas, final List<T> nodes, final Node root) {
        prepare(nodes);
        boolean succ = resize(nodes);
        if(!succ){ // some node is too wide
            resize(nodes);
        }
        render(canvas, nodes, root, true, false);
    }
    
    /**
     * Generate scheme.
     * 
     * @param canvas
     * @param nodes
     * @param root
     * @param makePicture generate the map into canvas to be transformed into picture
     * @param editable
     */
    protected abstract void render(Canvas canvas, List<T> nodes, Node root, boolean makePicture, boolean editable);

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

    protected void prepare(List<T> nodes) {

    	for (int i=0; i<nodes.size(); ++i) {
    		T node = nodes.get(i);
        	
            // fix for resize minimal and maximal nodes
            if (node.getOffsetWidth() == Settings.NODE_MIN_WIDTH || 
            	node.getOffsetWidth() == Settings.NODE_MAX_WIDTH) {
                node.setWidth("auto");
            }
            if (node.getOffsetHeight() == Settings.NODE_MIN_HEIGHT) {
                node.setHeight("auto");
            }
    	}
   
    }
    
    protected boolean resize(List<T> nodes) {
    	boolean succ = true;
    	for(int i=0; i<nodes.size(); ++i){
    		T node = nodes.get(i);
    		
	        if (node.getWidgetWidth() < Settings.NODE_MIN_WIDTH) {
	            node.setWidth(Settings.NODE_MIN_WIDTH+"px");
	        } else if (node.getWidgetWidth() > Settings.NODE_MAX_WIDTH) {
	            succ = false; // to set correct width in next cycle
	            node.setWidth(Settings.NODE_MAX_WIDTH+"px");// fix change from max size to smaller
	        }
	
	        if (node.getWidgetHeight() < Settings.NODE_MIN_HEIGHT) {
	        	node.setHeight(Settings.NODE_MIN_HEIGHT+"px");
	        }
    	}
    	return succ;
    }

}
