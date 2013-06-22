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
package org.teree.shared.data.tree;

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Node.NodeLocation;

@Portable
public enum TreeType {

	MindMap,
	HorizontalHierarchy(NodeLocation.RIGHT),
	VerticalHierarchy(NodeLocation.NONE);
	
	private NodeLocation location;
	
	private TreeType() {
		
	}
	
	private TreeType(NodeLocation location) {
		this.location = location;
	}

    /**
     * Get location for root child node.
     * 
     * @param root
     * @return
     */
	public NodeLocation getRootChildNodeLocation(Node root) {
		if (location != null) {
			return location;
		}
		int left = 0, right = 0;
		List<Node> cn = root.getChildNodes();
		if (cn == null || cn.isEmpty()) {
			return NodeLocation.RIGHT;
		}
		for (int i = 0; i < cn.size(); ++i) {
			Node n = cn.get(i);
			if (n.getLocation() == NodeLocation.LEFT) {
				left++;
			} else if (n.getLocation() == NodeLocation.RIGHT) {
				right++;
			}
		}
		return (left < right) ? NodeLocation.LEFT : NodeLocation.RIGHT;
	}
	
}
