package org.teree.client.viewer.ui.type;

import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;

import com.google.gwt.user.client.ui.AbsolutePanel;

public class DirectBrowse extends MapType {

    @Override
    public NodeLocation getRootChildNodeLocation(Node root) {
        return NodeLocation.RIGHT; // always right
    }

    @Override
    public void generate(AbsolutePanel panel, Node root, boolean editable) {
        // TODO Auto-generated method stub
        
    }

}
