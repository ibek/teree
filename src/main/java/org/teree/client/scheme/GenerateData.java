package org.teree.client.scheme;

import java.util.List;

import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.Node.NodeLocation;

import com.google.gwt.canvas.dom.client.Context2d;

public class GenerateData<T> {
	
	private List<T> nodes;
	private List<List<Integer>> levelBounds;
	private List<Integer> columns; // columns[row] = number of columns in the specific row (level)
	private NodeLocation location;
	private Context2d context;
	private boolean makePicture;
	
	public GenerateData() {
		
	}
	
	public GenerateData(List<T> nodes, List<List<Integer>> levelBounds, 
			List<Integer> columns, NodeLocation location, Context2d context, boolean makePicture) {
		setNodes(nodes);
		setLevelBounds(levelBounds);
		setColumns(columns);
		setLocation(location);
		setContext(context);
		setMakePicture(makePicture);
	}

	public List<T> getNodes() {
		return nodes;
	}

	public void setNodes(List<T> nodes) {
		this.nodes = nodes;
	}

	public List<List<Integer>> getLevelBounds() {
		return levelBounds;
	}

	public void setLevelBounds(List<List<Integer>> levelBounds) {
		this.levelBounds = levelBounds;
	}

	public List<Integer> getColumns() {
		return columns;
	}

	public void setColumns(List<Integer> columns) {
		this.columns = columns;
	}

	public NodeLocation getLocation() {
		return location;
	}

	public void setLocation(NodeLocation location) {
		this.location = location;
	}

	public Context2d getContext() {
		return context;
	}

	public void setContext(Context2d context) {
		this.context = context;
	}

	public boolean isMakePicture() {
		return makePicture;
	}

	public void setMakePicture(boolean makePicture) {
		this.makePicture = makePicture;
	}
	
}
