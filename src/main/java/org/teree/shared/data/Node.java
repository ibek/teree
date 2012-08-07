package org.teree.shared.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Node implements Cloneable {

    private Node parent;
    private List<Node> childNodes;
    private Object content;
    private NodeType type;
    private NodeLocation location;
    
    public Node clone() {
        Node root = new Node();
        root.setContent(content);
        root.setLocation(location);
        for(int i=0; childNodes != null && i<childNodes.size(); ++i){
            root.addChild(childNodes.get(i).clone());
        }
        return root;
    }
    
    public void addChild(Node child) {
        if (child == null) {
            return;
        }
        if (childNodes == null) {
            childNodes = new ArrayList<Node>();
        }
        child.setParent(this);
        childNodes.add(child);
    }
    
    public void insertBefore(Node node) {
        int index = parent.childNodes.indexOf(this);
        parent.childNodes.add(index, node);
        node.setParent(parent);
    }
    
    public void insertAfter(Node node) {
        if(parent == null || parent.childNodes == null)
            return;
        int index = parent.childNodes.indexOf(this);
        parent.childNodes.add(index+1, node);
        node.setParent(parent);
    }
    
    public void remove() {
        if(childNodes != null){
            for(int i=childNodes.size()-1; i>=0; --i){
                childNodes.get(i).remove(); // recursively remove
            }
        }
        if(parent != null){ // cannot remove root
            parent.remove(this);
        }
    }
    
    public void remove(Node child) {
        if (childNodes != null) {
            childNodes.remove(child);
        }
    }
    
    public Object getContent() {
        return content;
    }
    
    public void setContent(Object content) {
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
    
    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Node under(){
        List<Node> cn = parent.getChildNodes();
        boolean next = false;
        for(int i=0; cn != null && i<cn.size(); ++i){
            Node child = cn.get(i);
            if(next){
                return child;
            }
            if(child == this){
                next = true;
            }
        }
        return null;
    }
    
    public Node upper(){
        List<Node> cn = parent.getChildNodes();
        Node n = null, child = null;
        for(int i=0; cn != null && i<cn.size(); ++i){
            n = child;
            child = cn.get(i);
            if(child == this){
                break;
            }
            
        }
        return n;
    }
    
    public NodeLocation getLocation() {
        return location;
    }

    public void setLocation(NodeLocation location) {
        this.location = location;
    }

    public enum NodeType {
        None,
        String,
        IconString,
        URL;
    }

    public enum NodeLocation implements Serializable {
        LEFT,
        RIGHT;
    }
    
}
