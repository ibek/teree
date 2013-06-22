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
package org.teree.server.dao;

import java.net.UnknownHostException;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * Consider to have more MongoDB instances - but they should be closed when they are destroyed
 */
@ApplicationScoped
public class MongoDB {

   public static final String DBNAME = "teree";
   protected static Mongo mongodb;
   protected static DB db;
   
   public DB getDatabase() {
       if(db == null){
           try {
               //mongodb = new Mongo("127.0.0.1"); // localhost
        	   mongodb = new Mongo(System.getenv("OPENSHIFT_MONGODB_DB_HOST"), Integer.valueOf(System.getenv("OPENSHIFT_MONGODB_DB_PORT"))); // Openshift
               db = mongodb.getDB(DBNAME);
               if (!db.authenticate(System.getenv("OPENSHIFT_MONGODB_DB_USERNAME"), System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD").toCharArray())) {
            	   throw new MongoException("Authentication failed.");
               }
           } catch (UnknownHostException e) {
               e.printStackTrace();
           } catch (MongoException e) {
               e.printStackTrace();
           }
       }
       return db;
   }
   
   @PreDestroy
   public void destroy() {
	   if (mongodb != null) {
		   mongodb.close();
	   }
   }
	
}
