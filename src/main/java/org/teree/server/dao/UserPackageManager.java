package org.teree.server.dao;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.teree.shared.data.UserPackage;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Stateless
public class UserPackageManager {
	
	private static final int COUNT_OF_PARAMETERS = 1; // "primary" key name is not counted

	@Inject
    MongoDB mdb;
    
    protected DBCollection getCollection() {
    	DB db = mdb.getDatabase();
        DBCollection coll = null;
        if(db.getCollectionNames().contains("package")){
        	coll = db.getCollection("package");
        } else {
            coll = db.createCollection("package", null);
            initDB(coll);
        }
        return coll;
    }
    
    protected void initDB(DBCollection coll) {
    	try {
        	Properties prop = new Properties();
        	prop.load(getClass().getResourceAsStream("/UserPackages.properties"));
        	Iterator<Object> iter = prop.keySet().iterator();
        	while (iter.hasNext()) {
        		Object key = iter.next();
        		BasicDBObject dbo = new BasicDBObject("name", key);
        		String[] attr = prop.get(key).toString().split(",");
        		if (attr.length == COUNT_OF_PARAMETERS) {
        			dbo.put("memLimit", attr[0]);
        		}
                coll.insert(dbo);
        	}
    	} catch (IOException ex) {
    		// ignore
    	}
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
    	up.setMemLimit((String)userPackage.get("memLimit"));
    	
    	return up;
    }
    
}
