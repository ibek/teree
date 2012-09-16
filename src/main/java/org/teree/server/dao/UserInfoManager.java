package org.teree.server.dao;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.teree.shared.data.UserInfo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UserInfoManager {

	@Inject
    MongoDB mdb;
    
    private DBCollection getCollection() {
    	DB db = mdb.getDatabase();
        DBCollection coll = db.getCollection("user");
        if(coll == null){
            coll = db.createCollection("user", null);
        }
        return coll;
    }
    
    public void insert(UserInfo ui, String password) {
        BasicDBObject doc = new BasicDBObject();

        doc.put("username", ui.getUsername());
        doc.put("name", ui.getName());
        doc.put("email", ui.getEmail());
        doc.put("password", password);
        
        DBCollection coll = getCollection();
        coll.insert(doc);
    }
    
    public void insertWithGoogleId(UserInfo ui, String googleid) {
        BasicDBObject doc = new BasicDBObject();

        doc.put("name", ui.getName());
        doc.put("email", ui.getEmail());
        doc.put("googleid", googleid);
        
        DBCollection coll = getCollection();
        coll.insert(doc);
    }
    
    public void update(UserInfo ui, String password) {
    	
    }
    
    public void updateWithGoogleId(UserInfo ui, String googleid) {
    	
    }
    
    public UserInfo select(String username, String password) {
    	DBCollection coll = getCollection();
        DBObject search = new BasicDBObject();
        search.put("username", username);
        search.put("password", password);
        DBObject found = coll.findOne(search);
    	return fromUserInfoDBObject(found);
    }
    
    public UserInfo selectByGoogleId(String googleid) {
    	DBCollection coll = getCollection();
        DBObject search = new BasicDBObject();
        search.put("googleid", googleid);
        DBObject found = coll.findOne(search);
    	return fromUserInfoDBObject(found);
    }
    
    private UserInfo fromUserInfoDBObject(DBObject userinfo) {
    	if (userinfo == null) {
    		return null;
    	}
    	
        UserInfo ui = new UserInfo();

        ui.setUserId(((ObjectId)userinfo.get("_id")).toStringMongod());
        ui.setUsername((String)userinfo.get("username"));
        ui.setName((String)userinfo.get("name"));
        ui.setEmail((String)userinfo.get("email"));
        
        return ui;
    }
    
}
