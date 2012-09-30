package org.teree.server.dao;

import javax.inject.Inject;

import org.teree.shared.data.UserPackage;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UserPackageManager {

	@Inject
    MongoDB mdb;
    
    private DBCollection getCollection() {
    	DB db = mdb.getDatabase();
        DBCollection coll = db.getCollection("package");
        if(coll == null){
            coll = db.createCollection("package", null);
        }
        return coll;
    }
    
    public UserPackage getFreePackage() {
    	return select("free");
    }
    
    public UserPackage select(String name) {
    	DBCollection coll = getCollection();
    	
        DBObject search = new BasicDBObject();
        search.put("name", name);
        DBObject found = coll.findOne(search);
        
    	return fromUserPackageDBObject(found);
    }
    
    private UserPackage fromUserPackageDBObject(DBObject userPackage) {
    	UserPackage up = new UserPackage();
    	
    	up.setName((String)userPackage.get("name"));
    	up.setMemLimit((Long)userPackage.get("memLimit"));
    	
    	return up;
    }
    
}
