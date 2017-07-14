package com.mydrawer.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mydrawer.bean.Media;
import com.mydrawer.service.MemberDrawerService;
import com.mydrawer.utility.URLUtility;

@WebServlet(name = "urlpartsws",urlPatterns = {"/urlpartsws/*"})
public class UrlPartsWS extends HttpServlet
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

			String url = jo.get("url").toString();

			URLUtility u = new URLUtility();

			HashMap hm = u.parseUrlPageHeaderInfo(url);

			String statusCd = (String)hm.get("statusCd");
			String title = (String)hm.get("title");
			String descr = (String)hm.get("descr");

			String mediaType = "";

			if(statusCd.equals("0"))
			{
				Media media = new Media();

				MemberDrawerService sf = new MemberDrawerService();

				// Default to ARTICLE unless it's a video
				mediaType = "ARTICLE";

				String protocol = u.getUrlComponent(url, "protocol");
				String host = u.getUrlComponent(url, "host");
				String query = u.getUrlComponent(url, "query");
				String queryPath = u.getUrlComponent(url, "path");

				// If it's a youtube, vimeo or ted video then build the html to embed it
				String videoType = "";
				if(host.matches("(?i).*youtube.*")) videoType = "YOUTUBE";
				if(host.matches("(?i).*vimeo.*")) videoType = "VIMEO";
				if(host.matches("(?i).*ted.*")) videoType = "TED";

				if(videoType.equals("YOUTUBE") || videoType.equals("VIMEO") || videoType.equals("TED"))
				{
					String userAgent = 
						(String)request.getHeader("User-Agent");
					if(userAgent == null) userAgent = "";

					mediaType = "VIDEO";
				}
			}

			HashMap hmPayload = new HashMap();
			hmPayload.put("statusCd", statusCd);
			hmPayload.put("mediaType", mediaType);
			hmPayload.put("title", title);
			hmPayload.put("text", descr);
			hmPayload.put("url", url);

			// Convert the hashmap to a JSON string
			JSONObject joOutput = new JSONObject(hmPayload);
			String jsonResponse = joOutput.toString();

			out.println(jsonResponse);
			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doPost(): " + e);
		}
	}

}
