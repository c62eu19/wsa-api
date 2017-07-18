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

import com.mydrawer.service.MemberDrawerService;
import com.mydrawer.service.MemberService;

@WebServlet(name = "memberdrawerlistws",urlPatterns = {"/memberdrawerlistws/*"})

public class MemberDrawerListWS extends HttpServlet
{
	private static final long serialVersionUID = 2857847752169838915L;

	protected void doGet(
		HttpServletRequest request, 
		HttpServletResponse response) 
			throws ServletException, IOException
	{
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();

		try
		{
			String mbrSkToken = request.getPathInfo();

			// Exclude the beginning / of the query param
			String newMbrSkToken = mbrSkToken.substring(1, mbrSkToken.length());

			MemberService ms = new MemberService();

			// Decrypt mbrSk encrypted token
			String mbrSk = ms.decryptMbrSk(newMbrSkToken);

			MemberDrawerService mts = new MemberDrawerService();

			String mbrDrawerJson = 
				mts.getMemberDrawerList(request, mbrSk);

			out.println(mbrDrawerJson);
			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doGet(): " + e);
		}
	}

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

			MemberDrawerService mds = new MemberDrawerService();

			String jsonResponse = mds.getMemberDrawerList(request,"mbrSk");

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