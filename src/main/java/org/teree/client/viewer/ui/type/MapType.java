package org.teree.client.viewer.ui.type;

import java.util.Iterator;
import java.util.List;

import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.shared.data.Node;
import org.teree.shared.data.NodeContent;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class MapType {

    /**
     * Generate map into panel.
     * @param panel
     * @param root
     */
    public abstract void generate(AbsolutePanel panel, Node root);

    /**
     * Add nodes into panel to get size of labels. (workaround)
     * @param panel
     * @param root
     */
    public void prepare(AbsolutePanel panel, Node root){
        panel.add(new NodeWidget(root), 0, 0);
        List<Node> cn = root.getChildNodes();
        for(int i=0; cn != null && i<cn.size(); ++i){
            prepare(panel, cn.get(i));
        }
    }
    
    /**
     * Set size of labels in node contents.
     * @param panel
     */
    public void resize(AbsolutePanel panel){
        Iterator<Widget> it = panel.iterator();
        while(it.hasNext()){
            Widget w = it.next();
            if(w instanceof NodeWidget){
                NodeWidget nw = (NodeWidget) w;
                NodeContent nc = nw.getNode().getContent();
                nc.setWidth(nw.getContent().getWidgetWidth());
                nc.setHeight(nw.getContent().getWidgetHeight());
            }
        }
    }
    
}
