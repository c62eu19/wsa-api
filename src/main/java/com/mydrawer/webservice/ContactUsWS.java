package com.mydrawer.webservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

			String msgFrom = jo.get("msgFrom").toString();
			String msgSubj = jo.get("msgSubj").toString();
			String msg = jo.get("msg").toString();

			int statusCd = 
				this.addContactUs(msgFrom, msgSubj, msg);

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
		String argMsgFrom,
		String argMsgSubj,
		String argMsg)
			throws IOException 
	{
		BufferedWriter bw = null;
		FileWriter fw = null;

		int statusCd = 0;

		try
		{
			String contactUsFile = 
				System.getenv("OPENSHIFT_DATA_DIR") + "contactus/contact-us.txt";

			File file = new File(contactUsFile);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			String data = 
				argMsgFrom + "\n" +
				argMsgSubj + "\n" +
				argMsg + 
				"\n";

			bw.write(data);
		}
		catch(Exception e)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".addContactUs(): " + e);
		}
		finally
		{
			if (bw != null)
				bw.close();

			if (fw != null)
				fw.close();
		}

		return statusCd;
	}

}
