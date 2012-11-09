package org.teree.test.server;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teree.shared.data.UserInfo;
import org.teree.test.server.object.MockUserInfoManager;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class UserInfoManagerTest {

	private static MockUserInfoManager uim;
	private static DB db = null;
	
	@BeforeClass
	public static void init() {
		try {
		    Mongo mongodb = new Mongo("127.0.0.1");
		    db = mongodb.getDB("teree_testdb");
		    db.dropDatabase();
		} catch (UnknownHostException e) {
		    e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
		uim = new MockUserInfoManager(db);
	}
	
	@After
	public void clearDB() {
		db.dropDatabase();
	}
	
	@Test
	public void testInsertUserInfo() {
		insertUserInfo("admin", "Administrator", "admin@teree.org");
	}
	
	private void insertUserInfo(String username, String name, String email) {
		UserInfo admin = new UserInfo();
		admin.setUsername(username);
		admin.setName(name);
		admin.setEmail(email);
		
		boolean inserted = uim.insert(admin, "password");
		Assert.assertTrue(inserted);
		
		UserInfo selected = uim.select(username);

		Assert.assertEquals(admin.getUsername(), selected.getUsername());
		Assert.assertEquals(admin.getName(), selected.getName());
		Assert.assertEquals(admin.getEmail(), selected.getEmail());
	}

	@Test
	public void testInsertExistingUser() {
		insertUserInfo("admin", "Administrator", "admin@teree.org");
		
		UserInfo admin = new UserInfo();
		admin.setUsername("admin");
		admin.setName("Second Administrator");
		admin.setEmail("admin@teree.org");
		
		boolean inserted = uim.insert(admin, "password");
		Assert.assertFalse(inserted); // the user already exists
	}
	
	@Test
	public void testMultipleInsertUserInfo() {
		insertUserInfo("admin", "Administrator", "admin@teree.org");
		insertUserInfo("admin2", "Administrator2", "admin@teree.org");
	}

}
