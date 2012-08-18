package org.teree.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class MapType {

    public void generateEditor(AbsolutePanel panel, Node root) {
        generate(panel, root, true);
    }

    public void generateViewer(AbsolutePanel panel, Node root) {
        generate(panel, root, false);
    }
    
    /**
     * Generate map into panel.
     * 
     * @param panel
     * @param root
     */
    protected abstract void generate(AbsolutePanel panel, Node root, boolean editable);

    /**
     * Get location for root child node.
     * 
     * @param root
     * @return
     */
    public abstract NodeLocation getRootChildNodeLocation(Node root);

    /**
     * Add nodes into panel to get size of labels. (workaround)
     * 
     * @param panel
     * @param root
     */
    public int prepare(AbsolutePanel panel, Node root, Node changed, boolean resizeReq,
            boolean editable, int id) {
        NodeContent nc = root.getContent();

        // fix for resize minimal node
        if (nc.getWidth() == Settings.MIN_WIDTH) {
            nc.setWidth(0);
        }
        if (nc.getHeight() == Settings.MIN_HEIGHT) {
            nc.setHeight(0);
        }

        // fix change from max size to smaller
        if (nc.getWidth() == Settings.MAX_WIDTH && !resizeReq) {
            nc.setWidth(0);
        }

        if (changed == null && id < 0) { // prepare first render
            NodeWidget nw = new NodeWidget(root, change, editable);
            DOM.setStyleAttribute(nw.getElement(), "visibility", "hidden");
            panel.add(nw, 0, 0);
            nw.init();
        } else if (root == changed) { // prepare regeneration of map
            if (id >= panel.getWidgetCount()
                    || ((NodeWidget) panel.getWidget(id)).getNode() != changed) { // changed
                                                                                  // node
                                                                                  // has
                                                                                  // been
                                                                                  // inserted
                id = insertNode(panel, changed, change, editable, id);
            } else {
                id++;
            }
        } else {
            id++;
        }

        List<Node> cn = root.getChildNodes();
        List<Node> right = new ArrayList<Node>();
        for (int i = 0; cn != null && i < cn.size(); ++i) {
            Node n = cn.get(i);
            if (n.getLocation() == NodeLocation.LEFT) {
                id = prepare(panel, n, changed, resizeReq, change, editable, id);
            } else {
                right.add(n);
            }
        }
        for (int i = 0; i < right.size(); ++i) {
            id = prepare(panel, right.get(i), changed, resizeReq, change, editable, id);
        }
        return id;
    }

    private int insertNode(AbsolutePanel panel, Node node, NodeChangeRequest change,
            boolean editable, int id) {
        NodeWidget nw = new NodeWidget(node, change, editable);
        // hide the widgets, we use them only to get right sizes
        DOM.setStyleAttribute(nw.getElement(), "visibility", "hidden");
        if (id < panel.getWidgetCount()) {
            panel.insert(nw, 0, 0, id);
        } else {
            panel.add(nw, 0, 0);
        }
        nw.init();
        id++;
        List<Node> cn = node.getChildNodes();
        for (int i = 0; cn != null && i < cn.size(); ++i) {
            id = insertNode(panel, cn.get(i), change, editable, id);
        }
        return id;
    }

    /**
     * Set size of labels in node contents.
     * 
     * @param panel
     */
    public boolean resize(AbsolutePanel panel) {
        Iterator<Widget> it = panel.iterator();
        boolean succ = true;
        while (it.hasNext()) {
            Widget w = it.next();
            if (w instanceof NodeWidget) {
                NodeWidget nw = (NodeWidget) w;
                NodeContent nc = nw.getNode().getContent();

                // fix for resize minimal node
                if (nw.getContent().getWidgetWidth() < ContentWidget.MIN_WIDTH) {
                    nc.setWidth(ContentWidget.MIN_WIDTH);
                }
                // fix for resize maximal node
                else if (nw.getContent().getWidgetWidth() > ContentWidget.MAX_WIDTH) {
                    nc.setWidth(ContentWidget.MAX_WIDTH);
                    succ = false; // to set correct height in next cycle
                } else {
                    nc.setWidth(nw.getContent().getWidgetWidth());
                }

                if (nw.getContent().getWidgetHeight() < ContentWidget.MIN_HEIGHT) {
                    nc.setHeight(ContentWidget.MIN_HEIGHT);
                } else {
                    nc.setHeight(nw.getContent().getWidgetHeight());
                }

            }
        }
        return succ;
    }

}
