package org.teree.test.server.object;

import java.net.UnknownHostException;

import org.jboss.errai.ioc.client.api.TestMock;
import org.teree.server.dao.MongoDB;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

@TestMock
public class MockMongoDB extends MongoDB {

	private DB db;
	
	@Override
	public DB getDatabase() {
		if (db == null) {
			try {
			    Mongo mongodb = new Mongo("127.0.0.1");
			    db = mongodb.getDB("teree_testdb");
			    db.dropDatabase();
			} catch (UnknownHostException e) {
			    e.printStackTrace();
			} catch (MongoException e) {
				e.printStackTrace();
			}
		}
		return db;
	}
	
}
