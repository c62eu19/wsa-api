package com.story.db;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import com.story.util.DateUtility;

import static com.mongodb.client.model.Filters.eq;

public class DbStory {

	private static final Logger logger = Logger.getLogger(DbStory.class.getName());

	public ArrayList<HashMap<String,String>> selectGenreList(
		HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		ArrayList<HashMap<String,String>> list = new ArrayList<>();

		try {
			MongoCollection<Document> collection = 
				new DbMongo().getCollection(request.getServletContext(), "col_genre");

			cur = collection.find().sort(new Document("total_stories",-1)).iterator();

			while (cur.hasNext()) {

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String genre = (String)document.get("genre");
				String genreMnem = (String)document.get("genre_mnem");
				String image = (String)document.get("image");
				int totalStories = (Integer)document.get("total_stories");

				HashMap<String,String> hm = new HashMap<String,String>();

				hm.put("genreId", id);
				hm.put("genre", genre);
				hm.put("genreMnem", genreMnem);
				hm.put("image", image);
				hm.put("totalStories", Integer.toString(totalStories));

				list.add(hm);
			}
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectGenreList(): ", e);
		}
		finally {
			cur.close();
		}

		return list;
	}

	public int incrementStoryTotal(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			MongoCollection<Document> collection = 
					new DbMongo().getCollection(request.getServletContext(), "col_genre");

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("genreId"))),
					new Document("$inc", new Document("story_total", 1)));
		}
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".incrementStoryTotal(): ", e);
		}
		finally {}

		return statusCd;
	}

	public ArrayList<HashMap<String,String>> selectStoryList(
		HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		ArrayList<HashMap<String,String>> list = new ArrayList<>();

		try {
			MongoCollection<Document> collection = 
					new DbMongo().getCollection(request.getServletContext(), args.get("collectionName"));

			cur = collection.find().sort(new Document("created_date",-1)).iterator();

			list = processStoryList(cur);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectStoryList(): ", e);
		}
		finally {
			cur.close();
		}

		return list;
	}

	public ArrayList<HashMap<String,String>> selectStoryListByWildcard(
		HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		ArrayList<HashMap<String,String>> list = new ArrayList<>();

		try {
			String searchValue = args.get("searchTerm").toLowerCase();

			MongoCollection<Document> collection = 
				new DbMongo().getCollection(request.getServletContext(), args.get("collectionName"));

			Document titleSearch = new Document("$regex",searchValue).append("$options","i");

			Bson filter = Filters.eq("title", titleSearch);

			cur = collection.find(filter).sort(new Document("created_date",-1)).iterator();

			list = processStoryList(cur);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectStoryListByWildcard(): ", e);
		}
		finally {
			cur.close();
		}

		return list;
	}

	private ArrayList<HashMap<String,String>> processStoryList(MongoCursor<Document> cur) {

		ArrayList<HashMap<String,String>> list = new ArrayList<>();

		int rowCount = 0;

		try {
			while (cur.hasNext()) {

				rowCount++;

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String title = (String)document.get("title");
				String content = (String)document.get("content");
				String createdBy = (String)document.get("created_by");
				String createdDate = (String)document.get("created_date");
				String updatedDate = (String)document.get("updated_date");
				String originalStoryId = (String)document.get("original_story_id");
				String ancestorStoryIdList = (String)document.get("ancestor_story_id_list");
				String imageBase64 = (String)document.get("image_base64");
				int likes = (Integer)document.get("likes");
				String comments = (String)document.get("comments");

				HashMap<String,String> hm = new HashMap<String,String>();

				hm.put("storyId", id);
				hm.put("title", title);
				hm.put("content", content);
				hm.put("createdBy", createdBy);
				hm.put("createdDate", createdDate);
				hm.put("updatedDate", updatedDate);
				hm.put("originalStoryId", originalStoryId);
				hm.put("ancestorStoryIdList", ancestorStoryIdList);
				hm.put("imageBase64", imageBase64);
				hm.put("likes", Integer.toString(likes));
				hm.put("comments", comments);

				list.add(hm);
			}
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".processStoryList(): ", e);
		}
		finally {}

		return list;
	}

	public HashMap<String,String> selectStoryItem(HttpServletRequest request, HashMap<String,String> args) {

		MongoCursor<Document> cur = null;

		HashMap<String,String> hm = new HashMap<String,String>();

		int rowCount = 0;

		try {
			MongoCollection<Document> collection = 
				new DbMongo().getCollection(request.getServletContext(), args.get("collectionName"));

			Document query = 
				new Document("_id", new Document("$eq", new ObjectId(args.get("storyId"))));

			cur = collection.find(query).iterator();

			while (cur.hasNext()) {

				rowCount++;

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String title = (String)document.get("title");
				String content = (String)document.get("content");
				String createdBy = (String)document.get("created_by");
				String createdDate = (String)document.get("created_date");
				String updatedDate = (String)document.get("updated_date");
				String originalStoryId = (String)document.get("original_story_id");
				String ancestorStoryIdList = (String)document.get("ancestor_story_id_list");
				String imageBase64 = (String)document.get("image_base64");
				int likes = (Integer)document.get("likes");
				String comments = (String)document.get("comments");

				hm.put("storyId", id);
				hm.put("title", title);
				hm.put("content", content);
				hm.put("createdBy", createdBy);
				hm.put("createdDate", createdDate);
				hm.put("updatedDate", updatedDate);
				hm.put("originalStoryId", originalStoryId);
				hm.put("ancestorStoryIdList", ancestorStoryIdList);
				hm.put("imageBase64", imageBase64);
				hm.put("likes", Integer.toString(likes));
				hm.put("comments", comments);
			}
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectStoryItem(): ", e);
		}
		finally {
			cur.close();
		}

		return hm;
	}

	public int insertStoryItem(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			MongoCollection<Document> collection = 
				new DbMongo().getCollection(request.getServletContext(), args.get("collectionName"));

			Document doc = new Document();

			doc.append("title", args.get("title"));
			doc.append("content", args.get("content"));
			doc.append("created_by", args.get("createdBy"));
			doc.append("created_date", args.get("createdDate"));
			doc.append("updated_date", args.get("updatedDate"));
			doc.append("original_story_id", args.get("originalStoryId"));
			doc.append("ancestor_story_id_list", args.get("ancestorStoryIdList"));
			doc.append("image_base64", args.get("imageBase64"));
			doc.append("likes", Integer.parseInt(args.get("likes")));
			doc.append("comments", args.get("comments"));

			collection.insertOne(doc);
		} 
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".insertStoryItem(): ", e);
		}
		finally {}

		return statusCd;
	}

	public int updateStoryItem(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			MongoCollection<Document> collection = 
				new DbMongo().getCollection(request.getServletContext(), args.get("collectionName"));

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("storyId"))),
					new Document("$set", new Document("title", args.get("title"))));

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("storyId"))),
					new Document("$set", new Document("content", args.get("content"))));

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("storyId"))),
					new Document("$set", new Document("comments", args.get("comments"))));

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("storyId"))),
					new Document("$set", new Document("updated_date", DateUtility.getCurrentDateTime())));
		}
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".updateStoryItem(): ", e);
		}
		finally {}

		return statusCd;
	}

	public int deleteStoryItem(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			MongoCollection<Document> collection = 
				new DbMongo().getCollection(request.getServletContext(), args.get("collectionName"));

			collection.deleteOne(eq("_id", new ObjectId(args.get("storyId"))));
		}
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".deleteStoryItem(): ", e);
		}
		finally {}

		return statusCd;
	}

	public int incrementStoryLikeTotal(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			MongoCollection<Document> collection = 
				new DbMongo().getCollection(request.getServletContext(), args.get("collectionName"));

			collection.updateOne(eq(
				"_id", new ObjectId(args.get("storyId"))),
					new Document("$inc", new Document("likes", 1)));
		}
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".incrementStoryLikeTotal(): ", e);
		}
		finally {}

		return statusCd;
	}

}
