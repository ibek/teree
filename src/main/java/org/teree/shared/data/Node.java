package org.teree.shared.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Node {

    private NodeContent content;
    private Node parent;
    private List<Node> childNodes;
    private NodeLocation location;
    
    public void addChild(Node child) {
        if (childNodes == null) {
            childNodes = new ArrayList<Node>();
        }
        child.setParent(this);
        childNodes.add(child);
    }
    
    public void insertBefore(Node node) {
        if(parent == null || parent.childNodes == null)
            return;
        int index = parent.childNodes.indexOf(this);
        parent.childNodes.add(index, node);
        node.setParent(this);
    }
    
    public void insertAfter(Node node) {
        if(parent == null || parent.childNodes == null)
            return;
        int index = parent.childNodes.indexOf(this);
        parent.childNodes.add(index+1, node);
        node.setParent(this);
    }
    
    public void remove() {
        if (childNodes == null) {
            return;
        }
        Iterator<Node> it = childNodes.iterator();
        while(it.hasNext()){
            Node child = it.next();
            child.remove();
        }
        parent.remove(this);
    }
    
    public void remove(Node child) {
        if (childNodes != null) {
            childNodes.remove(child);
        }
    }
    
    public NodeContent getContent() {
        return content;
    }
    
    public void setContent(NodeContent content) {
        this.content = content;
    }
    
    public Node getParent() {
        return parent;
    }
    
    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    public List<Node> getChildNodes() {
        return childNodes;
    }
    
    public void setChildNodes(List<Node> childNodes) {
        for(int i=0; i<childNodes.size(); ++i){
            addChild(childNodes.get(i));
        }
    }
    
    public NodeLocation getLocation() {
        return location;
    }

    public void setLocation(NodeLocation location) {
        this.location = location;
    }

    @Portable
    public enum NodeLocation implements Serializable {
        LEFT,
        RIGHT;
    }
    
}
