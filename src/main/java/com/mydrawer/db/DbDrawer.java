package com.mydrawer.db;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import static com.mongodb.client.model.Filters.eq;

public class DbDrawer {

	private static final Logger logger = Logger.getLogger(DbDrawer.class.getName());

	public String selectDrawerList(HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		int rowCount = 0;

		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

		try {
			String drawerName = DbMongo.getDrawerCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), drawerName);

			cur = collection.find().iterator();

			while (cur.hasNext()) {

				rowCount++;

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String trayId = (String)document.get("tray_id");
				String insertedDate = (String)document.get("inserted_date");
				String updatedDate = (String)document.get("updated_date");
				String type = (String)document.get("type");
				String title = (String)document.get("title");
				String text = (String)document.get("text");
				String url = (String)document.get("url");

				HashMap<String,String> hm = new HashMap<String,String>();

				hm.put("drawerId", id);
				hm.put("trayId", trayId);
				hm.put("insertedDate", insertedDate);
				hm.put("updatedDate", updatedDate);
				hm.put("type", type);
				hm.put("title", title);
				hm.put("text", text);
				hm.put("url", url);

				list.add(hm);
			}
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectDrawerList(): ", e);
		}
		finally {
			cur.close();
		}

		// Convert the hashmap to a JSON string
		JSONArray joPayload = new JSONArray(list);

		String listJson = joPayload.toString();

		return listJson;
	}

	public String selectDrawerListByTraId(
		HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		int rowCount = 0;

		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

		try {
			String drawerName = DbMongo.getDrawerCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), drawerName);

			cur = collection.find().iterator();

			while (cur.hasNext()) {

				rowCount++;

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String trayId = (String)document.get("tray_id");
				String insertedDate = (String)document.get("inserted_date");
				String updatedDate = (String)document.get("updated_date");
				String type = (String)document.get("type");
				String title = (String)document.get("title");
				String text = (String)document.get("text");
				String url = (String)document.get("url");

				HashMap<String,String> hm = new HashMap<String,String>();

				hm.put("drawerId", id);
				hm.put("trayId", trayId);
				hm.put("insertedDate", insertedDate);
				hm.put("updatedDate", updatedDate);
				hm.put("type", type);
				hm.put("title", title);
				hm.put("text", text);
				hm.put("url", url);

				list.add(hm);
			}
		}
		catch(Exception e)
		{
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectDrawerListByTraId(): ", e);
		}
		finally {
			cur.close();
		}

		// Convert the hashmap to a JSON string
		JSONArray joPayload = new JSONArray(list);

		String listJson = joPayload.toString();

		return listJson;
	}

	public String selectDrawerListByWildcard(
		HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		int rowCount = 0;

		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

		try {
			String searchTerm = "%" + args.get("searchTerm").toLowerCase() + "%";

			String drawerName = DbMongo.getDrawerCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), drawerName);

			cur = collection.find().iterator();

			while (cur.hasNext()) {

				rowCount++;

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String trayId = (String)document.get("tray_id");
				String insertedDate = (String)document.get("inserted_date");
				String updatedDate = (String)document.get("updated_date");
				String type = (String)document.get("type");
				String title = (String)document.get("title");
				String text = (String)document.get("text");
				String url = (String)document.get("url");

				HashMap<String,String> hm = new HashMap<String,String>();

				hm.put("drawerId", id);
				hm.put("trayId", trayId);
				hm.put("insertedDate", insertedDate);
				hm.put("updatedDate", updatedDate);
				hm.put("type", type);
				hm.put("title", title);
				hm.put("text", text);
				hm.put("url", url);

				list.add(hm);
			}
		}
		catch(Exception e)
		{
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectDrawerListByWildcard(): ", e);
		}
		finally {
			cur.close();
		}

		// Convert the hashmap to a JSON string
		JSONArray joPayload = new JSONArray(list);

		String listJson = joPayload.toString();

		return listJson;
	}

	public HashMap<String,String> selectDrawer(HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		HashMap<String,String> hm = new HashMap<String,String>();

		int rowCount = 0;

		try {
			String drawerName = DbMongo.getDrawerCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), drawerName);

			Document query = 
				new Document("_id", new Document("$eq", new ObjectId(args.get("drawerId"))));

			cur = collection.find(query).iterator();

			while (cur.hasNext()) {

				rowCount++;

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String trayId = (String)document.get("tray_id");
				String insertedDate = (String)document.get("inserted_date");
				String updatedDate = (String)document.get("updated_date");
				String type = (String)document.get("type");
				String title = (String)document.get("title");
				String text = (String)document.get("text");
				String url = (String)document.get("url");

				hm.put("drawerId", id);
				hm.put("trayId", trayId);
				hm.put("insertedDate", insertedDate);
				hm.put("updatedDate", updatedDate);
				hm.put("type", type);
				hm.put("title", title);
				hm.put("text", text);
				hm.put("url", url);
			}
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectDrawer(): ", e);
		}
		finally {
			cur.close();
		}

		return hm;
	}

	public int insertDrawer(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			String drawerName = DbMongo.getDrawerCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), drawerName);

			Document doc = new Document();

			doc.append("tray_id", args.get("trayId"));
			doc.append("inserted_date", args.get("insertedDate"));
			doc.append("updated_date", args.get("updatedDate"));
			doc.append("type", args.get("type"));
			doc.append("title", args.get("title"));
			doc.append("text", args.get("text"));
			doc.append("url", args.get("url"));

			collection.insertOne(doc);
		} 
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".insertDrawer(): ", e);
		}
		finally {}

		return statusCd;
	}

	public int updateDrawer(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			String drawerName = DbMongo.getDrawerCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), drawerName);

			collection.replaceOne(
				eq("_id", new ObjectId(args.get("drawerId"))),
					new Document("tray-id", args.get("trayId"))
						.append("title", args.get("title"))
						.append("text", args.get("text"))
						.append("url", args.get("url")));
		} 
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".updateDrawer(): ", e);
		}
		finally {}

		return statusCd;
	}

	public int deleteDrawer(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			String drawerName = DbMongo.getDrawerCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), drawerName);

			collection.deleteOne(eq("_id", new ObjectId(args.get("drawerId"))));
		}
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".deleteDrawer(): ", e);
		}
		finally {}

		return statusCd;
	}

}
