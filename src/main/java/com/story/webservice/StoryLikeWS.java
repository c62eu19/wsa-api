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

@WebServlet(name = "Story Like Entry",urlPatterns = {"/StoryLike/*"})

public class StoryLikeWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(StoryEntryWS.class.getName());

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

			String storyId = jo.get("storyId").toString();
			String genreMnem = jo.get("genreMnem").toString();

			DbStory dbStory = new DbStory();

			// Get collection based on Genre Mnem
			String collectionName = "col_" + genreMnem.trim().toLowerCase();

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", collectionName);
			args.put("storyId", storyId);

			int statusCd = dbStory.incrementStoryLikeTotal(request, args);

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
