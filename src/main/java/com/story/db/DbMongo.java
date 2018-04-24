package com.story.db;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DbMongo
{
	private static final Logger logger = Logger.getLogger(DbMongo.class.getName());

	public MongoClient createMongoClient() {

		MongoClient mongoClient = null;
		
		try {
			// Create the Mongo Db Client
			mongoClient = new MongoClient("localhost",27017);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".createMongoClient(): ", e);
		}

		return mongoClient;
	}

	private MongoClient getMongoClient(ServletContext ctx) {

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
				Level.SEVERE, this.getClass().getName() + ".getMongoClient(): ", e);
		}

		return mongoClient;
	}

	public MongoCollection<Document> getCollection(
		ServletContext ctx, String collectionName) {

		MongoCollection<Document> collection = null;

		try {
			// Check if we have a valid Mongo Client in the ServletContext
			MongoClient mongoClient = getMongoClient(ctx);

			MongoDatabase database = mongoClient.getDatabase("db_stories");

			// Get the collection. It automatically gets created if it doesn't exist
			collection = database.getCollection(collectionName);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".getCollection(): ", e);
		}

		return collection;
	}

}
