package com.mydrawer.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

@WebServlet(name = "Drawer Entry",urlPatterns = {"/DrawerEntry/*"})

public class DrawerEntryWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(DrawerEntryWS.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		response.setContentType("application/json");

		PrintWriter out = response.getWriter();

		try {
			String inputJSON = request.getParameter("inputJSON");

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			// Token that identifies a member
			String encryptedCollectionName = jo.get("collectionName").toString();
			String traId = jo.get("traId").toString();
			String title = jo.get("title").toString();
			String text = jo.get("text").toString();
			String url = jo.get("url").toString();
			String type = jo.get("type").toString();

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().encryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collection-name", decryptedCollectionName);
			args.put("tray-id", traId);
			args.put("title", title);
			args.put("text", text);
			args.put("url", url);
			args.put("type", type);

			DbDrawer dbDrawer = new DbDrawer();

			int statusCd = dbDrawer.insertDrawer(request, args);

			String drawerJson = dbDrawer.selectDrawerList(request, args);

			out.println(drawerJson);
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		response.setContentType("application/json");

		BufferedReader br = null;
		InputStreamReader isr = null;

		PrintWriter out = response.getWriter();

		try {
			isr = new InputStreamReader(request.getInputStream());
	        br = new BufferedReader(isr);

	        String inputJSON = br.readLine();

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			// Token that identifies a member
			String encryptedCollectionName = jo.get("collectionName").toString();
			String drwId = jo.get("drwSk").toString();
			String traId = jo.get("traSk").toString();
			String url = jo.get("url").toString();
			String title = jo.get("title").toString();
			String text = jo.get("text").toString();

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().encryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collection-name", decryptedCollectionName);
			args.put("drawer-id", drwId);
			args.put("tray-id", traId);
			args.put("title", title);
			args.put("text", text);
			args.put("url", url);

			DbDrawer dbDrawer = new DbDrawer();

			int statusCd = dbDrawer.updateDrawer(request, args);

			String drawerJson = dbDrawer.selectDrawerList(request, args);

			out.println(drawerJson);
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPut(): ", e);
		}
		finally {
			if(isr != null) {
				isr.close();
			}

			if(br != null) {
				br.close();
			}
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		response.setContentType("application/json");

		BufferedReader br = null;
		InputStreamReader isr = null;

		PrintWriter out = response.getWriter();

		try {
			isr = new InputStreamReader(request.getInputStream());
	        br = new BufferedReader(isr);

	        String inputJSON = br.readLine();

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			String encryptedCollectionName = jo.get("collectionName").toString();
			String drwId = jo.get("drwId").toString();

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().encryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collection-name", decryptedCollectionName);
			args.put("drawer-id", drwId);

			DbDrawer dbDrawer = new DbDrawer();

			int statusCd = dbDrawer.deleteDrawer(request, args);

			String drawerJson = dbDrawer.selectDrawerList(request, args);

			out.println(drawerJson);
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doDelete(): ", e);
		}
		finally {
			if(isr != null) {
				isr.close();
			}

			if(br != null) {
				br.close();
			}
		}
	}

}
