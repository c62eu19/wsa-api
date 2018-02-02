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

import com.mydrawer.db.DbUser;
import com.mydrawer.exception.InvalidSignupException;
import com.mydrawer.util.DateUtility;
import com.mydrawer.util.MailUtility;
import com.mydrawer.util.Security;

@WebServlet(name = "Signup",urlPatterns = {"/Signup/*"})

public class SignUpWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(SignUpWS.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		try {
			String inputJSON = "";

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
			if(dbUser.selectUserCount(request, email) <= 0) {
				throw new InvalidSignupException(
					"This email is already registered. Please choose another email or sign in.");
			}

			// Get the User's collection name which is based on their email
			String collectionName = this.deriveCollectionName(email);

			// Encrypt the password
			String encryptedPword = new Security().encryptPword(email, password);

			// Status of User (Active)
			String status = "A";

			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("email",email);
			hm.put("password",encryptedPword);
			hm.put("user-name",name);
			hm.put("collection-name",collectionName);
			hm.put("registration-date",DateUtility.getCurrentDateTime());
			hm.put("last-login-date",DateUtility.getCurrentDateTime());
			hm.put("login-count","1");
			hm.put("status-ind",status);

			// Add the new user and create their drawer
			int statusCd = dbUser.insertUser(request, hm);

			if(statusCd < 0) {
				throw new InvalidSignupException(
					"We apologize but there is an problem with our Member Sign up. " +
					"Please try again and if the problem persists then please send us a message " +
					"by clicking Contact Us from the Hamburger menu above.");
			}

			// Add a default tray for the member
//			MemberTrayService mts = new MemberTrayService();

//			mts.addMemberTray(request, mbrSk, "Favorites");

			// Encrypt the mbrSk
//			String mbrSkToken = userService.encryptMbrSk(mbrSk);

			String trayJson = "";
			String drawerJson = "";

			this.sendResponse(
				request, response, "A", "Ok", collectionName, name, trayJson, drawerJson); 
		}
		catch(InvalidSignupException e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);

			this.sendResponse(
				request, response, "E", e.getMessage(), "No Collection", "No Name", "No Tray", "No Drawer"); 
		}
		catch(Exception e) {
			this.sendResponse(
				request, response, "E", e.getMessage(), "No Collection", "No Name", "No Tray", "No Drawer"); 

			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
	}

	private String deriveCollectionName(String email) {

		String collectionName = "";

		try {
			/*
			 *  Convert the user's email to a collection name by removing all special characters and 
			 *  change to lowercase
			 */
			collectionName = email.replaceAll("[^a-zA-Z0-9]", "").toLowerCase().trim();
		}
		catch(Exception e) {
			collectionName = "";
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".deriveCollectionName(): ", e);
		}
		finally {}

		return collectionName;
	}

	private void sendResponse(
		HttpServletRequest request, 
		HttpServletResponse response, 
		String statusInd,
		String statusMsg,
		String collectionName,
		String userName,
		String trayJson,
		String drawerJson) 
			throws ServletException, IOException {

		response.setContentType("application/json");

		String jsonResponse = "";

		PrintWriter out = response.getWriter();

		try {
			HashMap<String,String> hm = new HashMap<String,String>();

			hm.put("statusInd", statusInd);
			hm.put("statusMsg", statusMsg);
			hm.put("collectioName", collectionName);
			hm.put("userName", userName);
			hm.put("trayJson", trayJson);
			hm.put("drawerJson", drawerJson);

			// Convert the hashmap to a JSON string
			JSONObject joPayload = new JSONObject(hm);
			jsonResponse = joPayload.toString();
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".sendResponse(): ", e);
		}

		out.println(jsonResponse);
		out.flush();
	}

}
