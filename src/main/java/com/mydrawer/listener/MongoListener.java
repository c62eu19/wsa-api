package com.mydrawer.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mydrawer.db.DbMongo;
import com.mydrawer.webservice.SignUpWS;

@WebListener
public class MongoListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(MongoListener.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		try {
			logger.log(Level.INFO, "MongoListener(): contextInitialized()");

			// Get a handle to the servlet context
			ServletContext ctx = sce.getServletContext();

			/*
			 * This method creates a: 
			 * 		MongoClient
			 * 		db_drawers database
			 * 		col_users collection
			 */
			DbMongo.getCollection(ctx, "col_users");
			logger.log(Level.INFO, "MongoListener(): MongoClient created");
			logger.log(Level.INFO, "MongoListener(): MongoDatabase: db_drawers created");
			logger.log(Level.INFO, "MongoListener(): Mongo Collection: col_users created");

			logger.log(Level.INFO, "MongoClient initialized()");
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".contextInitializer(): ", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

		try {
			logger.log(Level.INFO, "MongoListener(): contextDestroyed()");

			// Get a handle to the servlet context
			ServletContext ctx = sce.getServletContext();

			MongoClient mongoClient = (MongoClient) ctx.getAttribute("MONGO-CLIENT");

			if(mongoClient != null) {
				mongoClient.close();
			}

			logger.log(Level.INFO, "MongoClient closed");
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".contextDestroyed(): ", e);
		}
	}
}
