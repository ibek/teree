package org.teree.shared.data;

import java.util.Iterator;
import java.util.List;

public class Node {

    private NodeContent content;
    private Node parent;
    private List<Node> childNodes;
    
    public void addChild(Node child) {
        childNodes.add(child);
    }
    
    public void insertBefore(Node node) {
        int index = parent.childNodes.indexOf(this);
        parent.childNodes.add(index, node);
        node.setParent(this);
    }
    
    public void insertAfter(Node node) {
        int index = parent.childNodes.indexOf(this);
        parent.childNodes.add(index+1, node);
        node.setParent(this);
    }
    
    public void remove() {
        Iterator<Node> it = childNodes.iterator();
        while(it.hasNext()){
            Node child = it.next();
            child.remove();
        }
        parent.remove(this);
    }
    
    public void remove(Node child) {
        childNodes.remove(child);
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
        this.childNodes = childNodes;
    }
    
}
