/**
 * 
 */
package com.dds.plugin.storage.mongodb;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dds.exception.UnsupportedException;
import com.dds.interfaces.APIInterface;
import com.dds.utils.Property;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author ravid
 * 
 */
public class MongoDB extends Mongo implements APIInterface {

	DB db = null;
	DBCollection coll = null;
	Logger logger = Logger.getLogger(MongoDB.class);

	private String dbName = null;
	private String collection = null;

	public MongoDB() throws UnknownHostException {
		Map<String, String> props = Property.getProperty().getDatabaseProperties();
		setDbName(props.get("mongo_dbName"));
		setCollection(props.get("mongo_collection"));
	}

	/**
	 * @param dbName
	 *            the dbName to set
	 */
	private void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @param collection
	 *            the collection to set
	 */
	private void setCollection(String collection) {
		this.collection = collection;
	}

	/**
	 * Create connection to MongoDB
	 * 
	 * @see com.dds.interfaces.APIInterface#createConnection()
	 */
	public void createConnection() {
		try {
			db = this.getDB(dbName);
			coll = db.getCollection(collection);

		} catch (MongoException e) {
			e.printStackTrace();
		}
		logger.info("MongoDB connection created");
	}

	/**
	 * Insert a key and value
	 * 
	 * @see com.dds.interfaces.APIInterface#put(java.lang.String, java.lang.String)
	 */
	public void put(String key, String value) {
		BasicDBObject doc = new BasicDBObject();

		doc.put("key", key);
		doc.put("value", value);

		if (!contains(key)) {
			coll.insert(doc);
		} else {
			delete(key);
			coll.insert(doc);
		}
		logger.info(key + " : " + value + " inserted into DB");
	}

	/**
	 * Delete a particular key
	 * 
	 * @see com.dds.interfaces.APIInterface#delete(java.lang.String)
	 */
	public void delete(String key) {
		BasicDBObject lookUp = new BasicDBObject();
		lookUp.put("key", key);

		coll.remove(lookUp);
		logger.info(key + " deleted from DB");
	}

	/**
	 * Used to retrieve a value for the associated key
	 * 
	 * @see com.dds.interfaces.APIInterface#get(java.lang.String)
	 */
	public String get(String key) {
		BasicDBObject lookUp = new BasicDBObject();
		lookUp.put("key", key);

		DBCursor cursor = coll.find(lookUp);
		List<DBObject> list = cursor.toArray();

		String value = null;
		for (DBObject obj : list) {
			value = obj.get("value").toString();
		}
		logger.info(key + " : " + value + " retrieved from DB");
		return value;
	}

	/**
	 * Display all the keys in the Collection
	 * TODO Incomplete
	 */
	public void showAll() {
		DBCursor cur = coll.find();

		while (cur.hasNext()) {
			System.out.println(cur.next());
		}
	}

	/**
	 * Delete all keys by dropping the collection
	 */
	public void deleteAll() {
		coll.drop();
	}

	/**
	 * Return true if key is present else false
	 * 
	 * @see com.dds.interfaces.APIInterface#contains(java.lang.String)
	 */
	public Boolean contains(String key) {
		BasicDBObject lookUp = new BasicDBObject();
		lookUp.put("key", key);

		DBCursor cursor = coll.find(lookUp);
		if (cursor.count() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Close the connections, setting all to none No in-built close function for
	 * MongoDB
	 * 
	 * @see com.dds.interfaces.APIInterface#closeConnection()
	 */
	public void closeConnection() {

		db = null;
		coll = null;
		logger.info("MongoDB connection closed");
	}
	
	public void createConnection(String bootstrapUrl) throws Exception {
		throw new UnsupportedException("Unsupported method");
	}
	
	public Object nativeAPI(String... args) throws Exception {
		throw new UnsupportedException("Unsupported method");
	}

	@Override
	public void replicate(String key, String value, int factor)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
