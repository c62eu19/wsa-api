package com.ohs.webservice;

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

import com.ohs.db.DbDrawer;
import com.ohs.db.DbTray;
import com.ohs.db.DbUser;
import com.ohs.exception.InvalidSigninException;
import com.ohs.exception.InvalidSignupException;
import com.ohs.util.Security;

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

			String collectionName = hm.get("collectionName");

			// Encrypt the collection name and use as the security token for all service calls
			String encryptedCollectionName = 
				new Security().encryptCollectionName(collectionName);

			// Get the user's trays
			HashMap<String,String> trayArgs = new HashMap<String,String>();
			trayArgs.put("collectionName", collectionName);

			ArrayList<HashMap<String,String>> trayList = 
				new DbTray().selectTrayList(request, trayArgs);
			String trayJson = wsHelper.convertPayloadToJson(trayList);

			// Get the user's drawer
			HashMap<String,String> drawerArgs = new HashMap<String,String>();
			drawerArgs.put("collectionName", collectionName);

			ArrayList<HashMap<String,String>> drawerList = 
				new DbDrawer().selectDrawerList(request, drawerArgs);
			String drawerJson = wsHelper.convertPayloadToJson(drawerList);

			wsHelper.sendResponse(
				request, response, statusInd, "", encryptedCollectionName, hm.get("userName"), trayJson, drawerJson);
		}
		catch(InvalidSigninException e) {
			wsHelper.sendResponse(
				request, response, "E", e.getMessage(), "", "", "", ""); 
		}
		catch(Exception e)
		{
			wsHelper.sendResponse(
				request, response, "E", e.getMessage(), "", "", "", ""); 

			logger.log(
				Level.SEVERE, this.getClass().getName() + ".doPost(): ", e);
		}
	}

}
