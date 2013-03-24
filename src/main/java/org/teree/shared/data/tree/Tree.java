package org.teree.shared.data.tree;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.StructureType;

/**
 * 
 * TODO: add details for scheme - created, lastEdit 
 *
 */
@Portable
public class Tree extends Scheme {

	private Node root;
	private TreeType visualization;
	
	public Tree() {
		setStructure(StructureType.Tree);
	}

    public Node getRoot() {
        return root;
    }

	@Override
	public Node getFirst() {
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
