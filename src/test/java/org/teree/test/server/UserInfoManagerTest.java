package org.teree.test.server;

public class UserInfoManagerTest {

	/**private static MockUserInfoManager uim;
	private static MongoDB mdb;
	
	@BeforeClass
	public static void init() {
		//mdb = new MockMongoDB();
		uim = new MockUserInfoManager(mdb);
	}
	
	@After
	public void clearDB() {
		mdb.getDatabase().dropDatabase();
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
	}*/

}
