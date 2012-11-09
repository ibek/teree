package org.teree.test.server.object;

import org.teree.server.dao.UserInfoManager;
import org.teree.server.dao.UserPackageManager;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MockUserInfoManager extends UserInfoManager {

	private DB db;
	private UserPackageManager upm;
	
	public MockUserInfoManager(DB db) {
		this.db = db;
		upm = new MockUserPackageManager(db);
		
	}
	
	@Override
	protected DBCollection getCollection() {
		DBCollection coll = db.getCollection("user");
		if (coll == null) {
			coll = db.createCollection("user", null);
		}
		coll.ensureIndex(new BasicDBObject("username",1), null, true);
		return coll;
	}
	
	@Override
	protected UserPackageManager getUserPackageManager() {
		return upm;
	}
	
}
