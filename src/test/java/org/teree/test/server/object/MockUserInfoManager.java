package org.teree.test.server.object;

import org.teree.server.dao.MongoDB;
import org.teree.server.dao.UserInfoManager;
import org.teree.server.dao.UserPackageManager;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MockUserInfoManager extends UserInfoManager {
	
	public MockUserInfoManager(MongoDB mdb) {
		this.mdb = mdb;
		upm = new MockUserPackageManager(mdb);
	}
	
}
