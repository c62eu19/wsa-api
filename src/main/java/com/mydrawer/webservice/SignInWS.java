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

import com.mydrawer.db.DbDrawer;
import com.mydrawer.db.DbTray;
import com.mydrawer.db.DbUser;
import com.mydrawer.exception.InvalidSigninException;
import com.mydrawer.exception.InvalidSignupException;
import com.mydrawer.util.Security;

@WebServlet(name = "Signin",urlPatterns = {"/Signin/*"})

public class SignInWS extends HttpServlet {

	private static final long serialVersionUID = 2857847752169838915L;

	private static final Logger logger = Logger.getLogger(SignInWS.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

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

			DbUser dbUser = new DbUser();

			// Sign the user in
			HashMap<String,String> hm = dbUser.selectUserSignin(request, email, encryptedPword);

			String statusInd = "";

			statusInd = hm.get("statusInd");

			// Check the user's status
			if(statusInd.equalsIgnoreCase("E")) {
				throw new InvalidSigninException(
					"We apologize but there is an problem with your Sign in. " +
					"Please try again and if the problem persists then please send us a message " +
					"by clicking Contact Us from the Hamburger menu above.");
			}

			String id = hm.get("id");
			String loginCount = hm.get("loginCount");

			dbUser.updateUserSignin(request, id, loginCount);

			String collectionName = hm.get("collectionName");

			// Encrypt the collection name and use as the security token for all service calls
			String encryptedCollectionName = 
				new Security().encryptCollectionName(collectionName);

			// Get the user's trays
			HashMap<String,String> trayArgs = new HashMap<String,String>();
			trayArgs.put("collectionName", collectionName);

			String trayJson = new DbTray().selectTrayList(request, trayArgs);

			// Get the user's drawer
			HashMap<String,String> drawerArgs = new HashMap<String,String>();
			drawerArgs.put("collectionName", collectionName);

			String drawerJson = new DbDrawer().selectDrawerList(request, drawerArgs);

			this.sendResponse(
				request, response, statusInd, "", encryptedCollectionName, hm.get("userName"), trayJson, drawerJson);
		}
		catch(InvalidSignupException e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);

			this.sendResponse(
				request, response, "E", e.getMessage(), "No Collection", "No Name", "No Tray", "No Drawer"); 
		}
		catch(Exception e)
		{
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
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
			hm.put("collectionName", collectionName);
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
