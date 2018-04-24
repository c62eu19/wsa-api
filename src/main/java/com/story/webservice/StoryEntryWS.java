package com.story.webservice;

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

import com.story.db.DbStory;
import com.story.util.DateUtility;
import com.story.util.Security;

@WebServlet(name = "Story Item Entry",urlPatterns = {"/StoryEntry/*"})

public class StoryEntryWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(StoryEntryWS.class.getName());

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

			// Token that identifies a member
			String genre = jo.get("genre").toString();
			String genreMnem = jo.get("genreMnem").toString();
			String createdBy = jo.get("createdBy").toString();
			String title = jo.get("title").toString();
			String content = jo.get("content").toString();
			String originalStoryId = jo.get("originalStoryId").toString();
			String ancestorStoryIdList = jo.get("ancestorStoryIdList").toString();
			String comments = jo.get("comments").toString();

			DbStory dbStory = new DbStory();

			// Get collection based on Genre Mnem
			String collectionName = "col_" + genreMnem.trim().toLowerCase();

			// Is this a new story or a new branch of an existing story?
			// If originalStoryId = 0 then it's a new story
			// If originalStoryId is an _id then it's a branch

			HashMap<String,String> args = new HashMap<String,String>();

			args.put("collectionName", collectionName);
			args.put("title", title);
			args.put("content", content);
			args.put("createdBy", createdBy);
			args.put("createdDate",DateUtility.getCurrentDateTime());
			args.put("updatedDate",DateUtility.getCurrentDateTime());
			args.put("originalStoryId", originalStoryId);
			args.put("ancestorStoryIdList", ancestorStoryIdList);
			args.put("imageBase64", ".");
			args.put("likes", "0");
			args.put("comments", comments);

			int statusCd = dbStory.insertStoryItem(request, args);

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
			String content = jo.get("content").toString();

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", collectionName);
			args.put("storyId", storyId);
			args.put("content", content);
			args.put("updatedDate",DateUtility.getCurrentDateTime());

			DbStory dbStory = new DbStory();

			int statusCd = dbStory.updateStoryItem(request, args);

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

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
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

			String collectionName = jo.get("collectionName").toString();
			String storyId = jo.get("storyId").toString();

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", collectionName);
			args.put("storyId", storyId);

			DbStory dbStory = new DbStory();

			int statusCd = dbStory.deleteStoryItem(request, args);

			ArrayList<HashMap<String,String>> storyList = 
				new DbStory().selectStoryList(request, args);
			String storyJson = wsHelper.convertPayloadToJson(storyList);

			out.println(storyJson);
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
