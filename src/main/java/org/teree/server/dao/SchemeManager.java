package org.teree.server.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.teree.shared.data.IconString;
import org.teree.shared.data.ImageLink;
import org.teree.shared.data.Link;
import org.teree.shared.data.NodeStyle;
import org.teree.shared.data.Scheme;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;
import org.teree.shared.data.Node.NodeType;
import org.teree.shared.data.UserInfo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Stateless
public class SchemeManager {

	@Inject
    MongoDB mdb;
    
    private DBCollection getCollection() {
    	DB db = mdb.getDatabase();
        DBCollection coll = db.getCollection("scheme");
        if(coll == null){
            coll = db.createCollection("scheme", null);
        }
        return coll;
    }
    
    public String insertPrivate(Scheme s, UserInfo ui) {
    	if (ui == null) {
    		return null;
    	}
        DBObject dbo = toSchemeDBObject(s);
        dbo.put("owner", ui.getUserId());
        DBCollection coll = getCollection();
        coll.insert(dbo);
        return ((ObjectId)dbo.get("_id")).toStringMongod();
    }
    
    /**
     * Select specific scheme identified by oid.
     * @param oid
     * @return node
     */
    public Scheme select(String oid) {
    	if (oid == null) {
    		return null;
    	}
        DBObject searchById = new BasicDBObject("_id", new ObjectId(oid));
        return select(searchById);
    }
    
    public Scheme selectPrivate(String oid, UserInfo ui) {
    	if (oid == null || ui == null) {
    		return null;
    	}
        DBObject searchBy = new BasicDBObject("_id", new ObjectId(oid));
        searchBy.put("owner", ui.getUserId());
        return select(searchBy);
    }
    
    private Scheme select(DBObject searchBy) {
        DBCollection coll = getCollection();
        DBObject found = coll.findOne(searchBy);
        return fromSchemeDBObject(found);
    }
    
    /**
     * Get all public schemes.
     * @return
     */
    public List<Scheme> allPublic() {
    	DBObject keys = new BasicDBObject();
    	keys.put("screen", 1);
    	DBObject ref = new BasicDBObject("owner", new BasicDBObject("$exists", false));
    	return all(ref, keys);
    }
    
    public List<Scheme> allPrivate(UserInfo ui) {
    	DBObject keys = new BasicDBObject();
    	keys.put("screen", 1);
    	DBObject ref = new BasicDBObject("owner", ui.getUserId());
    	return all(ref, keys);
    }

    private List<Scheme> all(DBObject ref, DBObject keys) {
    	List<Scheme> all = new ArrayList<Scheme>();
    	DBCollection coll = getCollection();
        DBCursor found = coll.find(ref, keys);
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
                doc.put("text", link.getText());
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
        
        NodeStyle ns = root.getStyle();
        if (ns != null && !ns.isDefault()) {
        	BasicDBObject style = new BasicDBObject();
        	style.put("bold", ns.isBold());
        	
        	doc.put("style", style);
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
    
    private Node fromNodeDBObject(BasicDBObject root) {
        Node node = new Node();
        NodeType type = NodeType.valueOf(root.getString("type"));
        
        switch(type){
            case String: {
                node.setContent(root.get("text"));
                break;
            }
            case IconString: {
                IconString is = new IconString();
                is.setText(root.getString("text"));
                is.setIconid(root.getInt("icon"));
                node.setContent(is);
                break;
            }
            case Link: {
            	Link link = new Link();
            	link.setText(root.getString("text"));
            	link.setUrl(root.getString("url"));
                node.setContent(link);
                break;
            }
            case ImageLink: {
            	ImageLink link = new ImageLink();
            	link.setUrl(root.getString("url"));
                node.setContent(link);
                break;
            }
        }
        
        String location = root.getString("location");
        if(location != null){
            node.setLocation(NodeLocation.valueOf(location));
        }
        
        BasicDBObject style = (BasicDBObject)root.get("style");
        if(style != null){
        	NodeStyle ns = new NodeStyle();
            ns.setBold(style.getBoolean("bold"));
        	node.setStyle(ns);
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
            cn.add(fromNodeDBObject((BasicDBObject)it.next()));
        }
        return cn;
    }
    
}
