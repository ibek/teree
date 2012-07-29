package org.teree.client.viewer.ui.type;

import java.util.Iterator;
import java.util.List;

import org.teree.client.viewer.ui.widget.ContentWidget;
import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.shared.data.Node;
import org.teree.shared.data.NodeContent;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class MapType {

    /**
     * Generate map into panel.
     * @param panel
     * @param root
     */
    public abstract void generate(AbsolutePanel panel, Node root, Regenerate reg);

    /**
     * Add nodes into panel to get size of labels. (workaround)
     * @param panel
     * @param root
     */
    public void prepare(AbsolutePanel panel, Node root, boolean resizeReq){
        NodeContent nc = root.getContent();
        
        // fix for resize minimal node 
        if(nc.getWidth() == ContentWidget.MIN_WIDTH){
            nc.setWidth(0);
        }
        if(nc.getHeight() == ContentWidget.MIN_HEIGHT){
            nc.setHeight(0);
        }
        
        // fix change from max size to smaller
        if(nc.getWidth() == ContentWidget.MAX_WIDTH && !resizeReq){
            nc.setWidth(0);
        }
        
        NodeWidget nw = new NodeWidget(root);
        // hide the widgets, we use them only to get right sizes
        DOM.setStyleAttribute(nw.getElement(), "visibility", "hidden");
        
        panel.add(nw, 0, 0);
        List<Node> cn = root.getChildNodes();
        for(int i=0; cn != null && i<cn.size(); ++i){
            prepare(panel, cn.get(i), resizeReq);
        }
    }
    
    /**
     * Set size of labels in node contents.
     * @param panel
     */
    public boolean resize(AbsolutePanel panel){
        Iterator<Widget> it = panel.iterator();
        boolean succ = true;
        while(it.hasNext()){
            Widget w = it.next();
            if(w instanceof NodeWidget){
                NodeWidget nw = (NodeWidget) w;
                NodeContent nc = nw.getNode().getContent();
                
                // fix for resize minimal node
                if(nw.getContent().getWidgetWidth() < ContentWidget.MIN_WIDTH){
                    nc.setWidth(ContentWidget.MIN_WIDTH);
                }
                // fix for resize maximal node
                else if (nw.getContent().getWidgetWidth() > ContentWidget.MAX_WIDTH) {
                    nc.setWidth(ContentWidget.MAX_WIDTH);
                    succ = false; // to set correct height in next cycle
                } else {
                    nc.setWidth(nw.getContent().getWidgetWidth());
                }
                
                if(nw.getContent().getWidgetHeight() < ContentWidget.MIN_HEIGHT){
                    nc.setHeight(ContentWidget.MIN_HEIGHT);
                }else{
                    nc.setHeight(nw.getContent().getWidgetHeight());
                }
                
                
                
            }
        }
        return succ;
    }
    
}
