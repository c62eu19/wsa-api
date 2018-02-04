package com.mydrawer.db;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mydrawer.util.DateUtility;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class DbContactUs {

	private static final Logger logger = Logger.getLogger(DbContactUs.class.getName());

	public int insertContactUs(HttpServletRequest request, HashMap<String,String> args) {

		int statusCd = 0;

		try {
			MongoCollection<Document> collection = 
				DbMongo.getCollection(request.getServletContext(), "col_contact-us");

			Document doc = new Document();

			doc.append("email", args.get("email"));
			doc.append("subject", args.get("subject"));
			doc.append("message", args.get("message"));
			doc.append("inserted-date", args.get("inserted-date"));

			collection.insertOne(doc);
		}
		catch(Exception e) {
			statusCd = -1;
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".insertContactUs(): ", e);
		}

		return statusCd;
	}

}
