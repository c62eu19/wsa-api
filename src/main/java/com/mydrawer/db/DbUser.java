package com.mydrawer.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mydrawer.util.DateUtility;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class DbUser {

	private static final Logger logger = Logger.getLogger(DbUser.class.getName());

	public HashMap<String,String> selectUserSignin(
		HttpServletRequest request, String argEmail, String argEncryptedPword) {

		MongoCursor<Document> cur = null;

		HashMap<String,String> hm = new HashMap<String,String>();

		int rowCount = 0;

		try {
			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), "col_users");

			cur = 
				collection.find(and(eq("email", argEmail), eq("password", argEncryptedPword))).iterator();

			while (cur.hasNext()) {

				rowCount++;

				Document document = cur.next();

				String id = (String)document.get("_id").toString();
				String email = (String)document.get("email");
				String password = (String)document.get("password");
				String name = (String)document.get("user-name");
				String collectionName = (String)document.get("collection-name");
				String registrationDt = (String)document.get("registration-date");
				String lastLoginDt = (String)document.get("last-login-date");
				String loginCnt = (String)document.get("login-count");
				String statusInd = (String)document.get("status-ind");

				if(name == null) name = "";

				hm.put("id",id);
				hm.put("email",email);
				hm.put("password",password);
				hm.put("user-name",name);
				hm.put("collection-name",collectionName);
				hm.put("registration-date",registrationDt);
				hm.put("last-Login-date",lastLoginDt);
				hm.put("login-count",loginCnt);
				hm.put("status-ind",statusInd);
			}
		}
		catch(Exception e) {
			hm.put("statusInd","E");
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectUserSignin(): ", e);
		}
		finally {
			cur.close();
		}

		if(rowCount <= 0) {
			hm.put("status-ind","E");
		}

		return hm;
	}

	public int selectUserCount(HttpServletRequest request, String email) {

		int rowCount = 0;

		try {
			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), "col_users");

			// Check if the email is already registered
			Document query = new Document();

			query.append("email", new Document("$regex", email).append("$options","i"));

			rowCount = (int) (long) collection.count(query);
		}
		catch(Exception e) {
			rowCount = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".selectUserCount(): ", e);
		}
		finally {}

		return rowCount;
	}

	public int insertUser(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), "col_users");
			
			Document doc = new Document();

			doc.append("email", args.get("email"));
			doc.append("password", args.get("password"));
			doc.append("user-name", args.get("user-name"));
			doc.append("collection-name", args.get("collection-name"));
			doc.append("registration-date", args.get("registration-date"));
			doc.append("last-login-date", args.get("last-login-date"));
			doc.append("login-count", args.get("login-count"));
			doc.append("status-ind", args.get("status-ind"));

			collection.insertOne(doc);
		}
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".insertUser(): ", e);
		}

		return statusCd;
	}

	public int updateUserSignin(HttpServletRequest request, String id, String loginCount) 
	{
		int statusCd = 0;

		try
		{
			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), "col_users");

			collection.updateOne(eq(
				"_id", new ObjectId(id)),
					new Document("$set", new Document("last-login-date", DateUtility.getCurrentDateTime())));

			// Increment the login count
			int newLoginCount = Integer.parseInt(loginCount) + 1;

			collection.updateOne(eq(
				"_id", new ObjectId(id)),
					new Document("$set", new Document("login-count", String.valueOf(newLoginCount))));
		}
		catch(Exception e)
		{
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".updateUserSignin(): ", e);
		}
		finally {}

		return statusCd;
	}

	public int updateUserPassword(
		HttpServletRequest request, String id, String encryptedPassword) {

		int statusCd = 0;

		try
		{
			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), "col_users");

			collection.replaceOne(eq(
				"_id", new ObjectId(id)),
					new Document("password", encryptedPassword));
		} 
		catch(Exception e)
		{
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".updateUserPassword(): ", e);
		}
		finally {}

		return statusCd;
	}

	public int updateUserStatusInd(
		HttpServletRequest request, String id, String statusInd) {

		int statusCd = 0;

		try
		{
			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), "col_users");

			collection.replaceOne(eq(
				"_id", new ObjectId(id)),
					new Document("status-ind", statusInd));
		} 
		catch(Exception e)
		{
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".updateUserStatusInd(): ", e);
		}
		finally {}

		return statusCd;
	}

}
