package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

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