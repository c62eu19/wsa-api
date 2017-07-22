package com.mydrawer.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.JSONObject;

import com.mydrawer.bean.Member;
import com.mydrawer.model.MemberDAO;
import com.mydrawer.service.MemberDrawerService;
import com.mydrawer.service.MemberService;
import com.mydrawer.service.MemberTrayService;

@WebServlet(name = "signinws",urlPatterns = {"/signinws/*"})
public class SignInWS extends HttpServlet
{
	private static final long serialVersionUID = 2857847752169838915L;

	protected void doPost(
		HttpServletRequest request, 
		HttpServletResponse response) 
			throws ServletException, IOException
	{
		response.setContentType("application/json");

		String jsonResponse = "";

		PrintWriter out = response.getWriter();

		try
		{
			String inputJSON = request.getParameter("inputJSON");

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			String email = jo.get("email").toString();
			String password = jo.get("password").toString();

			MemberService ms = new MemberService();

			// Encrypt the password
			String encryptedPword = ms.encryptPword(email, password);
 
			Member member = new Member();

			// Sign the member in
			member = 
				this.signInMember(request, email, encryptedPword);

			String statusInd = "";

			String mbrSk = "";
			String mbrSkToken = "";
			String mbrTrayJson = "";
			String mbrDrawerJson = "";

			statusInd = member.getStatusInd();

			// Check the member's status
			if(statusInd.equalsIgnoreCase("A"))
			{
				mbrSk = member.getMbrSk();

				// Encrypt the mbrSk
				mbrSkToken = ms.encryptMbrSk(mbrSk);

				MemberTrayService mts = 
					new MemberTrayService();

				mbrTrayJson = mts.getMemberTrayList(request, mbrSk);

				MemberDrawerService mds = 
					new MemberDrawerService();

				mbrDrawerJson = mds.getMemberDrawerListByMbrSk(request, mbrSk);
			}
			else
			{
				// Other problem with Sign in
				statusInd = "E";
			}

			HashMap hm = new HashMap();

			hm.put("statusInd",statusInd);
			hm.put("mbrSkToken",mbrSkToken);
			hm.put("mbrName",member.getName());
			hm.put("mbrTrayJson", mbrTrayJson);
			hm.put("mbrDrawerJson", mbrDrawerJson);

			// Convert the hashmap to a JSON string
			JSONObject joPayload = new JSONObject(hm);
			jsonResponse = joPayload.toString();

			out.println(jsonResponse);
			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doPost(): " + e);
		}
	}

	private Member signInMember(
		HttpServletRequest request,
		String argEmail,
		String argEncryptedPword)
			throws SQLException 
	{
		Connection con = null;

		Member member = new Member();

		try
		{
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con = ds.getConnection();

			con.setAutoCommit(false);

			// Prepopulate the profile template with member data
			MemberDAO mDAO = 
				new MemberDAO();

			// Check if email and password exist
			member = 
				mDAO.selectMemberSignin(
					con, 
					argEmail, 
					argEncryptedPword);

			String statusInd = member.getStatusInd();

			// Based on a successful sign in, update the sign in count for the member
			if(!statusInd.equalsIgnoreCase("E"))
			{
				String mbrSk = member.getMbrSk();

				mDAO.updateMemberSignin(
					con, 
					mbrSk);

				con.commit();
			}

			if(con != null) con.close();
		}
		catch(Exception e)
		{
			member.setStatusInd("E");
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".signInMember(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return member;
	}

}
