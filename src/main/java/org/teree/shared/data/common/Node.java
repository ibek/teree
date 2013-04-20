package org.teree.shared.data.common;

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
    private NodeCategory category = new NodeCategory();
    
    public Node() {
    	
    }
    
    public Node(Object content) {
    	setContent(content);
    }
    
	public Node clone() {
        Node root = new Node();
        root.setContent(content);
        root.setLocation(location);
        root.setCategory(category);
        for(int i=0; childNodes != null && i<childNodes.size(); ++i){
            root.addChild(childNodes.get(i).clone());
        }
        return root;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Node)) {
			return false;
		}
		Node other = (Node) obj;
		if (category == null) {
			if (other.category != null) {
				return false;
			}
		} else if (!category.equals(other.category)) {
			return false;
		}
		if (content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!content.equals(other.content)) {
			return false;
		}
		if (location != other.location) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
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
	
	public void addChildNodes(List<Node> childNodes) {
        for(int i=0; childNodes != null && i<childNodes.size(); ++i){
            addChild(childNodes.get(i));
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
        if (content instanceof Text) {
        	type = NodeType.IconText;
        }
        else if (content instanceof Link) {
        	type = NodeType.Link;
        }
        else if (content instanceof ImageLink) {
        	type = NodeType.ImageLink;
        }
        else if (content instanceof MathExpression) {
        	type = NodeType.MathExpression;
        }
        else if (content instanceof Connector) {
        	type = NodeType.Connector;
        }
        else if (content instanceof PercentText) {
        	type = NodeType.Percent;
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
    	if (this.childNodes != null) {
    		this.childNodes.clear();
    	}
        addChildNodes(childNodes);
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
    
    public boolean isChildNode(Node node) {
    	for(int i=0; childNodes != null && i < childNodes.size(); ++i){
    		Node n = childNodes.get(i);
    		if (n == node || n.isChildNode(node)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public NodeLocation getLocation() {
        return location;
    }

    public void setLocation(NodeLocation location) {
        this.location = location;
    }

	public NodeCategory getCategory() {
		return category;
	}

	public void setCategory(NodeCategory category) {
		this.category = category;
	}
	
	public void putAllRight() {
		for (int i=0; childNodes != null && i<childNodes.size(); ++i) {
			childNodes.get(i).setLocation(NodeLocation.RIGHT);
		}
	}

	public enum NodeType {
        None,
        IconText,
        Link,
        ImageLink,
        MathExpression,
        Connector,
        Percent;
    }

    public enum NodeLocation implements Serializable {
        LEFT,
        RIGHT,
        NONE;
    }
    
}
