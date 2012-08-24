package org.teree.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.teree.shared.data.IconString;
import org.teree.shared.data.Link;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;
import org.teree.shared.data.Node.NodeType;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Stateless
public class NodeManager {

    @Inject
    DB db;
    
    private DBCollection getCollection() {
        DBCollection coll = db.getCollection("node");
        if(coll == null){
            coll = db.createCollection("node", null);
        }
        return coll;
    }
    
    /**
     * Insert new node.
     * @param root of the new node
     * @return oid
     */
    public String insert(Node root) {
        DBObject dbo = toDBObject(root);
        DBCollection coll = getCollection();
        coll.insert(dbo);
        return (String)dbo.get("_id");
    }
    
    /**
     * Select specific node identified by oid.
     * @param oid
     * @return node
     */
    public Node select(String oid) {
        DBCollection coll = getCollection();
        DBObject searchById = new BasicDBObject("_id", new ObjectId(oid));
        DBObject found = coll.findOne(searchById);
        return fromDBObject(found);
    }
    
    /**
     * Update node identified by oid.
     * @param oid
     * @param root
     */
    public void update(String oid, Node root) {
    	DBCollection coll = getCollection();
        DBObject updateById = new BasicDBObject("_id", new ObjectId(oid));
        coll.update(updateById, toDBObject(root));
    }
    
    private BasicDBObject toDBObject(Node root) {
        BasicDBObject doc = new BasicDBObject();
        
        Object value = root.getContent();
        NodeType type = root.getType();
        doc.put("type", type.name());
        
        switch(type){
            case String: {
                doc.put("text", value);
            }
            case IconString: {
                IconString is = (IconString)value;
                doc.put("text", is.getText());
                doc.put("icon", is.getIconid());
            }
            case Link: {
            	Link link = (Link)value;
                doc.put("url", link.getUrl());
            }
        }
        
        if(root.getLocation() != null){
            doc.put("location", root.getLocation().name());
        }
        doc.put("childNodes", toDBList(root.getChildNodes()));
        
        return doc;
    }
    
    private Node fromDBObject(DBObject root) {
        Node node = new Node();
        NodeType type = NodeType.valueOf((String)root.get("type"));
        
        switch(type){
            case String: {
                node.setContent(root.get("text"));
            }
            case IconString: {
                IconString is = new IconString();
                is.setText((String)root.get("text"));
                is.setIconid((Integer)root.get("icon"));
                node.setContent(is);
            }
            case Link: {
            	Link link = new Link();
            	link.setUrl((String)root.get("url"));
                node.setContent(link);
            }
        }
        
        String location = (String)root.get("location");
        if(location != null){
            node.setLocation(NodeLocation.valueOf(location));
        }
        node.setChildNodes(fromDBList((BasicDBList)root.get("childNodes")));
        
        return node;
    }
    
    private BasicDBList toDBList(List<Node> childNodes) {
        BasicDBList doc = new BasicDBList();
        if(childNodes != null){
            for(int i=0; i<childNodes.size(); ++i){
                doc.add(toDBObject(childNodes.get(i)));
            }
        }
        return doc;
    }
    
    private List<Node> fromDBList(BasicDBList childNodes) {
        List<Node> cn = new ArrayList<Node>();
        Iterator<Object> it = childNodes.iterator();
        while(it.hasNext()){
            cn.add(fromDBObject((DBObject)it.next()));
        }
        return cn;
    }
    
}
