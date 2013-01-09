package org.teree.client.scheme;

import java.util.List;

import org.teree.shared.data.scheme.Node;

public class BoundsData<T> {
	
	private List<T> nodes;
	private List<List<Integer>> bounds;
	private int maxWidth = 0
	/**
	 * level of maxWidth for the case: parent1|child2 |child3 the longest
	 * parent 2
	 */;
	private int maxWidthLevel = 1;
	
	public BoundsData() {
		
	}
	
	public BoundsData(List<T> nodes, List<List<Integer>> levelBounds, int maxWidth) {
		setNodes(nodes);
		setBounds(levelBounds);
		setMaxWidth(maxWidth);
	}
	
	public List<T> getNodes() {
		return nodes;
	}
	
	public void setNodes(List<T> nodes) {
		this.nodes = nodes;
	}
	
	public List<List<Integer>> getBounds() {
		return bounds;
	}
	
	public void setBounds(List<List<Integer>> bounds) {
		this.bounds = bounds;
	}
	
	public int getMaxWidth() {
		return maxWidth;
	}
	
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	public int getMaxWidthLevel() {
		return maxWidthLevel;
	}
	
	public void setMaxWidthLevel(int maxWidthLevel) {
		this.maxWidthLevel = maxWidthLevel;
	}

}
