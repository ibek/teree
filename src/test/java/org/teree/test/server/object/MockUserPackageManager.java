package org.teree.test.server.object;

import org.teree.server.dao.UserPackageManager;
import org.teree.shared.data.UserPackage;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class MockUserPackageManager extends UserPackageManager {

	private DB db;
	
	public MockUserPackageManager(DB db) {
		this.db = db;
	}
	
	@Override
	protected DBCollection getCollection() {
		DBCollection coll = db.getCollection("package");
		if (coll == null) {
			coll = db.createCollection("package", null);
		}
		return coll;
	}
	
	@Override
	public UserPackage getFreePackage() {
		UserPackage free = new UserPackage();
		free.setName("free");
		free.setMemLimit(10485760L);
		return free;
	}
	
	@Override
	public UserPackage select(String name) {
		return getFreePackage();
	}
	
}
