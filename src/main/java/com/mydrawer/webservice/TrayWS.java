package com.mydrawer.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import com.mydrawer.db.DbTray;
import com.mydrawer.util.Security;

@WebServlet(name = "Tray",urlPatterns = {"/Tray/*"})

public class TrayWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(TrayWS.class.getName());

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

			ArrayList<HashMap<String,String>> trayList = 
				new DbTray().selectTrayList(request, args);
			String trayJson = wsHelper.convertPayloadToJson(trayList);

			out.println(trayJson);
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doGet(): ", e);

			out.println("");
			out.flush();
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
			String trayName = jo.getString("trayName").toString();

			// Decrypt the token
			String decryptedCollectionName = 
				new Security().decryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", decryptedCollectionName);
			args.put("trayName", trayName);

			int statusCd = new DbTray().insertTray(request, args);

			out.println(Integer.toString(statusCd));
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doGet(): ", e);

			out.println(-1);
			out.flush();
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
			String trayId = jo.get("trayId").toString();
			String trayName = jo.getString("trayName").toString();

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().decryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", decryptedCollectionName);
			args.put("trayId", trayId);
			args.put("trayName", trayName);

			int statusCd = new DbTray().updateTray(request, args);

			out.println(Integer.toString(statusCd));
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPut(): ", e);

			out.println(-1);
			out.flush();
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
			String trayId = jo.get("trayId").toString();

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().decryptCollectionName(encryptedCollectionName);

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", decryptedCollectionName);
			args.put("trayId", trayId);

			int statusCd = new DbTray().deleteTray(request, args);

			out.println(Integer.toString(statusCd));
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doDelete(): ", e);

			out.println(-1);
			out.flush();
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
