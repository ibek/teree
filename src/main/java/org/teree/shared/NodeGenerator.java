/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.shared;

import org.teree.shared.data.common.Text;
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
    
    public static Node mindmap() {
        Node root = createTextNode("Mind Mapping", null);
        Node technical = createTextNode("Technical", NodeLocation.LEFT);
        technical.addChild(createTextNode("Report writing", NodeLocation.LEFT));
        technical.addChild(createTextNode("Presentations", NodeLocation.LEFT));
        technical.addChild(createTextNode("Problem solving", NodeLocation.LEFT));
        technical.addChild(createTextNode("Planning", NodeLocation.LEFT));
        technical.addChild(createTextNode("Presentation planning", NodeLocation.LEFT));
        technical.addChild(createTextNode("Communication", NodeLocation.LEFT));
        root.addChild(technical);
        
        Node managerial = createTextNode("Managerial", NodeLocation.LEFT);
        Node interviews = createTextNode("Interviews", NodeLocation.LEFT);
        interviews.addChild(createTextNode("Job descriptions", NodeLocation.LEFT));
        interviews.addChild(createTextNode("Questioning techniques", NodeLocation.LEFT));
        managerial.addChild(interviews);
        managerial.addChild(createTextNode("Appraisals", NodeLocation.LEFT));
        managerial.addChild(createTextNode("Meeting minutes", NodeLocation.LEFT));
        managerial.addChild(createTextNode("Planning", NodeLocation.LEFT));
        managerial.addChild(createTextNode("Time management", NodeLocation.LEFT));
        root.addChild(managerial);

        Node personal = createTextNode("Personal", NodeLocation.RIGHT);
        personal.addChild(createTextNode("Planning your time", NodeLocation.RIGHT));
        personal.addChild(createTextNode("Organising your day", NodeLocation.RIGHT));
        personal.addChild(createTextNode("Letter writing", NodeLocation.RIGHT));
        personal.addChild(createTextNode("Report writing", NodeLocation.RIGHT));
        root.addChild(personal);

        Node creative = createTextNode("Creative", NodeLocation.RIGHT);
        creative.addChild(createTextNode("Problem solving", NodeLocation.RIGHT));
        creative.addChild(createTextNode("Generating ideas", NodeLocation.RIGHT));
        creative.addChild(createTextNode("Talks", NodeLocation.RIGHT));
        creative.addChild(createTextNode("Brainstorming", NodeLocation.RIGHT));
        root.addChild(creative);

        return root;
    }
    
    public static Node horizontalHierarchy() {
        Node root = createTextNode("Horizontal Hierarchy", null);
        root.addChild(createTextNode("Decision making", NodeLocation.RIGHT));
        root.addChild(createTextNode("Problem solving", NodeLocation.RIGHT));
        root.addChild(createTextNode("Planning", NodeLocation.RIGHT));
        return root;
    }
    
    public static Node createTextNode(String text, NodeLocation loc) {
        Node n = new Node();
        Text it = new Text();
        it.setText(text);
        n.setContent(it);
        n.setLocation(loc);
        return n;
    }
    
}
