package com.ohs.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mongodb.MongoClient;
import com.ohs.db.DbMongo;

@WebListener
public class MongoListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(MongoListener.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		try {
			logger.log(Level.INFO, "MongoListener(): contextInitialized()");

			// Get a handle to the servlet context
			MongoClient mongoClient = DbMongo.createMongoClient();

			ServletContext ctx = sce.getServletContext();

			// Set the Mongo Client to the Servlet context
			ctx.setAttribute("MONGO-CLIENT", mongoClient);

			logger.log(Level.INFO, "MongoListener(): MongoClient created");

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
