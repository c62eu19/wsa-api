package com.story.webservice;

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

import com.story.db.DbStory;
import com.story.util.DateUtility;
import com.story.util.Security;

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
			String collectionName = jo.get("collectionName").toString();

			String topicId = jo.get("topicId").toString();
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

			String url = ".";

			// Add the new post
			DbStory dbStory = new DbStory();

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", collectionName);
			args.put("topicId", topicId);
			args.put("title", title);
			args.put("text", text);
			args.put("url", url);
			args.put("mediaType", mediaType);
			args.put("mediaBase64", mediaBase64);
			args.put("type", type);
			args.put("insertedDate",DateUtility.getCurrentDateTime());
			args.put("updatedDate",DateUtility.getCurrentDateTime());

			dbStory.insertStoryItem(request, args);

			ArrayList<HashMap<String,String>> storyList = 
				new DbStory().selectStoryList(request, args);
			String storyJson = wsHelper.convertPayloadToJson(storyList);

			out.println(storyJson);
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
			String collectionName = jo.get("collectionName").toString();
			String storyId = jo.get("storyId").toString();
			String topicId = jo.get("topicId").toString();
			String title = jo.get("title").toString();
			String text = jo.get("text").toString();
			String type = jo.get("type").toString();
			String mediaType = jo.get("mediaType").toString();
			String mediaBase64 = jo.get("mediaBase64").toString();

			// Split out the data:image/png;base64, from the base64 String
//			String tempBase64 = mediaBase64;
//			String[] base64CommaDelimTokens = tempBase64.split(",");

			mediaType = ".";
			String base64Image = mediaBase64;

			String url = ".";

			// Add the new post
			DbStory dbFeed = new DbStory();

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", collectionName);
			args.put("storyId", storyId);
			args.put("topicId", topicId);
			args.put("title", title);
			args.put("text", text);
			args.put("url", url);
			args.put("mediaType", mediaType);
			args.put("mediaBase64", mediaBase64);
			args.put("updatedDate",DateUtility.getCurrentDateTime());

			int statusCd = dbFeed.updateStoryItem(request, args);

			ArrayList<HashMap<String,String>> storyList = 
				new DbStory().selectStoryList(request, args);
			String storyJson = wsHelper.convertPayloadToJson(storyList);

			out.println(storyJson);
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
