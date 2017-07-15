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

@WebServlet(name = "draweritementryws",urlPatterns = {"/draweritementryws/*"})
public class DrawerItemEntryWS extends HttpServlet
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
			String mbrSk = jo.get("mbrSk").toString();

			String traSk = jo.get("traSk").toString();
			String title = jo.get("title").toString();
			String text = jo.get("text").toString();
			String url = jo.get("url").toString();
			String typeId = jo.get("typeId").toString();

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
