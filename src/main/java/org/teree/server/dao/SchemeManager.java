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
import org.teree.shared.data.scheme.MathExpression;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;
import org.teree.shared.data.scheme.Permissions;
import org.teree.shared.data.scheme.Scheme;
import org.teree.shared.data.scheme.Node.NodeLocation;
import org.teree.shared.data.scheme.Node.NodeType;
import org.teree.shared.data.scheme.UserPermissions;
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
    public Scheme select(String oid, UserInfo ui) {
    	if (oid == null) {
    		return null;
    	}
    	try {
	        DBObject searchById = new BasicDBObject("_id", new ObjectId(oid));
	        putSelectSecurityConditions(searchById, ui);
	        DBCollection coll = getCollection();
	        DBObject found = coll.findOne(searchById);
	        return fromSchemeDBObject(found);
    	} catch (Exception ex) {
    		return null;
    	}
    }
    
    /**
     * Select specific scheme identified by oid.
     * @param oid
     * @return node
     */
    public Scheme selectToEdit(String oid, UserInfo ui) {
    	if (oid == null) {
    		return null;
    	}
    	try {
	        DBObject searchById = new BasicDBObject("_id", new ObjectId(oid));
	        putEditSecurityConditions(searchById, ui);
	        DBCollection coll = getCollection();
	        DBObject found = coll.findOne(searchById);
	        return fromSchemeDBObject(found);
    	} catch (Exception ex) {
    		return null;
    	}
    }
    
    public String insert(Scheme s, UserInfo ui) {
    	if (ui == null) {
    		return null;
    	}
        DBObject dbo = toSchemeDBObject(s);
        dbo.put("author", ui.getUserId());
        dbo.put("permissions", toPermissionsDBObject(new Permissions()));
        
        DBCollection coll = getCollection();
        coll.insert(dbo);
        String oid = ((ObjectId)dbo.get("_id")).toStringMongod();
        
        return oid;
        
    }

	public List<Scheme> allFrom(String from_oid, int limit, UserInfo ui) {
    	DBObject keys = new BasicDBObject();
    	keys.put("root", 0);
    	DBObject ref = new BasicDBObject();
    	putSelectSecurityConditions(ref, ui);
    	if (from_oid != null) {
    		ref = new BasicDBObject("_id", new BasicDBObject("$lt", new ObjectId(from_oid)));
    	}
    	return allFrom(ref, keys, limit, ui);
    }
    
    public List<Scheme> allTo(String to_oid, int limit, UserInfo ui) {
    	DBObject keys = new BasicDBObject();
    	keys.put("root", 0);
    	DBObject ref = new BasicDBObject();
    	putSelectSecurityConditions(ref, ui);
    	if (to_oid != null) {
    		ref = new BasicDBObject("_id", new BasicDBObject("$gt", new ObjectId(to_oid)));
    	}
    	return allTo(ref, keys, limit, ui);
    }

	public List<Scheme> allFromUser(String from_oid, int limit, String userid, UserInfo ui) {
    	DBObject keys = new BasicDBObject();
    	keys.put("root", 0);
    	DBObject ref = new BasicDBObject("author", userid);
    	putSelectSecurityConditions(ref, ui);
    	if (from_oid != null) {
    		ref = new BasicDBObject("_id", new BasicDBObject("$lt", new ObjectId(from_oid)));
    	}
    	return allFrom(ref, keys, limit, ui);
    }
    
    public List<Scheme> allToUser(String to_oid, int limit, String userid, UserInfo ui) {
    	DBObject keys = new BasicDBObject();
    	keys.put("root", 0);
    	DBObject ref = new BasicDBObject("author", userid);
    	putSelectSecurityConditions(ref, ui);
    	if (to_oid != null) {
    		ref = new BasicDBObject("_id", new BasicDBObject("$gt", new ObjectId(to_oid)));
    	}
    	return allTo(ref, keys, limit, ui);
    }

    private List<Scheme> allFrom(DBObject ref, DBObject keys, int limit, UserInfo ui) {
    	List<Scheme> res = new ArrayList<Scheme>();
    	DBCollection coll = getCollection();
        DBCursor found = coll.find(ref, keys)
        		.sort(new BasicDBObject("_id", -1))
        		.limit(limit);
        while(found.hasNext()) {
        	DBObject dbo = found.next();
        	res.add(fromSchemeDBObjectInfo(dbo, ui));
        }
        return res;
    }

    private List<Scheme> allTo(DBObject ref, DBObject keys, int limit, UserInfo ui) {
    	List<Scheme> res = new ArrayList<Scheme>();
    	DBCollection coll = getCollection();
        DBCursor found = coll.find(ref, keys)
        		.sort(new BasicDBObject("_id", 1))
        		.limit(limit);
        while(found.hasNext()) {
        	DBObject dbo = found.next();
        	res.add(0, fromSchemeDBObjectInfo(dbo, ui));
        }
        return res;
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
        removeBy.put("author", ui.getUserId()); // only author can remove scheme
        fromSchemeDBObject(coll.findOne(removeBy));
        boolean removed = coll.remove(removeBy).getLastError().ok();
        if (!removed) {
        	return removed;
        }
        
        return removed;
    }
    
    public void update(Scheme s, UserInfo ui) {
    	DBCollection coll = getCollection();
        DBObject updateBy = new BasicDBObject("_id", new ObjectId(s.getOid()));
        putEditSecurityConditions(updateBy, ui);
        coll.update(updateBy, new BasicDBObject("$set", toSchemeDBObject(s)));
    }
    
    public void updatePermissions(Scheme s, UserInfo ui) {
    	DBCollection coll = getCollection();
        DBObject updateBy = new BasicDBObject("_id", new ObjectId(s.getOid()));
        updateBy.put("author", ui.getUserId()); // only author can update permissions
        coll.update(updateBy, new BasicDBObject("$set", new BasicDBObject("permissions", toPermissionsDBObject(s.getPermissions()))));
    }
    
    private DBObject putSelectSecurityConditions(DBObject req, UserInfo ui) {
    	BasicDBList perm = new BasicDBList();
    	if (ui != null) {
    		perm.add(new BasicDBObject("author", ui.getUserId()));
    	   perm.add(new BasicDBObject("permissions.users.userid", ui.getUserId()));
    	}
    	perm.add(new BasicDBObject("permissions.write", new BasicDBObject("$exists", true)));
    	req.put("$or", perm);
    	return req;
    }
    
    private DBObject putEditSecurityConditions(DBObject req, UserInfo ui) {
    	BasicDBList perm = new BasicDBList();
    	if (ui != null) {
    		perm.add(new BasicDBObject("author", ui.getUserId()));
    		BasicDBObject user = new BasicDBObject("permissions.users.userid", ui.getUserId());
    		user.put("permissions.users.write", true);
    	    perm.add(user);
    	}
    	perm.add(new BasicDBObject("permissions.write", true));
    	req.put("$or", perm);
    	return req;
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
            case MathExpression: {
            	MathExpression me = (MathExpression)value;
            	doc.put("mathexpr", me.getExpression());
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
        Scheme s = fromSchemeDBObjectInfo(scheme, null);

        s.setRoot(fromNodeDBObject((BasicDBObject)scheme.get("root")));
        
        return s;
    }
    
    private Scheme fromSchemeDBObjectInfo(DBObject scheme, UserInfo ui) {
    	Scheme s = new Scheme();
        
        s.setSchemePicture((String)scheme.get("screen"));
        s.setOid(((ObjectId)scheme.get("_id")).toStringMongod());
        s.setAuthor(_uim.selectByOid((String)scheme.get("author")));
    	s.setPermissions(fromPermissionsDBObject((DBObject)scheme.get("permissions")));
        
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
            case MathExpression: {
            	MathExpression me = new MathExpression();
            	me.setExpression(root.getString("mathexpr"));
            	node.setContent(me);
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
    
    private Permissions fromPermissionsDBObject(DBObject permissions) {
    	Permissions p = new Permissions();
    	
    	p.setWrite((Boolean)permissions.get("write"));
    	BasicDBList list = (BasicDBList)permissions.get("users");
    	if (list != null) {
        	List<UserPermissions> users = new ArrayList<UserPermissions>();
	    	Iterator<Object> it = list.iterator();
	        while(it.hasNext()){
	        	DBObject upo = (BasicDBObject)it.next();
	        	UserPermissions up = new UserPermissions();
	        	up.setWrite((Boolean)upo.get("write"));
	        	up.setUser(_uim.selectByOid((String)upo.get("userid")));
	            users.add(up);
	        }
	    	p.setUsers(users);
    	}
    	
    	return p;
    }
    
    private Object toPermissionsDBObject(Permissions permissions) {
    	BasicDBObject doc = new BasicDBObject();
        
    	if (permissions.getWrite() != null) {
    		doc.put("write", permissions.getWrite());
    	}
        
        BasicDBList users = new BasicDBList();
        List<UserPermissions> up = permissions.getUsers();
        if (up != null) {
	        for (UserPermissions u:up) {
	        	DBObject user = new BasicDBObject();
	        	if (u.getUser() != null && (u.getUser().getUserId() == null || u.getUser().getUserId().isEmpty())) {
	        		u.setUser(_uim.selectByEmail(u.getUser().getEmail()));
	        	}
	        	if (u.getUser() != null) {
		        	user.put("userid", u.getUser().getUserId());
		        	if (u.getWrite() != null) {
		        		user.put("write", u.getWrite());
		        	}
		        	users.add(user);
	        	}
	        }
        }
        doc.put("users", users);
        
        return doc;
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
