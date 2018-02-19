package com.mydrawer.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mydrawer.db.DbContactUs;
import com.mydrawer.util.DateUtility;

@WebServlet(name = "ContactUs",urlPatterns = {"/ContactUs/*"})
public class ContactUsWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(ContactUsWS.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();

		try {
			String inputJSON = request.getParameter("inputJSON");

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			String email = jo.get("email").toString();
			String subject = jo.get("subject").toString();
			String message = jo.get("message").toString();

			HashMap<String,String> args = new HashMap<String,String>();
			args.put("email",email);
			args.put("subject",subject);
			args.put("message",message);
			args.put("insertedDate", DateUtility.getCurrentDateTime());

			// Add the new user and create their drawer
			int statusCd = new DbContactUs().insertContactUs(request, args);

			String jsonResponse = "";

			out.println(jsonResponse);
			out.flush();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
		finally
		{
			if(out != null) out.close();
		}
	}

}
