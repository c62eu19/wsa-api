package com.mydrawer.webservice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mydrawer.service.MemberDrawerService;
import com.mydrawer.utility.Security;

@WebServlet(name = "sharewebentryws",urlPatterns = {"/sharewebentryws/*"})
public class ShareWebEntryWS extends HttpServlet
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

			// Token that identifies a member
			String mbrSkToken = jo.get("mbrSkToken").toString();
			String traSk = jo.get("traSk").toString();
			String title = jo.get("title").toString();
			String text = jo.get("text").toString();
			String url = jo.get("url").toString();

			Security s = new Security();

			// Decrypt the mbrSkToken
			String mbrSk = s.decrypt(mbrSkToken);

			// Web link
			String typeId ="4";

			// Add the new post
			MemberDrawerService mds = new MemberDrawerService();

			mds.addMemberDrawer(
				request,
				mbrSk,
				traSk,
				typeId,
				title,
				text,
				url);

			String jsonResponse = 
				mds.getMemberDrawerList(request, mbrSk);

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
