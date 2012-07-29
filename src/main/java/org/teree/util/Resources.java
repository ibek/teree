package org.teree.util;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * This class uses CDI to alias Java EE resources to CDI beans
 */
public class Resources {
   
   @Produces
   public Logger produceLog(InjectionPoint injectionPoint) {
      return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
   }
   
   private static final String DBNAME = "teree";
   private static DB db;
   
   @Produces
   public DB getDatabase(InjectionPoint injectionPoint) {
       if(db == null){
           try {
               Mongo mongodb = new Mongo("localhost");
               db = mongodb.getDB(DBNAME);
           } catch (UnknownHostException e) {
               e.printStackTrace();
           } catch (MongoException e) {
               e.printStackTrace();
           }
       }
       return db;
   }
   
}
