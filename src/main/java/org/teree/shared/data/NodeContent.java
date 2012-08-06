package org.teree.shared.data;

import java.net.URL;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class NodeContent implements Cloneable {
    
    private Object value;
    
    private Type type;
    
    public NodeContent clone() {
        NodeContent nc = new NodeContent();
        nc.setValue(getValue());
        return nc;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        None,
        String,
        IconString,
        URL;
    }
    
}
