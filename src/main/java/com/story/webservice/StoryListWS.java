package com.story.webservice;

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

import com.story.webservice.WSHelper;
import com.story.db.DbStory;

@WebServlet(name = "StoryList",urlPatterns = {"/StoryList/*"})

public class StoryListWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(StoryListWS.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		response.setContentType("application/json");

		WSHelper wsHelper = new WSHelper();

		PrintWriter out = response.getWriter();

		try {
			String pathInfo = request.getPathInfo();

			// Exclude the beginning / of the query param
			String genre = pathInfo.substring(1, pathInfo.length());

			String collectionName = "col_" + genre.toLowerCase();

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", collectionName);

			ArrayList<HashMap<String,String>> storyList = 
				new DbStory().selectStoryList(request, args);
			String storyJson = wsHelper.convertPayloadToJson(storyList);

			out.println(storyJson);
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
			String collectionName = jo.get("collectionName").toString();
			String searchTerm = jo.getString("searchTerm").toString();

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", collectionName);
			args.put("searchTerm", searchTerm);

			String storyJson = "";

			DbStory dbStory = new DbStory();

			ArrayList<HashMap<String,String>> storyList = new ArrayList<>();

			if(searchType.equalsIgnoreCase("WILDCARD")) {

				// If an empty searchTerm is entered then get the entire story
				if(searchTerm == null || searchTerm.equals("")) {
					storyList = new DbStory().selectStoryList(request, args);

				} else {
					storyList = dbStory.selectStoryListByWildcard(request, args);
				}
			}

			storyJson = wsHelper.convertPayloadToJson(storyList);

			out.println(storyJson);
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
	}

}
