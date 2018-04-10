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

@WebServlet(name = "GenreList",urlPatterns = {"/GenreList/*"})

public class GenreListWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(GenreListWS.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		response.setContentType("application/json");

		WSHelper wsHelper = new WSHelper();

		PrintWriter out = response.getWriter();

		try {
			HashMap<String,String> args = new HashMap<String,String>();
			args.put("collectionName", "col_genre");

			ArrayList<HashMap<String,String>> genreList = 
				new DbStory().selectGenreList(request, args);
			String genreJson = wsHelper.convertPayloadToJson(genreList);

			out.println(genreJson);
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doGet(): ", e);
		}
	}

}
