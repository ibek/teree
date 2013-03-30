package org.teree.shared;

import org.teree.shared.data.common.IconText;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeCategory;
import org.teree.shared.data.common.Node.NodeLocation;

public class NodeGenerator {

    public static Node newNode() {
        Node root = createTextNode("root", null);
        return root;
    }

    public static Node basic() {
        Node root = createTextNode("root", null);
        root.addChild(createTextNode("left node", NodeLocation.LEFT));
        root.addChild(createTextNode("right node", NodeLocation.RIGHT));
        return root;
    }
    
    public static Node linechilds() {
        Node root = createTextNode("root", null);
        Node left = createTextNode("left node", NodeLocation.LEFT);
        left.addChild(createTextNode("left left node", NodeLocation.LEFT));
        root.addChild(left);
        Node right = createTextNode("right node", NodeLocation.RIGHT);
        right.addChild(createTextNode("right right node", NodeLocation.RIGHT));
        root.addChild(right);
        return root;
    }
    
    public static Node columnchilds() {
        Node root = createTextNode("root", null);
        root.addChild(createTextNode("left node1", NodeLocation.LEFT));
        root.addChild(createTextNode("left node2", NodeLocation.LEFT));
        root.addChild(createTextNode("left node3", NodeLocation.LEFT));
        root.addChild(createTextNode("left node4", NodeLocation.LEFT));
        root.addChild(createTextNode("left node5", NodeLocation.LEFT));
        root.addChild(createTextNode("right node1", NodeLocation.RIGHT));
        root.addChild(createTextNode("right node2", NodeLocation.RIGHT));
        root.addChild(createTextNode("right node3", NodeLocation.RIGHT));
        root.addChild(createTextNode("right node4", NodeLocation.RIGHT));
        root.addChild(createTextNode("right node5", NodeLocation.RIGHT));
        return root;
    }
    
    public static Node complex() {
        Node root = createTextNode("root", null);
        root.addChild(createTextNode("left node1", NodeLocation.LEFT));
        Node left = createTextNode("left node2", NodeLocation.LEFT);
        left.addChild(createTextNode("left left node1", NodeLocation.LEFT));
        left.addChild(createTextNode("left left node2", NodeLocation.LEFT));
        root.addChild(left);
        root.addChild(createTextNode("left node3", NodeLocation.LEFT));
        root.addChild(createTextNode("right node1", NodeLocation.RIGHT));
        Node right = createTextNode("right node2", NodeLocation.RIGHT);
        right.addChild(createTextNode("right right node1", NodeLocation.RIGHT));
        Node right2 = createTextNode("right right node2", NodeLocation.RIGHT);
        right2.addChild(createTextNode("r^3 node1", NodeLocation.RIGHT));
        right2.addChild(createTextNode("r^3 node2", NodeLocation.RIGHT));
        right.addChild(right2);
        right.addChild(createTextNode("right right node3", NodeLocation.RIGHT));
        root.addChild(right);
        root.addChild(createTextNode("right node3", NodeLocation.RIGHT));
        return root;
    }
    
    public static Node createTextNode(String text, NodeLocation loc) {
        Node n = new Node();
        IconText it = new IconText();
        it.setText(text);
        n.setContent(it);
        n.setLocation(loc);
        n.setCategory(new NodeCategory());
        return n;
    }
    
}
