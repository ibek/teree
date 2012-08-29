package org.teree.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.teree.shared.data.IconString;
import org.teree.shared.data.ImageLink;
import org.teree.shared.data.Link;
import org.teree.shared.data.Scheme;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;
import org.teree.shared.data.Node.NodeType;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Stateless
public class SchemeManager {

    @Inject
    DB db;
    
    private DBCollection getCollection() {
        DBCollection coll = db.getCollection("scheme");
        if(coll == null){
            coll = db.createCollection("scheme", null);
        }
        return coll;
    }
    
    /**
     * Insert new scheme.
     * @param s
     * @return oid
     */
    public String insert(Scheme s) {
        DBObject dbo = toSchemeDBObject(s);
        DBCollection coll = getCollection();
        coll.insert(dbo);
        return ((ObjectId)dbo.get("_id")).toStringMongod();
    }
    
    /**
     * Select specific node identified by oid.
     * @param oid
     * @return node
     */
    public Scheme select(String oid) {
    	if (oid == null) {
    		return null;
    	}
        DBCollection coll = getCollection();
        DBObject searchById = new BasicDBObject("_id", new ObjectId(oid));
        DBObject found = coll.findOne(searchById);
        return fromSchemeDBObject(found);
    }
    
    public List<Scheme> all() {
    	List<Scheme> all = new ArrayList<Scheme>();
    	DBCollection coll = getCollection();
    	DBObject keys = new BasicDBObject();
    	keys.put("screen", 1);
        DBCursor found = coll.find(new BasicDBObject(), keys);
        while(found.hasNext()) {
        	DBObject dbo = found.next();
        	Scheme s = new Scheme();
        	s.setOid(((ObjectId)dbo.get("_id")).toStringMongod());
        	s.setSchemePicture((String)dbo.get("screen"));
        	all.add(s);
        }
        return all;
    }
    
    /**
     * Update scheme identified by oid.
     * @param s
     */
    public void update(Scheme s) {
    	DBCollection coll = getCollection();
        DBObject updateById = new BasicDBObject("_id", new ObjectId(s.getOid()));
        coll.update(updateById, toSchemeDBObject(s));
    }
    
    private BasicDBObject toSchemeDBObject(Scheme s) {
        BasicDBObject doc = new BasicDBObject();
        
        doc.put("root", toNodeDBObject(s.getRoot()));
        
        if (s.getSchemePicture() != null) {
            doc.put("screen", s.getSchemePicture());
        }
        
        return doc;
    }
    
    private BasicDBObject toNodeDBObject(Node root) {
        BasicDBObject doc = new BasicDBObject();
        
        Object value = root.getContent();
        NodeType type = root.getType();
        doc.put("type", type.name());
        
        switch(type){
            case String: {
                doc.put("text", value);
                break;
            }
            case IconString: {
                IconString is = (IconString)value;
                doc.put("text", is.getText());
                doc.put("icon", is.getIconid());
                break;
            }
            case Link: {
            	Link link = (Link)value;
                doc.put("url", link.getUrl());
                break;
            }
            case ImageLink: {
            	ImageLink link = (ImageLink)value;
                doc.put("url", link.getUrl());
                break;
            }
        }
        
        if (root.getLocation() != null) {
            doc.put("location", root.getLocation().name());
        }
        
        doc.put("childNodes", toDBList(root.getChildNodes()));
        
        return doc;
    }
    
    private Scheme fromSchemeDBObject(DBObject scheme) {
        Scheme s = new Scheme();
        
        s.setRoot(fromNodeDBObject((BasicDBObject)scheme.get("root")));
        s.setSchemePicture((String)scheme.get("screen"));
        s.setOid(((ObjectId)scheme.get("_id")).toStringMongod());
        
        return s;
    }
    
    private Node fromNodeDBObject(DBObject root) {
        Node node = new Node();
        NodeType type = NodeType.valueOf((String)root.get("type"));
        
        switch(type){
            case String: {
                node.setContent(root.get("text"));
                break;
            }
            case IconString: {
                IconString is = new IconString();
                is.setText((String)root.get("text"));
                is.setIconid((Integer)root.get("icon"));
                node.setContent(is);
                break;
            }
            case Link: {
            	Link link = new Link();
            	link.setUrl((String)root.get("url"));
                node.setContent(link);
                break;
            }
            case ImageLink: {
            	ImageLink link = new ImageLink();
            	link.setUrl((String)root.get("url"));
                node.setContent(link);
                break;
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
                doc.add(toNodeDBObject(childNodes.get(i)));
            }
        }
        return doc;
    }
    
    private List<Node> fromDBList(BasicDBList childNodes) {
        List<Node> cn = new ArrayList<Node>();
        Iterator<Object> it = childNodes.iterator();
        while(it.hasNext()){
            cn.add(fromNodeDBObject((DBObject)it.next()));
        }
        return cn;
    }
    
}
