package com.mydrawer.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mydrawer.db.DbDrawer;
import com.mydrawer.util.Security;

@WebServlet(name = "DrawerList",urlPatterns = {"/DrawerList/*"})

public class DrawerListWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(DrawerListWS.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		response.setContentType("application/json");

		PrintWriter out = response.getWriter();

		try {
			String pathInfo = request.getPathInfo();

			// Exclude the beginning / of the query param
			String encryptedCollectionName = pathInfo.substring(1, pathInfo.length());

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().encryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collection-name", decryptedCollectionName);

			String drawerJson = new DbDrawer().selectDrawerList(request, args);

			out.println(drawerJson);
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doGet(): ", e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		response.setContentType("application/json");

		PrintWriter out = response.getWriter();

		try {
			String inputJSON = request.getParameter("inputJSON");

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			String searchType = jo.get("searchType").toString();
			String encryptedCollectionName = jo.get("collectionName").toString();
			String searchTerm = jo.getString("searchTerm").toString();
			String traId = jo.getString("traId").toString();

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().encryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collection-name", decryptedCollectionName);

			String drawerJson = "";

			DbDrawer dbDrawer = new DbDrawer();

			if(searchType.equalsIgnoreCase("WILDCARD")) {

				// If an empty searchTerm is entered then get the member's drawer
				if(searchTerm == null || searchTerm.equals("")) {
					drawerJson = dbDrawer.selectDrawerList(request, args);

				} else {
					drawerJson = dbDrawer.selectDrawerListByWildcard(request, args);
				}
			} else {
				drawerJson = dbDrawer.selectDrawerListByTraId(request, args);
			}

			out.println(drawerJson);
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
	}

}
