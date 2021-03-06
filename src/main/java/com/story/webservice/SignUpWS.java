package com.story.webservice;

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

import com.story.db.DbMongo;
import com.story.db.DbUser;
import com.story.exception.InvalidSignupException;
import com.story.util.DateUtility;
import com.story.util.MailUtility;
import com.story.util.Security;

@WebServlet(name = "Signup",urlPatterns = {"/Signup/*"})

public class SignUpWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(SignUpWS.class.getName());

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
			String name = jo.getString("name").toString();

			DbUser dbUser = new DbUser();

			// Check if valid email address
			if(!MailUtility.isEmailValid(email)) {
				throw new InvalidSignupException(
					"The entered Email is not a valid Email address.");
			}

			// Check if this user is already in the system
			if(dbUser.selectUserCount(request, email) > 0) {
				throw new InvalidSignupException(
					"This email is already registered. Please choose another email or sign in.");
			}

			/*
			 *  Removing all special characters from email, change to lowercase and encrypt it 
			 *  for use as a token on all calls
			 */
			String userToken = email.replaceAll("[^a-zA-Z0-9]", "").toLowerCase().trim();

			// Encrypt the user token
			String encryptedUserToken = new Security().encryptUserToken(userToken);

			// Encrypt the password
			String encryptedPword = new Security().encryptPword(email, password);

			// Status of User (Active)
			String statusInd = "A";

			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("email",email);
			hm.put("password",encryptedPword);
			hm.put("userName",name);
			hm.put("userToken",encryptedUserToken);
			hm.put("registrationDate",DateUtility.getCurrentDateTime());
			hm.put("lastLoginDate",DateUtility.getCurrentDateTime());
			hm.put("loginCount","1");
			hm.put("statusInd",statusInd);

			// Add the new user and create their drawer
			int statusCd = dbUser.insertUser(request, hm);

			if(statusCd < 0) {
				throw new InvalidSignupException(
					"We apologize but there is an problem with our Member Sign up. " +
					"Please try again and if the problem persists then please send us a message " +
					"by clicking Contact Us from the Hamburger menu above.");
			}

			String storyJson = "";

			wsHelper.sendResponse(
				request, response, "A", "Ok", encryptedUserToken, name, storyJson); 
		}
		catch(InvalidSignupException e) {
			wsHelper.sendResponse(
				request, response, "E", e.getMessage(), "", "", ""); 
		}
		catch(Exception e) {
			wsHelper.sendResponse(
				request, response, "E", e.getMessage(), "", "", ""); 

			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
	}

}
