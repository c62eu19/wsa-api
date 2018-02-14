package com.mydrawer.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class WSHelper {

	private static final Logger logger = Logger.getLogger(WSHelper.class.getName());

	public String convertPayloadToJson(Object object) {

		String jsonResponse = "";

		try {

			// Convert the object based on type to a JSON string
			if(object instanceof HashMap) {
				JSONObject joPayload = new JSONObject((HashMap)object);
				jsonResponse = joPayload.toString();

			} else if(object instanceof ArrayList) {
				JSONArray jaPayload = new JSONArray((ArrayList)object);
				jsonResponse = jaPayload.toString();
			}
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".sendResponse(): ", e);
		}
		finally {}

		return jsonResponse;
	}

	public void sendResponse(
		HttpServletRequest request, 
		HttpServletResponse response, 
		String statusInd,
		String statusMsg,
		String collectionName,
		String userName,
		String trayJson,
		String drawerJson) 
			throws ServletException, IOException {

		response.setContentType("application/json");

		String jsonResponse = "";

		PrintWriter out = response.getWriter();

		try {
			HashMap<String,String> hm = new HashMap<String,String>();

			hm.put("statusInd", statusInd);
			hm.put("statusMsg", statusMsg);
			hm.put("collectionName", collectionName);
			hm.put("userName", userName);
			hm.put("trayJson", trayJson);
			hm.put("drawerJson", drawerJson);

			// Convert the hashmap to a JSON string
			JSONObject joPayload = new JSONObject(hm);
			jsonResponse = joPayload.toString();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".sendResponse(): ", e);
		}
		finally {}

		out.println(jsonResponse);
		out.flush();
	}

}
