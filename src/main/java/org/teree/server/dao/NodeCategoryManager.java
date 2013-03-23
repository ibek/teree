package org.teree.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.common.NodeCategory;
import org.teree.shared.data.common.Scheme;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Stateless
public class NodeCategoryManager {

	@Inject
    MongoDB _mdb;
    
	@Inject
	UserInfoManager _uim;
    
    protected DBCollection getCollection() {
    	DB db = _mdb.getDatabase();
        DBCollection coll = null;
        if(db.getCollectionNames().contains("category")){
        	coll = db.getCollection("category");
        } else {
            coll = db.createCollection("category", null);
            coll.ensureIndex(new BasicDBObject("name", 1), new BasicDBObject("unique", true));
        }
        return coll;
    }
    
    public String insert(NodeCategory category) {
        DBObject doc = toCategoryDBObject(category);

        doc.put("owner", category.getOwner().getUserId());
        
        DBCollection coll = getCollection();
        coll.insert(doc);
        return ((ObjectId)doc.get("_id")).toStringMongod();
    }
    
    public void update(NodeCategory category, UserInfo ui) {
    	DBCollection coll = getCollection();
        DBObject updateBy = new BasicDBObject("_id", new ObjectId(category.getOid()));
        updateBy.put("owner", ui.getUserId());
        coll.update(updateBy, new BasicDBObject("$set", toCategoryDBObject(category)));
    }
    
    /**
     * Only owner can remove node category.
     * @param oid
     * @param ui
     * @return
     */
    public boolean remove(String oid, UserInfo ui) {
    	DBCollection coll = getCollection();
        DBObject removeBy = new BasicDBObject("_id", new ObjectId(oid));
        removeBy.put("owner", ui.getUserId());
        //fromCategoryDBObject(coll.findOne(removeBy));
        boolean removed = coll.remove(removeBy).getLastError().ok();
        return removed;
    }
    
    public List<NodeCategory> selectByOids(String... oids) {
    	DBCollection coll = getCollection();
    	BasicDBList idlist = new BasicDBList();
        DBObject search = new BasicDBObject("$or", idlist);
        for (String o: oids) {
        	idlist.add(new BasicDBObject("_id", new ObjectId(o)));
        }
    	
        DBCursor found = coll.find(search);
        List<NodeCategory> res = new ArrayList<NodeCategory>();
        while(found.hasNext()) {
        	DBObject dbo = found.next();
        	res.add(fromCategoryDBObject(dbo));
        }
    	return res;
    }
    
    public List<NodeCategory> selectAll(UserInfo ui) {
        List<NodeCategory> res = new ArrayList<NodeCategory>();
        if (ui == null) {
        	return res;
        }
    	DBCollection coll = getCollection();
        DBObject search = new BasicDBObject("owner", ui.getUserId());
    	
        DBCursor found = coll.find(search);
        while(found.hasNext()) {
        	DBObject dbo = found.next();
        	res.add(fromCategoryDBObject(dbo));
        }
    	return res;
    }
    
    private NodeCategory fromCategoryDBObject(DBObject category) {
    	if (category == null) {
    		return null;
    	}
    	
        NodeCategory nc = new NodeCategory();
        
        nc.setOid(((ObjectId)category.get("_id")).toStringMongod());
        nc.setName((String)category.get("name"));
        nc.setOwner(_uim.selectByOid((String)category.get("owner")));
        nc.setBold((Boolean)category.get("bold"));
        nc.setIconType((String)category.get("icontype"));
        
        return nc;
    }
    
    private DBObject toCategoryDBObject(NodeCategory category) {
    	BasicDBObject doc = new BasicDBObject();

        doc.put("name", category.getName());
        doc.put("bold", category.isBold());
        doc.put("icontype", category.getIconType());
        
        return doc;
    }
    
}
