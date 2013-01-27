package org.teree.shared.data.tree;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Permissions;
import org.teree.shared.data.common.Scheme;

/**
 * 
 * TODO: add details for scheme - created, lastEdit 
 *
 */
@Portable
public class Tree extends Scheme {

	private Node root;
	private TreeType visualization;

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
	
	public TreeType getVisualization() {
		return visualization;
	}

	public void setVisualization(TreeType visualization) {
		this.visualization = visualization;
	}
	
	@Override
	public String toString() {
		return root.getContent().toString();
	}
	
}
