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

import com.mydrawer.model.MemberDAO;
import com.mydrawer.service.MemberService;
import com.mydrawer.service.MemberTrayService;
import com.mydrawer.utility.Mail;
import com.mydrawer.utility.Security;

@WebServlet(name = "signupws",urlPatterns = {"/signupws/*"})
public class SignUpWS extends HttpServlet
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
			String name = jo.getString("name").toString();
			String pin = ".";

			MemberService ms = new MemberService();

			// Encrypt the password
			String encryptedPword = ms.encryptPword(email, password);

			// Encrypt the Pin
			String encryptedPin = ms.encryptPin(email, pin);

			HashMap hm = new HashMap();

			int statusCd = 
				this.validateMemberEmailAddress(request, email);

			// Check if valid email address
			if(statusCd == 0)
			{
				// Check if member already signed up with email
				statusCd = 
					this.validateDuplicateMember(request, email);

				if(statusCd == 0)
				{
					// Add the new member
					String mbrSk = 
						this.addNewMember(
							request, 
							email, 
							name,
							encryptedPword, 
							encryptedPin);

					if(Integer.parseInt(mbrSk) > 0)
					{
						// Add a default tray for the member
						MemberTrayService mts = 
							new MemberTrayService();

						mts.addMemberTray(request, mbrSk, "Default");

						// Encrypt the mbrSk
						String mbrSkToken = ms.encryptMbrSk(mbrSk);

						hm.put("statusInd","A");
						hm.put("mbrSkToken",mbrSkToken);
						hm.put("mbrName",name);
					}
					else
					{
						// Display the Error
						hm.put("statusInd","E");
						hm.put("mbrSkToken","");
						hm.put("mbrName","");
					}
				}
				else
				{
					hm.put("statusInd","E");
					hm.put("statusMsg","There already is a member with this same email and password. " +
						"Please choose another email address.");
					hm.put("mbrSkToken","-1");
					hm.put("mbrName","");
				}
			}
			else
			{
				hm.put("statusCd","E");
				hm.put("statusMsg","The entered Email is not a valid Email address.");
				hm.put("mbrSkToken","-1");
				hm.put("mbrName","");
			}

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

	private int validateMemberEmailAddress(
		HttpServletRequest request,
		String argEmail) 
	{
		int statusCd = 0;

		try
		{
			// Check if the email address is a valid one
			Mail m = new Mail();

			statusCd = 
				m.validateEmailAddress(argEmail);
		}
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".validateMemberEmailAddress(): " + ex);
		}

		return statusCd;
	}

	private int validateDuplicateMember(
		HttpServletRequest request,
		String argEmail) 
			throws SQLException 
	{
		Connection con = null;

		int statusCd = 0;

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
			int rowCount = 
				mDAO.selectMemberCount(
					con, 
					argEmail);

			// Member exists already, cannot register the same member twice
			if(rowCount > 0)
			{
				statusCd = -1;
			}
			else
			{
				statusCd = 0;
			}

			if(con != null) con.close();
		}
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".validateDuplicateMember(): " + ex);
		}
		finally
		{
			if(con != null) con.close();
		}

		return statusCd;
	}

	private String addNewMember(
		HttpServletRequest request,
		String argEmail,
		String argName,
		String argEncryptedPword,
		String argEncryptedPin) 
			throws SQLException 
	{
		Connection con = null;

		String mbrSk = "0";

		try
		{
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con = ds.getConnection();

			con.setAutoCommit(false);

			// Prepopulate the profile template with member data
			MemberDAO mDAO = 
				new MemberDAO();

			mbrSk = 
				mDAO.selectMbrSkNextval(con);

			con.commit();

			int statusCd = 
				mDAO.insertMember(
					con, 
					mbrSk,
					argEmail,
					argName,
					argEncryptedPword,
					argEncryptedPin);

			con.commit();

			if(statusCd < 0)
			{
				mbrSk = "-1";
			}

			if(con != null) con.close();
		}
		catch(Exception ex)
		{
			mbrSk = "-1";
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".addNewMember(): " + ex);
		}
		finally
		{
			if(con != null) con.close();
		}

		return mbrSk;
	}

}
