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

import com.mydrawer.service.MemberTrayService;
import com.mydrawer.utility.Security;

@WebServlet(name = "membertrayws",urlPatterns = {"/membertrayws/*"})

public class MemberTrayWS extends HttpServlet
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
			String mbrSkToken = request.getRequestURI();

			// Decrypt mbrSk encrypted token
			String mbrSk = "";

			MemberTrayService mts = new MemberTrayService();

			String jsonResponse = 
				mts.getMemberTrayList(request, mbrSk);

			out.println(jsonResponse);
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

			String mbrSkToken = jo.get("mbrSkToken").toString();
			String name = jo.getString("name").toString();

			// Decrypt and parse out the mbrSkToken
			String decryptedMbrSkToken = Security.decrypt(mbrSkToken);
			String mbrSk = decryptedMbrSkToken.split("[|]")[0];

			MemberTrayService mts = new MemberTrayService();

			String jsonResponse = 
				mts.getMemberTrayList(request, mbrSk);

			out.println(jsonResponse);
			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doPost(): " + e);
		}
	}

	protected void doPut(
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

			String mbrSkToken = jo.get("mbrSkToken").toString();
			String traSk = jo.get("traSk").toString();
			String name = jo.getString("name").toString();

			// Decrypt and parse out the mbrSkToken
			String decryptedMbrSkToken = Security.decrypt(mbrSkToken);
			String mbrSk = decryptedMbrSkToken.split("[|]")[0];

			MemberTrayService mts = new MemberTrayService();

			String jsonResponse = 
				mts.getMemberTrayList(request, mbrSk);

			out.println(jsonResponse);
			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doPut(): " + e);
		}
	}

	protected void doDelete(
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

			String mbrSkToken = jo.get("mbrSkToken").toString();
			String traSk = jo.get("traSk").toString();

			// Decrypt and parse out the mbrSkToken
			String decryptedMbrSkToken = Security.decrypt(mbrSkToken);
			String mbrSk = decryptedMbrSkToken.split("[|]")[0];

			MemberTrayService mts = new MemberTrayService();


			String jsonResponse = 
				mts.getMemberTrayList(request, mbrSk);

			out.println(jsonResponse);
			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doDelete(): " + e);
		}
	}

}
