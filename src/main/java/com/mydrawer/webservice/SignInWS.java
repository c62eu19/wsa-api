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
import com.mydrawer.mediator.DrawerMediator;

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
			HashMap<String,String> hm =
				dbUser.selectMemberSignin(request, email, encryptedPword);

			String statusInd = "";

			String mbrSk = "";
			String mbrSkToken = "";
			String mbrTrayJson = "";
			String mbrDrawerJson = "";

			statusInd = hm.get("status-ind");

			// Check the member's status
			if(statusInd.equalsIgnoreCase("A")) {
				String id = hm.get("id");

				dbUser.updateMemberSignin(request, id);

				// Encrypt the mbrSk
//				mbrSkToken = ms.encryptMbrSk(mbrSk);

//				mbrTrayJson = mts.getMemberTrayList(request, mbrSk);

				DrawerMediator mds = new DrawerMediator();

				mbrDrawerJson = mds.getMemberDrawerListByMbrSk(request, mbrSk);
			}
			else
			{
				// Other problem with Sign in
				statusInd = "E";
			}

//			this.sendResponse(
//				request, response, statusInd, statusMsg, collectionName, userName, trayJson, drawerJson);
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
