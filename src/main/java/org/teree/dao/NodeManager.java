package org.teree.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.teree.shared.data.Node;
import org.teree.shared.data.NodeContent;
import org.teree.shared.data.Node.NodeLocation;

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
    
    public String insert(Node root) {
        DBObject dbo = toDBObject(root);
        DBCollection coll = getCollection();
        coll.insert(dbo);
        Object oid = dbo.get("_id");
        System.out.println("oid:"+oid+" class:" + oid.getClass().getName());
        return (oid != null)?oid.toString():null;
    }
    
    public Node select(String oid) {
        DBCollection coll = getCollection();
        DBObject searchById = new BasicDBObject("_id", new ObjectId(oid));
        DBObject found = coll.findOne(searchById);
        return fromDBObject(found);
    }
    
    public void update(String oid, Node root) {
    	DBCollection coll = getCollection();
        DBObject updateById = new BasicDBObject("_id", new ObjectId(oid));
        coll.update(updateById, toDBObject(root));
    }
    
    private BasicDBObject toDBObject(Node root) {
        BasicDBObject doc = new BasicDBObject();
        
        doc.put("text", root.getContent().getText());
        if(root.getLocation() != null){
            doc.put("location", root.getLocation().name());
        }
        doc.put("childNodes", toDBList(root.getChildNodes()));
        
        return doc;
    }
    
    private Node fromDBObject(DBObject root) {
        Node node = new Node();
        
        NodeContent nc = new NodeContent();
        nc.setText((String)root.get("text"));
        node.setContent(nc);
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
