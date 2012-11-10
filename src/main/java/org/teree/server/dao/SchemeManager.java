package org.teree.server.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.teree.shared.data.scheme.IconText;
import org.teree.shared.data.scheme.ImageLink;
import org.teree.shared.data.scheme.Link;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;
import org.teree.shared.data.scheme.Scheme;
import org.teree.shared.data.scheme.Node.NodeLocation;
import org.teree.shared.data.scheme.Node.NodeType;
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
    
	@Inject
	UserInfoManager _uim;
    
    private DBCollection getCollection() {
    	DB db = mdb.getDatabase();
        DBCollection coll = null;
        if(db.getCollectionNames().contains("scheme")){
        	coll = db.getCollection("scheme");
        } else {
            coll = db.createCollection("scheme", null);
        }
        return coll;
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
    
    public String insert(Scheme s, UserInfo ui) {
    	if (ui == null) {
    		return null;
    	}
        DBObject dbo = toSchemeDBObject(s);
        dbo.put("owner", ui.getUserId());
        DBCollection coll = getCollection();
        coll.insert(dbo);
        String oid = ((ObjectId)dbo.get("_id")).toStringMongod();

        // update userinfo

    	ui.setPrivateCount(ui.getPrivateCount() + 1);
        _uim.updateCount(ui);
        
        return oid;
        
    }
    
    /**
     * Get all public schemes.
     * @return
     */
    public List<Scheme> allPublicFrom(String from_oid, int limit) {
    	DBObject keys = new BasicDBObject();
    	keys.put("root", 0);
    	DBObject ref = new BasicDBObject("owner", new BasicDBObject("$exists", false));
    	if (from_oid != null) {
    		ref.put("_id", new BasicDBObject("$lt", new ObjectId(from_oid)));
    	}
    	return allFrom(ref, keys, limit);
    }
    
    public List<Scheme> allPublicTo(String to_oid, int limit) {
    	DBObject keys = new BasicDBObject();
    	keys.put("root", 0);
    	DBObject ref = new BasicDBObject("owner", new BasicDBObject("$exists", false));
    	if (to_oid != null) {
    		ref.put("_id", new BasicDBObject("$gt", new ObjectId(to_oid)));
    	}
    	return allTo(ref, keys, limit); 
    }
    
    public List<Scheme> allPrivateFrom(UserInfo ui, String from_oid, int limit) {
    	DBObject keys = new BasicDBObject();
    	keys.put("root", 0);
    	DBObject ref = new BasicDBObject("owner", ui.getUserId());
    	if (from_oid != null) {
    		ref.put("_id", new BasicDBObject("$lt", new ObjectId(from_oid)));
    	}
    	return allFrom(ref, keys, limit);
    }
    
    public List<Scheme> allPrivateTo(UserInfo ui, String to_oid, int limit) {
    	DBObject keys = new BasicDBObject();
    	keys.put("root", 0);
    	DBObject ref = new BasicDBObject("owner", ui.getUserId());
    	if (to_oid != null) {
    		ref.put("_id", new BasicDBObject("$gt", new ObjectId(to_oid)));
    	}
    	return allTo(ref, keys, limit);
    }

    private List<Scheme> allFrom(DBObject ref, DBObject keys, int limit) {
    	List<Scheme> res = new ArrayList<Scheme>();
    	DBCollection coll = getCollection();
        DBCursor found = coll.find(ref, keys)
        		.sort(new BasicDBObject("_id", -1))
        		.limit(limit);
        while(found.hasNext()) {
        	DBObject dbo = found.next();
        	res.add(fromSchemeDBObjectInfo(dbo));
        }
        return res;
    }

    private List<Scheme> allTo(DBObject ref, DBObject keys, int limit) {
    	List<Scheme> res = new ArrayList<Scheme>();
    	DBCollection coll = getCollection();
        DBCursor found = coll.find(ref, keys)
        		.sort(new BasicDBObject("_id", 1))
        		.limit(limit);
        while(found.hasNext()) {
        	DBObject dbo = found.next();
        	res.add(0, fromSchemeDBObjectInfo(dbo));
        }
        return res;
    }
    
    public void publish(String oid, UserInfo ui) {
    	DBCollection coll = getCollection();
        DBObject updateById = new BasicDBObject("_id", new ObjectId(oid));
        updateById.put("owner", ui.getUserId());
        DBObject update = new BasicDBObject("$set", new BasicDBObject("author", ui.getUserId()));
        update.put("$unset", new BasicDBObject("owner", 1));
        coll.update(updateById, update);
        
        // update userinfo

		ui.setPublicCount(ui.getPublicCount() + 1);
    	ui.setPrivateCount(ui.getPrivateCount() - 1);
        _uim.updateCount(ui);
        
    }
    
    /**
     * Only owner or author can remove scheme.
     * @param oid
     * @param ui
     * @return
     */
    public boolean remove(String oid, UserInfo ui) {
    	DBCollection coll = getCollection();
        DBObject removeBy = new BasicDBObject("_id", new ObjectId(oid));
        BasicDBList list = new BasicDBList();
        list.add(new BasicDBObject("owner", ui.getUserId()));
        list.add(new BasicDBObject("author", ui.getUserId()));
        removeBy.put("$or", list);
        Scheme rs = fromSchemeDBObject(coll.findOne(removeBy));
        boolean removed = coll.remove(removeBy).getN() == 1;
        if (!removed) {
        	return removed;
        }
        
        // update userinfo
        
        if (rs.getAuthor() != null) { // public scheme
    		ui.setPublicCount(ui.getPublicCount() - 1);
        } else if (rs.getOwner() != null) { // private scheme
        	ui.setPrivateCount(ui.getPrivateCount() - 1);
        }
        
        _uim.updateCount(ui);
        
        return removed;
    }
    
    public void update(Scheme s, UserInfo ui) {
    	DBCollection coll = getCollection();
        DBObject updateBy = new BasicDBObject("_id", new ObjectId(s.getOid()));
        BasicDBList list = new BasicDBList();
        list.add(new BasicDBObject("owner", ui.getUserId()));
        list.add(new BasicDBObject("author", ui.getUserId()));
        updateBy.put("$or", list);
        coll.update(updateBy, new BasicDBObject("$set", toSchemeDBObject(s)));
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
            case IconText: {
                IconText is = (IconText)value;
                doc.put("text", is.getText());
                doc.put("icon", is.getIconType());
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
    	if (scheme == null) {
    		return null;
    	}
        Scheme s = fromSchemeDBObjectInfo(scheme);

        s.setRoot(fromNodeDBObject((BasicDBObject)scheme.get("root")));
        
        return s;
    }
    
    private Scheme fromSchemeDBObjectInfo(DBObject scheme) {
    	Scheme s = new Scheme();
        
        s.setSchemePicture((String)scheme.get("screen"));
        s.setOid(((ObjectId)scheme.get("_id")).toStringMongod());
        s.setAuthor(_uim.selectByOid((String)scheme.get("author")));
        s.setOwner(_uim.selectByOid((String)scheme.get("owner")));
        
        return s;
    }
    
    private Node fromNodeDBObject(BasicDBObject root) {
    	if (root == null) {
    		return null;
    	}
        Node node = new Node();
        NodeType type = NodeType.valueOf(root.getString("type"));
        
        switch(type){
            case IconText: {
                IconText is = new IconText();
                is.setText(root.getString("text"));
                is.setIconType(root.getString("icon"));
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
