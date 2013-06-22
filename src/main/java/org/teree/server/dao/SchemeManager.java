/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.server.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.teree.server.util.MongoDBFilter;
import org.teree.shared.data.common.Connector;
import org.teree.shared.data.common.Text;
import org.teree.shared.data.common.ImageLink;
import org.teree.shared.data.common.Link;
import org.teree.shared.data.common.MathExpression;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeCategory;
import org.teree.shared.data.common.PercentText;
import org.teree.shared.data.common.Permissions;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.SchemeFilter;
import org.teree.shared.data.common.StructureType;
import org.teree.shared.data.common.UserPermissions;
import org.teree.shared.data.common.Node.NodeLocation;
import org.teree.shared.data.common.Node.NodeType;
import org.teree.shared.data.tree.Tree;
import org.teree.shared.data.tree.TreeType;
import org.teree.shared.data.UserInfo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Stateless
public class SchemeManager {

	@Inject
    MongoDB _mdb;
    
	@Inject
	UserInfoManager _uim;
    
	@Inject
	NodeCategoryManager _ncm;
    
    private DBCollection getCollection() {
    	DB db = _mdb.getDatabase();
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
        dbo.put("structure", s.getStructure().name());
        dbo.put("author", ui.getUserId());
        dbo.put("permissions", toPermissionsDBObject(new Permissions()));
        
        DBCollection coll = getCollection();
        coll.insert(dbo);
        String oid = ((ObjectId)dbo.get("_id")).toStringMongod();
        
        return oid;
        
    }

	public List<Scheme> selectFrom(String fromOid, SchemeFilter schemeFilter, int limit, UserInfo ui) {
    	DBObject keys = new BasicDBObject();
    	keys.put("root.childNodes", 0);

		MongoDBFilter mdbfilter = new MongoDBFilter();
		mdbfilter.setSchemeFilter(schemeFilter);
		DBObject filter = mdbfilter.getFilter();
    	if (fromOid != null) {
    		filter.put("_id", new BasicDBObject("$lt", new ObjectId(fromOid)));
    	}
    	putSelectSecurityConditions(filter, ui);
    	
    	List<Scheme> res = new ArrayList<Scheme>();
    	DBCollection coll = getCollection();
        DBCursor found = coll.find(filter, keys)
        		.sort(new BasicDBObject("_id", -1))
        		.limit(limit);
        while(found.hasNext()) {
        	DBObject dbo = found.next();
        	res.add(fromSchemeDBObjectInfo(dbo));
        }
        return res;
    }
    
    public List<Scheme> selectTo(String toOid, SchemeFilter schemeFilter, int limit, UserInfo ui) {
    	DBObject keys = new BasicDBObject();
    	keys.put("root.childNodes", 0);
    	
		MongoDBFilter mdbfilter = new MongoDBFilter();
		mdbfilter.setSchemeFilter(schemeFilter);
		DBObject filter = mdbfilter.getFilter();
    	if (toOid != null) {
    		filter.put("_id", new BasicDBObject("$gt", new ObjectId(toOid)));
    	}
    	putSelectSecurityConditions(filter, ui);
    	
    	List<Scheme> res = new ArrayList<Scheme>();
    	DBCollection coll = getCollection();
        DBCursor found = coll.find(filter, keys)
        		.sort(new BasicDBObject("_id", 1))
        		.limit(limit);
        while(found.hasNext()) {
        	DBObject dbo = found.next();
        	res.add(0, fromSchemeDBObjectInfo(dbo));
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
        //fromSchemeDBObject(coll.findOne(removeBy));
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
    
    public String exportJSON(String oid, UserInfo ui) {
    	if (oid == null) {
    		return null;
    	}
    	try {
	        DBObject searchById = new BasicDBObject("_id", new ObjectId(oid));
	        putSelectSecurityConditions(searchById, ui);
	        DBCollection coll = getCollection();
	        BasicDBObject filter = new BasicDBObject("screen", 0); // without preview
	        DBObject found = coll.findOne(searchById, filter);
	        found.removeField("_id"); // id won't be exported
	        found.removeField("author");
	        String json = JSON.serialize(found);
	        return json;
    	} catch (Exception ex) {
    		return null;
    	}
    }
    
    public Scheme importJSON(String json) {
    	try {
	    	DBObject data = (DBObject)JSON.parse(json);
	    	data.removeField("_id"); // id cannot be used for import
	    	return fromSchemeDBObject(data);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	
    	return null;
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
        
        switch (s.getStructure()) {
	        case Tree: {
	        	Tree tree = (Tree)s;
	            doc.put("root", toNodeDBObject(tree.getRoot()));
	            doc.put("visualization", tree.getVisualization().name());
	        	break;
	        }
        }
        
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
                Text is = (Text)value;
                doc.put("text", is.getText());
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
            case Connector: {
            	Connector con = (Connector)value;
            	doc.put("text", con.getRoot().getText());
            	doc.put("conid", con.getOid());
            	break;
            }
            case Percent: {
                PercentText pt = (PercentText)value;
                doc.put("text", pt.getText());
                doc.put("percentage", pt.getPercentage());
                doc.put("group", pt.getGroup());
                break;
            }
        }
        
        if (root.getLocation() != null) {
            doc.put("location", root.getLocation().name());
        }
        if (root.getCategory() != null) {
        	doc.put("category", root.getCategory().getOid());
        }
        
        doc.put("childNodes", toDBList(root.getChildNodes()));
        
        return doc;
    }
    
    private Scheme fromSchemeDBObject(DBObject scheme) {
    	if (scheme == null) {
    		return null;
    	}
    	Scheme s = fromSchemeDBObjectInfo(scheme);

    	switch (s.getStructure()) {
	        case Tree: {
	        	Tree tree = (Tree)s;
	            tree.setRoot(fromNodeDBObject((BasicDBObject)scheme.get("root")));
	            String[] oids = new String[categories.size()];
	            Collection<NodeCategory> ncv = categories.values();
	            int i = 0;
	            for (NodeCategory nc : ncv) {
	            	String oid = nc.getOid();
	            	if (oid != null) {
		            	oids[i] = oid;
		            	i++;
	            	}
	            }
	            List<NodeCategory> nclist = _ncm.selectByOids(oids);
	            for (NodeCategory nc : ncv) {
	            	for (NodeCategory updated : nclist) {
	            		String oid = nc.getOid();
	            		if (oid != null && oid.equals(updated.getOid())) {
	            			nc.set(updated);
	            			nclist.remove(updated); // optimization - won't be necessary any more
	            			break;
	            		}
	            	}
	            }
	            categories.clear(); // categories are set in fromNodeDBObject method
	        	break;
	        }
    	}
        
        return s;
    }
    
    private Scheme fromSchemeDBObjectInfo(DBObject scheme) {
    	Scheme s = null;
    	StructureType type = StructureType.valueOf((String)scheme.get("structure"));
    	
    	switch (type) {
	    	case Tree: {
	    		Tree tree = new Tree();
	        	Node root = new Node();
	        	Text it = new Text();
	        	DBObject r = (DBObject)scheme.get("root");
	        	it.setText((String)r.get("text"));
	        	root.setContent(it);
	        	
	        	tree.setRoot(root);
	        	tree.setVisualization(TreeType.valueOf((String)scheme.get("visualization")));
	        	
	        	s = tree;
	    		break;
	    	}
    	}
        
        s.setSchemePicture((String)scheme.get("screen"));
        if (scheme.get("_id") != null) { // for import it is null
        	s.setOid(((ObjectId)scheme.get("_id")).toStringMongod());
        }
        s.setAuthor(_uim.selectByOid((String)scheme.get("author")));
    	s.setPermissions(fromPermissionsDBObject((DBObject)scheme.get("permissions")));
        
        return s;
    }

    private Map<String, NodeCategory> categories = new HashMap<String, NodeCategory>();
	private Node fromNodeDBObject(BasicDBObject root) {
    	if (root == null) {
    		return null;
    	}
        Node node = new Node();
        NodeType type = NodeType.valueOf(root.getString("type"));
        
        switch(type){
            case IconText: {
                Text is = new Text();
                is.setText(root.getString("text"));
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
            case Connector: {
            	Connector con = new Connector();
            	Text it = new Text();
            	it.setText(root.getString("text"));
            	con.setRoot(it);
            	con.setOid(root.getString("conid"));
            	node.setContent(con);
            	break;
            }
            case Percent: {
                PercentText pt = new PercentText();
                pt.setText(root.getString("text"));
                pt.setPercentage(root.getInt("percentage"));
                pt.setGroup(root.getInt("group"));
                node.setContent(pt);
                break;
            }
        }
        
        String location = root.getString("location");
        if(location != null){
            node.setLocation(NodeLocation.valueOf(location));
        }
        String ncoid = root.getString("category");
    	NodeCategory nc = new NodeCategory();
        if (ncoid != null) {
        	if (!categories.containsKey(ncoid)) {
		        nc.setOid(ncoid); // fully loaded later
		        categories.put(ncoid, nc);
        	} else {
        		nc = categories.get(ncoid);
        	}
        }
        node.setCategory(nc);
        
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
            for(Node n : childNodes){
                doc.add(toNodeDBObject(n));
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
