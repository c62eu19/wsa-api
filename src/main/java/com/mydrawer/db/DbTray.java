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
import com.mydrawer.obj.*;

import static com.mongodb.client.model.Filters.eq;

public class DbTray {

	private static final Logger logger = Logger.getLogger(DbUser.class.getName());

	public ArrayList<HashMap> selectTrayList(HttpServletRequest request, HashMap<String,String> args) {

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
				String name = (String)document.get("tray-name");

				HashMap<String,String> hm = new HashMap<String,String>();

				hm.put("id", id);
				hm.put("tray-name", name);

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
//		JSONArray joPayload = new JSONArray(hmList);
//		listJson = joPayload.toString();

		return list;
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
				new Document("_id", new Document("$eq", new ObjectId(args.get("id"))));

			cur = collection.find(query).iterator();

			while (cur.hasNext()) {

				rowCount++;

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String name = (String)document.get("tray-name");

				hm.put("id", id);
				hm.put("tray-name", name);
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

			doc.append("tray-name", args.get("tray-name"));

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
				"_id", new ObjectId(args.get("id"))),
					new Document("tray-name", args.get("tray-name")));
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

			collection.deleteOne(eq("_id", new ObjectId(args.get("id"))));
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
