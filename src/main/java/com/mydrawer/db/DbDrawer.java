package com.mydrawer.db;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mydrawer.util.DateUtility;

import static com.mongodb.client.model.Filters.eq;

public class DbDrawer {

	private static final Logger logger = Logger.getLogger(DbDrawer.class.getName());

	public ArrayList<HashMap<String,String>> selectDrawerList(
		HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		ArrayList<HashMap<String,String>> list = new ArrayList<>();

		try {
			String drawerName = DbMongo.getDrawerCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), drawerName);

			cur = collection.find().sort(new Document("updated_date",-1)).iterator();

			HashMap<String,String> hmTrays = this.selectTrayList(request, args);

			list = processDrawerList(cur, hmTrays);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectDrawerList(): ", e);
		}
		finally {
			cur.close();
		}

		return list;
	}

	public ArrayList<HashMap<String,String>> selectDrawerListByTrayId(
		HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		ArrayList<HashMap<String,String>> list = new ArrayList<>();

		try {
			String trayId = args.get("trayId");

			String drawerName = DbMongo.getDrawerCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), drawerName);

			cur = collection.find(eq("tray_id", trayId)).sort(new Document("updated_date",-1)).iterator();

			HashMap<String,String> hmTrays = this.selectTrayList(request, args);

			list = processDrawerList(cur, hmTrays);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectDrawerListByTrayId(): ", e);
		}
		finally {
			cur.close();
		}

		return list;
	}

	public ArrayList<HashMap<String,String>> selectDrawerListByWildcard(
		HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		ArrayList<HashMap<String,String>> list = new ArrayList<>();

		try {
			String searchValue = args.get("searchTerm").toLowerCase();

			String drawerName = DbMongo.getDrawerCollectionName(args.get("collectionName"));

			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), drawerName);

			Document titleSearch = new Document("$regex",searchValue).append("$options","i");
			Document textSearch = new Document("$regex",searchValue).append("$options","i");

			Bson filter = Filters.or(Filters.eq("title", titleSearch), Filters.eq("text", textSearch));

			cur = collection.find(filter).sort(new Document("updated_date",-1)).iterator();

			HashMap<String,String> hmTrays = this.selectTrayList(request, args);

			list = processDrawerList(cur, hmTrays);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectDrawerListByWildcard(): ", e);
		}
		finally {
			cur.close();
		}

		return list;
	}

	private HashMap<String,String> selectTrayList(
		HttpServletRequest request, HashMap<String,String> args) {

		HashMap<String,String> hm = new HashMap<>();

		try {
			ArrayList<HashMap<String,String>> list = 
				new DbTray().selectTrayList(request, args);

			for(HashMap<String,String> map : list) {
				hm.put(map.get("trayId"), map.get("trayName"));
			}
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectTrayList(): ", e);
		}
		finally {}

		return hm;
	}

	private ArrayList<HashMap<String,String>> processDrawerList(
		MongoCursor<Document> cur, HashMap<String,String> hmTrays) {

		ArrayList<HashMap<String,String>> list = new ArrayList<>();

		int rowCount = 0;

		try {
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

				String trayName = hmTrays.get(trayId);

				HashMap<String,String> hm = new HashMap<String,String>();

				hm.put("drawerId", id);
				hm.put("trayId", trayId);
				hm.put("trayName", trayName);
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
				Level.SEVERE, this.getClass().getName() + ".processDrawerList(): ", e);
		}
		finally {}

		return list;
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

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("drawerId"))),
					new Document("$set", new Document("tray_id", args.get("trayId"))));

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("drawerId"))),
					new Document("$set", new Document("title", args.get("title"))));

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("drawerId"))),
					new Document("$set", new Document("text", args.get("text"))));

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("drawerId"))),
					new Document("$set", new Document("url", args.get("url"))));

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("drawerId"))),
					new Document("$set", new Document("updated_date", DateUtility.getCurrentDateTime())));
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
