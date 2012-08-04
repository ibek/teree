package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class MapChange {

    private String oid;
    private Node changedNode;
    private Type type;
    
    public MapChange() {
        
    }
    
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Node getChangedNode() {
        return changedNode;
    }

    public void setChangedNode(Node changedNode) {
        this.changedNode = changedNode;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Portable
    public enum Type {
        CREATED,
        REMOVED
    }
    
}
