package com.mydrawer.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mydrawer.utility.FileUtility;

@WebServlet(name = "contactusws",urlPatterns = {"/contactusws/*"})
public class ContactUsWS extends HttpServlet
{
	private static final long serialVersionUID = 2857847752169838915L;

	protected void doPost(
		HttpServletRequest request, 
		HttpServletResponse response) 
			throws ServletException, IOException
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();

		try
		{
			String inputJSON = request.getParameter("inputArgs");

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			String emailFrom = jo.get("emailFrom").toString();
			String emailSubj = jo.get("emailSubj").toString();
			String emailMsg = jo.get("emailMsg").toString();

//			int statusCd = 
//				this.addContactUs(request, emailFrom, emailSubj, emailMsg);

			String jsonResponse = "";

			out.println(jsonResponse);
			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doPost(): " + e);
		}
		finally
		{
			if(out != null) out.close();
		}
	}

	private int addContactUs(
		HttpServletRequest request,
		String argEmailFrom,
		String argEmailSubj,
		String argEmailMsg)
			throws IOException 
	{

		int statusCd = 0;

		try
		{
		}
		catch(Exception e)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".addContactUs(): " + e);
		}
		finally
		{

		}

		return statusCd;
	}

}
