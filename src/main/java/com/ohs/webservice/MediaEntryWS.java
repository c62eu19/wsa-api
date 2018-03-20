package com.ohs.webservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import com.ohs.util.DateUtility;
import com.ohs.util.Security;

@WebServlet(name = "Media Entry",urlPatterns = {"/MediaEntry/*"})

public class MediaEntryWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(MediaEntryWS.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		response.setContentType("application/json");

		WSHelper wsHelper = new WSHelper();

		OutputStream os = null;

		PrintWriter out = response.getWriter();

		try {
			String inputJSON = request.getParameter("inputJSON");

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			// Token that identifies a member
			String encryptedCollectionName = jo.get("collectionName").toString();

			String trayId = jo.get("trayId").toString();
			String title = jo.get("title").toString();
			String text = jo.get("text").toString();
			String type = jo.get("type").toString();
			String mediaType = jo.get("mediaType").toString();
			String mediaBase64 = jo.get("mediaBase64").toString();

			// Split out the data:image/png;base64, from the base64 String
			String tempBase64 = mediaBase64;
			String[] base64CommaDelimTokens = tempBase64.split(",");

			mediaType = base64CommaDelimTokens[0];
			String base64Image = base64CommaDelimTokens[1];

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().decryptCollectionName(encryptedCollectionName);

			String url = ".";

			// Add the new post
			DbDrawer dbDrawer = new DbDrawer();

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", decryptedCollectionName);
			args.put("trayId", trayId);
			args.put("title", title);
			args.put("text", text);
			args.put("url", url);
			args.put("mediaType", mediaType);
			args.put("mediaBase64", mediaBase64);
			args.put("type", type);
			args.put("insertedDate",DateUtility.getCurrentDateTime());
			args.put("updatedDate",DateUtility.getCurrentDateTime());

			dbDrawer.insertDrawer(request, args);

			ArrayList<HashMap<String,String>> drawerList = 
				new DbDrawer().selectDrawerList(request, args);
			String drawerJson = wsHelper.convertPayloadToJson(drawerList);

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

		WSHelper wsHelper = new WSHelper();

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
System.out.println(encryptedCollectionName);
			String drawerId = jo.get("drawerId").toString();
System.out.println(drawerId);
			String trayId = jo.get("trayId").toString();
System.out.println(trayId);
			String title = jo.get("title").toString();
System.out.println(title);
			String text = jo.get("text").toString();
System.out.println(text);
			String type = jo.get("type").toString();
System.out.println(type);
			String mediaType = jo.get("mediaType").toString();
System.out.println(mediaType);
			String mediaBase64 = jo.get("mediaBase64").toString();
System.out.println(mediaBase64);

			// Split out the data:image/png;base64, from the base64 String
//			String tempBase64 = mediaBase64;
//			String[] base64CommaDelimTokens = tempBase64.split(",");

			mediaType = ".";
			String base64Image = mediaBase64;

			// Encrypt the collection name and use as the security token for all service calls
			String decryptedCollectionName = 
				new Security().decryptCollectionName(encryptedCollectionName);

			String url = ".";

			// Add the new post
			DbDrawer dbDrawer = new DbDrawer();

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", decryptedCollectionName);
			args.put("drawerId", drawerId);
			args.put("trayId", trayId);
			args.put("title", title);
			args.put("text", text);
			args.put("url", url);
			args.put("mediaType", mediaType);
			args.put("mediaBase64", mediaBase64);
			args.put("updatedDate",DateUtility.getCurrentDateTime());

			int statusCd = dbDrawer.updateDrawer(request, args);

			ArrayList<HashMap<String,String>> drawerList = 
				new DbDrawer().selectDrawerList(request, args);
			String drawerJson = wsHelper.convertPayloadToJson(drawerList);

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

}
