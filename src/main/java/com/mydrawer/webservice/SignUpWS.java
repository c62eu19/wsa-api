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

import com.mydrawer.db.DbMongo;
import com.mydrawer.db.DbTray;
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
			 *  Convert the user's email to a collection name by removing all 
			 *  special characters and change to lowercase
			 */
			String collectionName = email.replaceAll("[^a-zA-Z0-9]", "").toLowerCase().trim();

			// Encrypt the password
			String encryptedPword = new Security().encryptPword(email, password);

			// Status of User (Active)
			String statusInd = "A";

			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("email",email);
			hm.put("password",encryptedPword);
			hm.put("userName",name);
			hm.put("collectionName",collectionName);
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

			// Create a Tray collection and add a default tray for the member
			String trayName = DbMongo.getTrayCollectionName(collectionName);
			DbMongo.createCollection(request.getServletContext(), trayName);

			// Add a default Favorites tray for the user
			HashMap<String,String> trayArgs = new HashMap<String,String>();

			trayArgs.put("collectionName",trayName);
			trayArgs.put("trayName", "Favorites");

			new DbTray().insertTray(request, trayArgs);

			// Create a Drawer collection for the member
			String drawerName = DbMongo.getDrawerCollectionName(collectionName);
			DbMongo.createCollection(request.getServletContext(), drawerName);

			// Create an Inbox collection for the member
			String inboxName = DbMongo.getInboxCollectionName(collectionName);
			DbMongo.createCollection(request.getServletContext(), inboxName);

			// Encrypt the collection name and use as the security token for all service calls
			String encryptedCollectionName = new Security().encryptCollectionName(collectionName);

			String trayJson = "";
			String drawerJson = "";

			wsHelper.sendResponse(
				request, response, "A", "Ok", encryptedCollectionName, name, trayJson, drawerJson); 
		}
		catch(InvalidSignupException e) {
			wsHelper.sendResponse(
				request, response, "E", e.getMessage(), "", "", "", ""); 
		}
		catch(Exception e) {
			wsHelper.sendResponse(
				request, response, "E", e.getMessage(), "", "", "", ""); 

			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
	}

}
