package org.teree.client.viewer.ui.type;

import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;

import com.google.gwt.user.client.ui.AbsolutePanel;

public class DirectBrowse extends MapType {

    @Override
    public NodeWidget generate(AbsolutePanel panel, Node root, Regenerate reg) {
        int max_x = 0, max_y = 0;
        /**while(it.hasNext()) {
            NodeWidget nw = it.next();
            Box bounds = nw.getBounds();
            panel.add(nw, bounds.x, bounds.y);
            if (max_x < bounds.x + bounds.w) {
                max_x = bounds.x + bounds.w;
            }
            if (max_y < bounds.y + bounds.h) {
                max_y = bounds.y + bounds.h;
            }
        }*/
        panel.setWidth(max_x + "px");
        panel.setHeight(max_y + "px");
        return null;
    }

    @Override
    public NodeLocation getRootChildNodeLocation(Node root) {
        return NodeLocation.RIGHT; // always right
    }

}
