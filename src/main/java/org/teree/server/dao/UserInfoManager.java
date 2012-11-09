package org.teree.server.dao;

import java.text.DateFormat;
import java.util.Date;

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
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class UserInfoManager {

	@Inject
    MongoDB mdb;
	
	@Inject
	UserPackageManager upm;
    
    private DBCollection getCollection() {
    	DB db = mdb.getDatabase();
        DBCollection coll = db.getCollection("user");
        if(coll == null){
            coll = db.createCollection("user", null);
            coll.ensureIndex(new BasicDBObject("username", 1), new BasicDBObject("unique", true));
        }
        return coll;
    }
    
    public boolean insert(UserInfo ui, String password) {
        DBObject doc = toUserInfoDBObject(ui);

        doc.put("username", ui.getUsername());
        doc.put("password", BCrypt.hashpw(password, BCrypt.gensalt(12)));
        doc.put("package", upm.getFreePackage().getName());
        doc.put("joined", DateFormat.getDateInstance(DateFormat.DEFAULT).format(new Date(System.currentTimeMillis())));
        
        DBCollection coll = getCollection();
        WriteResult wr = coll.insert(doc);
        return wr.getLastError().ok() && doc.get("_id") != null; // FIXME: has to return false when username already exists
    }
    
    public void insertWithGoogleId(UserInfo ui, String googleid) {
        DBObject doc = toUserInfoDBObject(ui);
        
        doc.put("googleid", googleid);
        doc.put("package", upm.getFreePackage().getName());
        
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
    
    public void updateCount(UserInfo ui) {
    	DBCollection coll = getCollection();
        DBObject updateBy = getUpdateBy(ui);
        BasicDBObject doc = new BasicDBObject();

        // only these properties are updated
        doc.put("publicCount", ui.getPublicCount());
        doc.put("privateCount", ui.getPrivateCount());
        
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
    	return (String)found.get("password");
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
        ui.setPublicCount((Integer)userinfo.get("publicCount"));
        ui.setPrivateCount((Integer)userinfo.get("privateCount"));
        ui.setMemUsed((Long)userinfo.get("memUsed"));
        ui.setUserPackage(upm.select((String)userinfo.get("package")));
        
        return ui;
    }
    
    private DBObject toUserInfoDBObject(UserInfo ui) {
    	BasicDBObject doc = new BasicDBObject();

        doc.put("name", ui.getName());
        doc.put("email", ui.getEmail());
        doc.put("publicCount", ui.getPublicCount());
        doc.put("privateCount", ui.getPrivateCount());
        doc.put("memUsed", ui.getMemUsed());
        
        return doc;
    }
    
}
