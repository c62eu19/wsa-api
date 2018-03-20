package com.ohs.util;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;

import static com.mongodb.client.model.Filters.*;

public class MongoTest
{
	private int getDocumentCount(MongoCollection<Document> col, String field, String value) {

		int count = 0;

		try {
//			Document query = new Document("name", new Document("$eq", name));
			Document query = new Document();

			String exactMatchValue = "^" + value + "$";

			query.append(field, new Document("$regex", exactMatchValue).append("$options","i"));

			count = (int) (long) col.count(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}

		return count;
	}

	private Document getDocumentById(MongoCollection<Document> col, String id) {

		Document document = null;

		try {
			MongoCursor<Document> cur = null;

			Document query = new Document("_id", new Document("$eq", new ObjectId(id)));
			cur = col.find(query).iterator();

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

				System.out.println(document.toJson());
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

	private void listDocumentsByQuery(MongoCollection<Document> col) {

		MongoCursor<Document> cur = null;

		try {
			cur = 
					col.find(and(eq("name", new Document("$regex","^stan zajdel$").append("$options","i")), 
					eq("role",new Document("$regex","^kin$").append("$options","i")))).iterator();

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

	public boolean isValidEmail(String email) {

		String emailPattern = 
			"^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

		Pattern p = Pattern.compile(emailPattern);

		Matcher m = p.matcher(email);

		return m.matches();
	}

	public static void main(String[] args) throws UnknownHostException {

		MongoTest obj = new MongoTest();

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
		obj.createDocument(col, "Stan Zajdel", "king");
		obj.createDocument(col, "Diane Zajdel", "homemaker");
		obj.createDocument(col, "Alyssa Zajdel", "daughter");
		obj.createDocument(col, "Ryan Zajdel", "son");
		obj.createDocument(col, "james zajdel", "son");
		obj.createDocument(col, "ryan zajdel", "son");

		// Update a document
//		obj.updateDocument(col, "5a6682d9d3fd231478abe339");

		// Delete a document
//		obj.deleteDocument(col, "5a66a4a2d3fd231f382c6129");

		// List the entire collection
		obj.listDocuments(col);

		obj.listDocumentsByQuery(col);

 String email = "Stan.Zajdel@ibx.com";
 if(obj.isValidEmail(email)) {
	 System.out.println("VALID");
 } else {
	 System.out.println("NOT VALID");
 }
 
 String db = email.replaceAll("[^a-zA-Z0-9]", "");
 System.out.println("col_" + db.toLowerCase().trim());
 
 System.out.println(LocalDateTime.now());
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 String formatDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
 
		// Close the Mongo Client
		mongo.close();
	}
}
