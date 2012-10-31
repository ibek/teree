package org.teree.shared.data.scheme;

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
    private NodeStyle style;
    
	public Node clone() {
        Node root = new Node();
        root.setContent(content);
        root.setLocation(location);
        if (style != null) {
        	root.setStyle(style.clone());
        }
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
        if (parent == null && child.getLocation() == NodeLocation.LEFT) { // guarantee that left root child nodes will be first
        	
        	int id = 0;
        	for(; id<childNodes.size(); ++id){ // find index of last left child
        		if (childNodes.get(id).getLocation() == NodeLocation.RIGHT) {
        			break;
        		}
        	}
        	childNodes.add(id, child);
        } else {
        	childNodes.add(child);
        }
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
        
        // set type
        if (content instanceof IconText) {
        	type = NodeType.IconText;
        }
        else if (content instanceof Link) {
        	type = NodeType.Link;
        }
        else if (content instanceof ImageLink) {
        	type = NodeType.ImageLink;
        }
        else {
        	type = NodeType.None;
        }
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
    	if (type == null) {
    		setContent(content);
    	}
        return type;
    }
    
    public int getNumberOfChildNodes() {
    	int noc = 0;
    	for(int i=0; childNodes != null && i < childNodes.size(); ++i, ++noc){
    		noc += childNodes.get(i).getNumberOfChildNodes(); // recursively get number of their child nodes
    	}
    	return noc;
    }
    
    public int getNumberOfLeftChildNodes() {
    	int noc = 0;
    	for(int i=0; childNodes != null && i < childNodes.size(); ++i){
    		Node n = childNodes.get(i);
    		if(n.getLocation() == NodeLocation.LEFT){
    			noc += n.getNumberOfLeftChildNodes(); // recursively get number of their child nodes
    			noc++;
    		}
    	}
    	return noc;
    }
    
    public NodeLocation getLocation() {
        return location;
    }

    public void setLocation(NodeLocation location) {
        this.location = location;
    }
    
    public NodeStyle getStyle() {
		return style;
	}
    
    public NodeStyle getStyleOrCreate() {
    	if (style == null) {
    		style = new NodeStyle();
    	}
		return style;
	}

	public void setStyle(NodeStyle style) {
		this.style = style;
	}

    public enum NodeType {
        None,
        IconText,
        Link,
        ImageLink;
    }

    public enum NodeLocation implements Serializable {
        LEFT,
        RIGHT;
    }
    
}
