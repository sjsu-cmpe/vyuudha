/**
 * 
 */
package com.dds.storage;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author ravid
 * 
 */
public class MongoDB {

	Mongo m = null;
	DB db = null;

	private String dbName = null;
	private String collection = null;
	
	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	public void createConnection()
	{
		try {
			m = new Mongo();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db = m.getDB( dbName);
	}
	
	public void show()
	{
		Set<String> colls = db.getCollectionNames();

		for (String s : colls) {
		    System.out.println(s);
		}
	}
	
	public void insert(String key, String value)
	{
		DBCollection coll = db.getCollection(collection);
        BasicDBObject doc = new BasicDBObject();

        doc.put("key", key);
        doc.put("value", value);

        coll.insert(doc);
	}
	
	public void remove(String key)
	{
		DBCollection coll = db.getCollection(collection);
		DBObject myDoc = coll.findOne();
		coll.remove(myDoc);
		System.out.println("Removed");
	}
	
	public void get(String key)
	{
		DBCollection coll = db.getCollection(collection);
		DBObject myDoc = coll.findOne();
		System.out.println(myDoc);
	}
}
