package com.mydrawer.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mydrawer.mediator.DrawerMediator;
import com.mydrawer.util.Security;

@WebServlet(name = "memberdrawerentryws",urlPatterns = {"/memberdrawerentryws/*"})
public class DrawerEntryWS extends HttpServlet
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
			String typeId = jo.get("typeId").toString();

			Security s = new Security();

			// Decrypt the mbrSkToken
			String mbrSk = s.decrypt(mbrSkToken);

			// Add the new post
			DrawerMediator mds = new DrawerMediator();

			mds.addMemberDrawer(
				request,
				mbrSk,
				traSk,
				typeId,
				title,
				text,
				url);

			String mbrDrawerJson = 
				mds.getMemberDrawerListByMbrSk(request, mbrSk);

			out.println(mbrDrawerJson);
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

		BufferedReader br = null;
		InputStreamReader isr = null;

		PrintWriter out = response.getWriter();

		try
		{
			isr = new InputStreamReader(request.getInputStream());
	        br = new BufferedReader(isr);

	        String inputJSON = br.readLine();

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			// Token that identifies a member
			String mbrSkToken = jo.get("mbrSkToken").toString();
			String drwSk = jo.get("drwSk").toString();
			String traSk = jo.get("traSk").toString();
			String url = jo.get("url").toString();
			String title = jo.get("title").toString();
			String text = jo.get("text").toString();

			Security s = new Security();

			// Decrypt the mbrSkToken
			String mbrSk = s.decrypt(mbrSkToken);

			// Add the new post
			DrawerMediator mds = new DrawerMediator();

			int statusCd = 
				mds.updateMemberDrawer(request, drwSk, traSk, title, text, url);

			String mbrDrawerJson = 
				mds.getMemberDrawerListByMbrSk(request, mbrSk);

			out.println(mbrDrawerJson);
			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doPut(): " + e);
		}
		finally
		{
			if(isr != null) isr.close();
			if(br != null) br.close();
		}
	}

	protected void doDelete(
		HttpServletRequest request, 
		HttpServletResponse response) 
			throws ServletException, IOException
	{
		response.setContentType("application/json");

		BufferedReader br = null;
		InputStreamReader isr = null;

		PrintWriter out = response.getWriter();

		try
		{
			isr = new InputStreamReader(request.getInputStream());
	        br = new BufferedReader(isr);

	        String inputJSON = br.readLine();

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			String mbrSkToken = jo.get("mbrSkToken").toString();
			String drwSk = jo.get("drwSk").toString();

			Security s = new Security();

			// Decrypt and parse out the mbrSkToken
			String mbrSk = s.decrypt(mbrSkToken);

			DrawerMediator mds = new DrawerMediator();

			int statusCd = 
				mds.deleteMemberDrawer(request, drwSk);

			String mbrDrawerJson = 
				mds.getMemberDrawerListByMbrSk(request, mbrSk);

			out.println(mbrDrawerJson);
			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doDelete(): " + e);
		}
		finally
		{
			if(isr != null) isr.close();
			if(br != null) br.close();
		}
	}

}
