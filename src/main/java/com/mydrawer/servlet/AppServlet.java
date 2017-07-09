package com.mydrawer.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mydrawer.utility.FileUtility;

@WebServlet(name = "mydrawer",urlPatterns = {"/mydrawer/*"})
public class AppServlet extends HttpServlet
{
	private static final long serialVersionUID = 2857847752169838915L;

	public void init(ServletConfig config) throws ServletException 
	{
		try
		{
			super.init(config);
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".init(): " + e);
		}
		finally
		{
		}
	}

	protected void doGet(
		HttpServletRequest request, 
		HttpServletResponse response) 
			throws ServletException, IOException
	{
		response.setContentType("text/html");

		PrintWriter writer = response.getWriter();

System.out.println("I AM HERE");

		try
		{
			String path = request.getRequestURI();

			// Load the pages properties file for this servlet to always get the latest
			FileUtility fu = 
				new FileUtility();

			Properties prop = 
				fu.readPropFile("configure/app-mappings.properties");

			// Get the main template page template for this servlet
			String htmlResourceName = 
				prop.getProperty(path,"html/main.html");
			File htmlResourceFile = new File(System.getenv("OPENSHIFT_DATA_DIR") + htmlResourceName);
			String htmlResource = fu.readFile(htmlResourceFile);

			// Serve it
			writer.println(htmlResource);
			writer.flush();

			writer.close();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doGet(): " + e);
		}
		finally
		{
			if(writer != null) writer.close();
		}
	}

}
