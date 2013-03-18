package org.teree.server.dao;

import java.text.DateFormat;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.jboss.errai.bus.server.api.RpcContext;
import org.mindrot.jbcrypt.BCrypt;
import org.teree.shared.data.UserInfo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Stateless
public class UserInfoManager {

	@Inject
    MongoDB _mdb;
    
    protected DBCollection getCollection() {
    	DB db = _mdb.getDatabase();
        DBCollection coll = null;
        if(db.getCollectionNames().contains("user")){
        	coll = db.getCollection("user");
        } else {
            coll = db.createCollection("user", null);
            coll.ensureIndex(new BasicDBObject("username", 1), new BasicDBObject("unique", true));
            coll.ensureIndex(new BasicDBObject("email", 1), new BasicDBObject("unique", true));
        }
        return coll;
    }
    
    public boolean insert(UserInfo ui, String password) {
        DBObject doc = toUserInfoDBObject(ui);

        doc.put("username", ui.getUsername());
        doc.put("password", BCrypt.hashpw(password, BCrypt.gensalt(12)));
        doc.put("joined", DateFormat.getDateInstance(DateFormat.DEFAULT).format(new Date(System.currentTimeMillis())));
        
        DBCollection coll = getCollection();
        coll.insert(doc);
        UserInfo inserted = selectByOid(((ObjectId)doc.get("_id")).toStringMongod());
        return inserted != null && inserted.getUsername().equals(ui.getUsername());
    }
    
    public void insertWithGoogleId(UserInfo ui, String googleid) {
        DBObject doc = toUserInfoDBObject(ui);
        
        doc.put("googleid", googleid);
        doc.put("joined", DateFormat.getDateInstance(DateFormat.DEFAULT).format(new Date(System.currentTimeMillis())));
        
        DBCollection coll = getCollection();
        coll.insert(doc);
    }
    
    public void update(UserInfo ui) {
    	DBCollection coll = getCollection();
        DBObject updateBy = getUpdateBy(ui);
        BasicDBObject doc = new BasicDBObject();

        // only these properties are updated
        doc.put("name", ui.getName());
        doc.put("email", ui.getEmail());
        
        coll.update(updateBy, new BasicDBObject("$set", doc));
    }
    
    public void updateMemUsed(UserInfo ui) {
    	DBCollection coll = getCollection();
        DBObject updateBy = getUpdateBy(ui);
        BasicDBObject doc = new BasicDBObject();

        // only these properties are updated
        doc.put("memUsed", ui.getMemUsed());
        
        coll.update(updateBy, new BasicDBObject("$set", doc));
    }
    
    public void updatePassword(UserInfo ui, String newPassword) {
    	if (ui.getUsername() == null) {
    		return;
    	}
    	DBCollection coll = getCollection();
        DBObject updateBy = getUpdateBy(ui);
        BasicDBObject doc = new BasicDBObject();

        // only these properties are updated
        doc.put("password", BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
        
        coll.update(updateBy, new BasicDBObject("$set", doc));
    }
    
    private DBObject getUpdateBy(UserInfo ui) {
        DBObject updateBy = null;
        
        HttpSession session = RpcContext.getHttpSession();
        String googleid = (String)session.getAttribute("googleid");
        
        if (ui.getUsername() != null) {
        	updateBy = new BasicDBObject("username", ui.getUsername());
        } else if (googleid != null) {
        	updateBy = new BasicDBObject("googleid", googleid);
        }
        
        return updateBy;
    }
    
    public String getHashedPassword(String username) {
    	DBCollection coll = getCollection();
        DBObject search = new BasicDBObject();
        search.put("username", username);
        DBObject found = coll.findOne(search, new BasicDBObject("password", 1));
    	return (found == null)?null:(String)found.get("password");
    }
    
    // TODO return only public informations (not mem used and so)
    public UserInfo selectByOid(String oid) {
    	if (oid == null) {
    		return null;
    	}
    	DBCollection coll = getCollection();
        DBObject search = new BasicDBObject("_id", new ObjectId(oid));
        DBObject found = coll.findOne(search);
    	return fromUserInfoDBObject(found);
    }
    
    // TODO return only public informations (not mem used and so)
    public UserInfo selectByEmail(String email) {
    	if (email == null) {
    		return null;
    	}
    	DBCollection coll = getCollection();
        DBObject search = new BasicDBObject("email", email);
        DBObject found = coll.findOne(search);
    	return fromUserInfoDBObject(found);
    }
    
    public UserInfo select(String username) {
    	DBCollection coll = getCollection();
        DBObject search = new BasicDBObject();
        search.put("username", username);
        //search.put("password", password);
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
        ui.setJoined((String)userinfo.get("joined"));
        ui.setMemUsed((Long)userinfo.get("memUsed"));
        //ui.setUserPackage(getUserPackageManager().select((String)userinfo.get("package")));
        
        return ui;
    }
    
    private DBObject toUserInfoDBObject(UserInfo ui) {
    	BasicDBObject doc = new BasicDBObject();

        doc.put("name", ui.getName());
        doc.put("email", ui.getEmail());
        doc.put("memUsed", ui.getMemUsed());
        
        return doc;
    }
    
}
