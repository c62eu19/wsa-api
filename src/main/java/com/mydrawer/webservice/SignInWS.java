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

			String mbrSk = member.getMbrSk();

			String mbrTrayJson = "";
			String mbrDrawerJson = "";

			// Based on a successful sign in, get the member content
			if(Integer.parseInt(mbrSk) > 0)
			{
				// Check if the member's account has been disabled
				if(member.getStatusInd().equalsIgnoreCase("D"))
				{
				}
				else
				{
					MemberTrayService mts = 
						new MemberTrayService();

					mbrTrayJson = mts.getMemberTrayList(request, mbrSk);

					MemberDrawerService mds = 
						new MemberDrawerService();

					mbrDrawerJson = mds.getMemberDrawerList(request, mbrSk);
				}
			}
			else
			{
			}

			// Encrypt the mbrSk
			String mbrSkToken = ms.encryptMbrSk(email, mbrSk);

			// TODO: Get the mmeber's drawer


			HashMap hm = new HashMap();

			hm.put("statusCd","0");
			hm.put("statusMsg","");
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

			String mbrSk = member.getMbrSk();

			// Based on a successful sign in, update the sign in count for the member
			if(!mbrSk.equals("-1"))
			{
				mDAO.updateMemberSignin(
					con, 
					mbrSk);

				con.commit();
			}

			if(con != null) con.close();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".signInMember(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return member;
	}

}
