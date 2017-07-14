package com.mydrawer.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.json.JSONObject;

@WebServlet(name = "draweritemws",urlPatterns = {"/draweritemws/*"})

public class DrawerItemWS extends HttpServlet
{
	private static final long serialVersionUID = 2857847752169838915L;

	protected void doPost(
		HttpServletRequest request, 
		HttpServletResponse response) 
			throws ServletException, IOException
	{
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();

		try
		{
			String inputJSON = request.getParameter("inputJSON");

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			String dwrSk = jo.get("dwrSk").toString();

			// Get the template social content
//			ItemService ss = new ItemService();

//			String htmlContent = 
//				ss.getItemDetail(request, itemSk);

//			HashMap hmPayload = new HashMap();
//			hmPayload.put("htmlMenu", htmlMenu);
//			hmPayload.put("htmlContent", htmlContent);

			// Convert the hashmap to a JSON string
//			JSONObject joPayload = new JSONObject(hmPayload);

//			String jsonResponse = joPayload.toString();

//			out.println(jsonResponse);
//			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doPost(): " + e);
		}
	}

}
