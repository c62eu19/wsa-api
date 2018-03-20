package com.ohs.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.ohs.db.DbDrawer;
import com.ohs.util.Security;

@WebServlet(name = "DrawerList",urlPatterns = {"/DrawerList/*"})

public class DrawerListWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(DrawerListWS.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		response.setContentType("application/json");

		WSHelper wsHelper = new WSHelper();

		PrintWriter out = response.getWriter();

		try {
			String pathInfo = request.getPathInfo();

			// Exclude the beginning / of the query param
			String encryptedCollectionName = pathInfo.substring(1, pathInfo.length());

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().decryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", decryptedCollectionName);

			ArrayList<HashMap<String,String>> drawerList = 
				new DbDrawer().selectDrawerList(request, args);
			String drawerJson = wsHelper.convertPayloadToJson(drawerList);

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

		WSHelper wsHelper = new WSHelper();

		PrintWriter out = response.getWriter();

		try {
			String inputJSON = request.getParameter("inputJSON");

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			String searchType = jo.get("searchType").toString();
			String encryptedCollectionName = jo.get("collectionName").toString();
			String searchTerm = jo.getString("searchTerm").toString();
			String trayId = jo.getString("trayId").toString();

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().decryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", decryptedCollectionName);
			args.put("searchTerm", searchTerm);
			args.put("trayId", trayId);

			String drawerJson = "";

			DbDrawer dbDrawer = new DbDrawer();

			ArrayList<HashMap<String,String>> drawerList = new ArrayList<>();

			if(searchType.equalsIgnoreCase("WILDCARD")) {

				// If an empty searchTerm is entered then get the member's drawer
				if(searchTerm == null || searchTerm.equals("")) {
					drawerList = new DbDrawer().selectDrawerList(request, args);

				} else {
					drawerList = dbDrawer.selectDrawerListByWildcard(request, args);
				}
			} else {
				drawerList = dbDrawer.selectDrawerListByTrayId(request, args);
			}

			drawerJson = wsHelper.convertPayloadToJson(drawerList);

			out.println(drawerJson);
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
	}

}
