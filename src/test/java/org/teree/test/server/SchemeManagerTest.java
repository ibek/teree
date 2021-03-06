/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.test.server;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.BeforeClass;
import org.junit.Test;
import org.teree.server.dao.MongoDB;
import org.teree.server.dao.SchemeManager;
import org.teree.server.dao.UserInfoManager;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.SchemeFilter;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class SchemeManagerTest {
	
	private static SchemeManager sm;
	private static UserInfoManager uim;
	private static MongoDB mdb;
	
	@BeforeClass
	public static void initDB() {
		mdb = new MongoDB() {
			@Override
			public DB getDatabase() {
				if (db == null) {
					try {
					    mongodb = new Mongo("127.0.0.1");
					    db = mongodb.getDB("teree");
					} catch (Exception e) {
					    e.printStackTrace();
					}
				}
				return db;
			}
		};
		uim = new UserInfoManager();
		try {
			Class<?> c = uim.getClass();
			Field f = c.getDeclaredField("_mdb");
			f.setAccessible(true);
			f.set(uim, mdb);
	    } catch (Exception x) {
	      x.printStackTrace();
	    }
		
		sm = new SchemeManager();
		try {
			Class<?> c = sm.getClass();
			Field f = c.getDeclaredField("_mdb");
			f.setAccessible(true);
			f.set(sm, mdb);
			f = c.getDeclaredField("_uim");
			f.setAccessible(true);
			f.set(sm, uim);
	    } catch (Exception x) {
	      x.printStackTrace();
	    }
	}
	
	@Test
	public void testInsertUserInfo() {
		initDB();
		UserInfo ui = new UserInfo();
		ui.setUserId("50e8a5efe4b09e11e18873c7");
		//List<Scheme> s = sm.searchFrom(null, "vzor", 5, ui);
		List<Scheme> s = sm.selectFrom(null, new SchemeFilter(), 5, ui);
		System.out.println("ahoj");
		for (Scheme sc:s) {
			System.out.println("cau");
			System.out.println(sc.toString());
		}
	}

}
