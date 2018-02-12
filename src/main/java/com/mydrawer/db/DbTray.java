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

public class DbTray {

	private static final Logger logger = Logger.getLogger(DbTray.class.getName());

	public String selectTrayList(HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		int rowCount = 0;

		ArrayList<HashMap> list = new ArrayList<HashMap>();

		try
		{
			String trayName = DbMongo.getTrayCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), trayName);

	        cur = collection.find().iterator();

			while (cur.hasNext()) {

				rowCount++;

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String name = (String)document.get("tray_name");

				HashMap<String,String> hm = new HashMap<String,String>();

				hm.put("trayId", id);
				hm.put("trayName", name);

				list.add(hm);
			}
		}
		catch(Exception e)
		{
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectTrayList(): ", e);
		}
		finally
		{
			cur.close();
		}

		// Convert the hashmap to a JSON string
		JSONArray joPayload = new JSONArray(list);

		String listJson = joPayload.toString();

		return listJson;
	}

	public HashMap<String,String> selectTray(HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		HashMap<String,String> hm = new HashMap<String,String>();

		int rowCount = 0;

		try
		{
			String trayName = DbMongo.getTrayCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), trayName);

			Document query = 
				new Document("_id", new Document("$eq", new ObjectId(args.get("trayId"))));

			cur = collection.find(query).iterator();

			while (cur.hasNext()) {

				rowCount++;

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String name = (String)document.get("tray_name");

				hm.put("trayId", id);
				hm.put("trayName", name);
			}
		}
		catch(Exception e)
		{
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectTray(): ", e);
		}
		finally {
			cur.close();
		}

		return hm;
	}

	public int insertTray(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try
		{
			String trayName = DbMongo.getTrayCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), trayName);

			Document doc = new Document();

			doc.append("tray_name", args.get("trayName"));

			collection.insertOne(doc);
		} 
		catch(Exception e)
		{
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".insertTray(): ", e);
		}
		finally {}

		return statusCd;
	}

	public int updateTray(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			String trayName = DbMongo.getTrayCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), trayName);

			collection.replaceOne(eq(
				"_id", new ObjectId(args.get("trayId"))),
					new Document("tray_name", args.get("trayName")));
		}
		catch(Exception e)
		{
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".updateTray(): ", e);
		}
		finally {}
			return statusCd;
	}

	public int deleteTray(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			String trayName = DbMongo.getTrayCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), trayName);

			collection.deleteOne(eq("_id", new ObjectId(args.get("trayId"))));
		} 
		catch(Exception e)
		{
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".deleteTray(): ", e);
		}
		finally {}

		return statusCd;
	}

}
