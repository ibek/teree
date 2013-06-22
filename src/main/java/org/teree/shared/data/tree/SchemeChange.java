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

import org.jboss.errai.common.client.api.annotations.Portable;
import org.teree.shared.data.common.Node;

@Portable
public class SchemeChange {

    private String oid;
    private int nodeId;
    private Node changedNode;
    private Type type;
    
    public SchemeChange() {
        
    }
    
    public String getOid() {
        return oid;
    }

    public SchemeChange setOid(String oid) {
        this.oid = oid;
        return this;
    }

    public int getNodeId() {
        return nodeId;
    }

    public SchemeChange setNodeId(int nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public Node getChangedNode() {
        return changedNode;
    }

    public SchemeChange setChangedNode(Node changedNode) {
        this.changedNode = changedNode;
        return this;
    }

    public Type getType() {
        return type;
    }

    public SchemeChange setType(Type type) {
        this.type = type;
        return this;
    }
    
    @Override
    public String toString() {
        return "oid:"+oid+" type:"+type;
    }

    @Portable
    public enum Type {
        CREATE_CHILD,
        CREATE_BEFORE,
        CREATE_AFTER,
        EDIT,
        REMOVE,
        MOVE_UP,
        MOVE_DOWN
    }
    
}
