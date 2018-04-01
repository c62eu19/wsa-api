package com.story.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.story.db.DbStory;
import com.story.db.DbUser;
import com.story.exception.InvalidSigninException;
import com.story.exception.InvalidSignupException;
import com.story.util.Security;

@WebServlet(name = "Signin",urlPatterns = {"/Signin/*"})

public class SignInWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(SignInWS.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		WSHelper wsHelper = new WSHelper();

		try {
			String inputJSON = request.getParameter("inputJSON");

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			String email = jo.get("email").toString();
			String password = jo.get("password").toString();

			Security security = new Security();

			// Encrypt the password
			String encryptedPword = security.encryptPword(email, password);

			// Sign the user in
			HashMap<String,String> hm = 
				new DbUser().selectUserSignin(request, email, encryptedPword);

			String statusInd = "";

			statusInd = hm.get("statusInd");

			// Check the user's status
			if(statusInd.equalsIgnoreCase("E")) {
				throw new InvalidSigninException(
					"We apologize but there is an problem with your Sign in. " +
					"Please try again and if the problem persists then please send us a message " +
					"by clicking Contact Us from the Hamburger menu above.");
			}

			String storyJson = "";

			wsHelper.sendResponse(
				request, response, statusInd, "", hm.get("userToken"), hm.get("userName"), storyJson);
		}
		catch(InvalidSigninException e) {
			wsHelper.sendResponse(
				request, response, "E", e.getMessage(), "", "", ""); 
		}
		catch(Exception e)
		{
			wsHelper.sendResponse(
				request, response, "E", e.getMessage(), "", "", ""); 

			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
	}

}
