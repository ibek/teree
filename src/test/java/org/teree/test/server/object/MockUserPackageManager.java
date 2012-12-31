package org.teree.test.server.object;

import org.teree.server.dao.MongoDB;
import org.teree.server.dao.UserPackageManager;
import org.teree.shared.data.UserPackage;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class MockUserPackageManager extends UserPackageManager {
	
	public MockUserPackageManager(MongoDB mdb) {
		//this.mdb = mdb;
	}
	
}
