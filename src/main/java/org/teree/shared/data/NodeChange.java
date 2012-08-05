package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * Information about change of node for cooperation.
 *
 * @author Ivo Bek
 *
 */
@Portable
public class NodeChange {

    /**
     * Map object identifier.
     */
    private String oid;
    
    /**
     * Identifier of the node where was the change.
     */
    private int nodeId;
    
    /**
     * New node which should replace the old one.
     */
    private Node newNode;
    
    /**
     * Type of change.
     */
    private Type type;
    
    public NodeChange() {
        
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public Node getNewNode() {
        return newNode;
    }

    public void setNewNode(Node newNode) {
        this.newNode = newNode;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
