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
package org.teree.client.visualization.tree;

import java.util.List;

import org.teree.shared.data.common.Node.NodeLocation;

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
