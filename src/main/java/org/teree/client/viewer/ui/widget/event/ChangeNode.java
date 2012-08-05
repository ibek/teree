package org.teree.client.viewer.ui.widget.event;

import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.shared.data.MapChange;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;
import org.teree.shared.data.NodeContent;

import com.google.gwt.user.client.ui.AbsolutePanel;

public abstract class ChangeNode {
    
    /**
     * TODO: modify createNode to enable have a different types of node
     * @param loc
     * @return
     */
    private Node createNode(NodeLocation loc) {
        Node node = new Node();
        NodeContent nc = new NodeContent();
        nc.setText("");
        node.setContent(nc);
        node.setLocation(loc);
        return node;
    }

    private void createChild(Node parent) {
        parent.addChild(createNode(parent.getLocation()));
    }

    private void createBefore(Node child, Node under) {
        under.insertBefore(child);
    }

    private void createAfter(Node child, Node upper) {
        upper.insertAfter(child);
    }
    
    protected abstract void regenerate(Node changed, boolean edit);

    public void change(AbsolutePanel panel, MapChange change) {
        NodeWidget nw = (NodeWidget)panel.getWidget(change.getNodeId());
        switch(change.getType()){
            case CREATE_CHILD: {
                createChild(nw.getNode());
            }
            case CREATE_BEFORE: {
                
            }
            case CREATE_AFTER: {
                
            }
            case EDIT: {
                
            }
            case REMOVE: {
                
            }
            case MOVE_UP: {
                
            }
            case MOVE_DOWN: {
                
            }
        }
        regenerate(change.getChangedNode(), false); // TODO: fix for remote and local createChild to enable edit content after create
    }
    
}
