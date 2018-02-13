package com.mydrawer.db;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class DbMongo
{
	private static final Logger logger = Logger.getLogger(DbMongo.class.getName());

	public static MongoClient createMongoClient() {

		MongoClient mongoClient = null;

		
		try {
			// Create the Mongo Db Client
			mongoClient = new MongoClient("localhost",27017);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, "DbMongo.createMongoClient(): ", e);
		}

		return mongoClient;
	}

	private static MongoClient getMongoClient(ServletContext ctx) {

		MongoClient mongoClient = null;

		
		try {
			// Check if we have a valid Mongo Client in the ServletContext
			mongoClient = (MongoClient) ctx.getAttribute("MONGO-CLIENT");

			if(mongoClient == null) {

				// Create the Mongo Db Client
				mongoClient = new MongoClient("localhost",27017);

				// Set the Mongo Client to the Servlet context
				ctx.setAttribute("MONGO-CLIENT", mongoClient);
			}
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, "DbMongo.getMongoClient(): ", e);
		}

		return mongoClient;
	}

	public static int createCollection(
		ServletContext ctx, String collectionName) {

		int statusCd = 0;

		try {
			// Check if we have a valid Mongo Client in the ServletContext
			MongoClient mongoClient = getMongoClient(ctx);

			MongoDatabase database = mongoClient.getDatabase("db_drawers");

			// Get the collection. It automatically gets created if it doesn't exist
			database.createCollection(collectionName);
		}
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, "DbMongo.getCollection(): ", e);
		}

		return statusCd;
	}

	public static MongoCollection<Document> getCollection(
		ServletContext ctx, String collectionName) {

		MongoCollection<Document> collection = null;

		try {
			// Check if we have a valid Mongo Client in the ServletContext
			MongoClient mongoClient = getMongoClient(ctx);

			MongoDatabase database = mongoClient.getDatabase("db_drawers");

			// Get the collection. It automatically gets created if it doesn't exist
			collection = database.getCollection(collectionName);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, "DbMongo.getCollection(): ", e);
		}

		return collection;
	}

	public static String getTrayCollectionName(String collectionName) {

		String trayName = "";

		try {
			// Derive the user's tray collection name from the collection name
			trayName = "col_" + collectionName + "_tray";
		}
		catch(Exception e) {
			trayName = "";
			logger.log(
				Level.SEVERE, "DbMongo.getTrayCollectionName(): ", e);
		}
		finally {}

		return trayName;
	}

	public static String getDrawerCollectionName(String collectionName) {

		String drawerName = "";

		try {
			// Derive the user's drawer collection name from the collection name
			drawerName = "col_" + collectionName + "_drawer";
		}
		catch(Exception e) {
			drawerName = "";
			logger.log(
				Level.SEVERE, "DbMongo.getDrawerCollectionName(): ", e);
		}
		finally {}

		return drawerName;
	}

	public static String getInboxCollectionName(String collectionName) {

		String inboxName = "";

		try {
			// Derive the user's drawer collection name from the collection name
			inboxName = "col_" + collectionName + "_inbox";
		}
		catch(Exception e) {
			inboxName = "";
			logger.log(
				Level.SEVERE, "DbMongo.getInboxCollectionName(): ", e);
		}
		finally {}

		return inboxName;
	}

	public int getDocumentCount(
		MongoCollection<Document> collection, String field, String value) {

		int count = 0;

		try {
			Document query = new Document();

			query.append(field, new Document("$regex", value).append("$options","i"));

			count = (int) (long) collection.count(query);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".getDocumentCount(): ", e);
		}

		return count;
	}

	private Document getDocumentById(MongoCollection<Document> collection, String id) {

		Document document = null;

		try {
			MongoCursor<Document> cur = null;

			Document query = new Document("_id", new Document("$eq", new ObjectId(id)));
			cur = collection.find(query).iterator();

			while (cur.hasNext()) {

				document = cur.next();
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}

		return document;
	}

	public Document getDocumentByField(
		MongoCollection<Document> collection, String field, String value) {

		Document document = null;

		try {
			MongoCursor<Document> cur = null;

			String matchValue = "^" + value + "$";

			cur = 
				collection.find(eq(field, new Document("$regex",matchValue).append("$options","i"))).iterator();

 			while (cur.hasNext()) {

				document = cur.next();
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}

		return document;
	}

	private void createDocument(MongoCollection<Document> col, String name, String role) {

		try {
			int count = getDocumentCount(col, "name", name);

			if(count <= 0) {
				Document d1 = new Document();

				d1.append("name", name);
				d1.append("role", role);

				col.insertOne(d1);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}


	private void updateDocument(MongoCollection<Document> col, String id) {

		try {
			col.replaceOne(eq(
				"_id", new ObjectId(id)),
					new Document("name", "Diane Zajdel")
	                        .append("role", "wife"));
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	private void deleteDocument(MongoCollection<Document> col, String id) {

		try {
			col.deleteOne(eq("_id", new ObjectId(id)));
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	private void listDocuments(MongoCollection<Document> col) {

		MongoCursor<Document> cur = null;

		try {
			cur = col.find().iterator();

			while (cur.hasNext()) {

				Document document = cur.next();

				String _id = (String)document.get("_id").toString();
				String name = (String)document.get("name");
				String role = (String)document.get("role");

				System.out.println("(" + _id + ") " + name + " --> " + role);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		finally {
			cur.close();
		}
	}

	private void dropCollection(MongoCollection<Document> col) {

		try {
			// Drop the collection
			col.drop();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public int insertTray(
		MongoCollection<Document> collection, String trayName) {

		int statusCd = 0;

		try {
			Document doc = new Document();

			doc.append("tray-name", trayName);

			collection.insertOne(doc);
		}
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".insertTray(): ", e);
		}
			return statusCd;
	}

	public int insertDrawer(
		MongoCollection<Document> collection, String trayName) {

		int statusCd = 0;

		try {
			Document doc = new Document();

			doc.append("tray-name", trayName);
			doc.append("inserted-date", trayName);
			doc.append("updated-date", trayName);
			doc.append("type", trayName);
			doc.append("title", trayName);
			doc.append("text", trayName);
			doc.append("url", trayName);

			collection.insertOne(doc);
		}
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".insertDrawer(): ", e);
		}
			return statusCd;
	}

	public static void main(String[] args) throws UnknownHostException {

		DbMongo obj = new DbMongo();

		MongoClient mongo = new MongoClient("localhost", 27017);

		MongoDatabase database = mongo.getDatabase("db_drawers");

		// Get the collection.  It automatically gets created if it doesn't exist
		MongoCollection<Document> col = database.getCollection("col_users");

		// Drop the collection
//		obj.dropCollection(col);

		// Retrieve a document by the internal doc Id
//		Document doc = obj.getDocumentById(col, "5a6682d9d3fd231478abe39");
//		System.out.println(doc.get("name"));

		// Add data to a collection
/*		obj.createDocument(col, "Stan Zajdel", "king");
		obj.createDocument(col, "Diane Zajdel", "homemaker");
		obj.createDocument(col, "Alyssa Zajdel", "daughter");
		obj.createDocument(col, "Ryan Zajdel", "son");
		obj.createDocument(col, "james zajdel", "son");
		obj.createDocument(col, "ryan zajdel", "son");

		// Update a document
		obj.updateDocument(col, "5a6682d9d3fd231478abe339");
*/
		// Delete a document
//		obj.deleteDocument(col, "5a66a4a2d3fd231f382c6129");

		// List the entire collection
		obj.listDocuments(col);

		// Close the Mongo Client
		mongo.close();
	}
}
