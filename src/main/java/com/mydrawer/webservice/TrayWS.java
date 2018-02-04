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

import com.mydrawer.db.DbTray;
import com.mydrawer.util.Security;

@WebServlet(name = "Tray",urlPatterns = {"/Tray/*"})

public class TrayWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(TrayWS.class.getName());

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

			String trayJson = new DbTray().selectTrayList(request, args);

			out.println(trayJson);
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

			String encryptedCollectionName = jo.get("collectionName").toString();
			String name = jo.getString("name").toString();

			// Decrypt the token
			String decryptedCollectionName = 
				new Security().encryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collection-name", decryptedCollectionName);
			args.put("tray-name", name);

			DbTray dbTray = new DbTray();

			int statusCd = dbTray.insertTray(request, args);

			out.println(Integer.toString(statusCd));
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doGet(): ", e);
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

			String encryptedCollectionName = jo.get("collectionName").toString();
			String id = jo.get("id").toString();
			String name = jo.getString("name").toString();

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().encryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collection-name", decryptedCollectionName);
			args.put("id", id);
			args.put("tray-name", name);

			DbTray dbTray = new DbTray();

			int statusCd = dbTray.updateTray(request, args);

			out.println(Integer.toString(statusCd));
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

			String encryptedCollectionName = jo.get("encryptedCollectionName").toString();
			String id = jo.get("id").toString();

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().encryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collection-name", decryptedCollectionName);
			args.put("id", id);

			DbTray dbTray = new DbTray();

			int statusCd = dbTray.deleteTray(request, args);

			out.println(Integer.toString(statusCd));
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