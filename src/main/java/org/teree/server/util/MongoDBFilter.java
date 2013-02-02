package org.teree.server.util;

import java.util.regex.Pattern;

import org.teree.shared.data.common.SchemeFilter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongoDBFilter {

	private DBObject filter;
	
	public MongoDBFilter() {
		filter = new BasicDBObject();
	}
	
	public DBObject getFilter() {
		return filter;
	}
	
	public MongoDBFilter author(String userid) {
		if (userid != null && !userid.isEmpty()) {
			filter.put("author", userid);
		}
		return this;
	}
	
	public MongoDBFilter search(String text) {
		if (text == null || text.isEmpty()) {
			return this;
		}
		String[] parts = text.split(" ");
    	BasicDBList pl = new BasicDBList();
    	for (String p:parts) {
    		pl.add(Pattern.compile(".*" + p + ".*", Pattern.CASE_INSENSITIVE));
    	}
		filter.put("root.text", new BasicDBObject("$in", pl));
		return this;
	}
	
	public void setSchemeFilter(SchemeFilter filter) {
		author(filter.getAuthor());
		search(filter.getSearchText());
	}
	
}
