package com.mydrawer.service;

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.mydrawer.model.*;
import com.mydrawer.utility.*;

public class MemberService
{
	public String encryptPword(
		String argEmail,
		String argPassword) 
			throws Exception 
	{
		String encryptedPword = "";

		try
		{
			Security s = new Security();

			// Salt the password before encrypting
			String saltedPword = argEmail.trim() + s.getSalt() + argPassword.trim();
			encryptedPword = s.encrypt(saltedPword);
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".encryptPword(): " + ex);
		}
		finally
		{
		}

		return encryptedPword;
	}

	public String encryptPin(
		String argEmail,
		String argPin) 
			throws Exception 
	{
		String encryptedPin = "";

		try
		{
			Security s = new Security();

			// Salt the pin before encrypting
			String saltedPin = argEmail.trim() + s.getSalt() + argPin.trim();
			encryptedPin = s.encrypt(saltedPin);
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".encryptPin(): " + ex);
		}
		finally
		{
		}

		return encryptedPin;
	}

	public String encryptMbrSk(
		String argEmail,
		String argMbrSk) 
			throws Exception 
	{
		String encryptedMbrSk = "";

		try
		{
			Security s = new Security();

			// Salt the mbrSk before encrypting
			String saltedMbrSk = argEmail.trim() + s.getSalt() + argMbrSk.trim();
			encryptedMbrSk = s.encrypt(saltedMbrSk);
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".encryptMbrSk(): " + ex);
		}
		finally
		{
		}

		return encryptedMbrSk;
	}

	public String decryptMbrSk(String argEncryptedMbrSk) 
		throws Exception 
	{
		String mbrSk = "";

		try
		{
			Security s = new Security();

			String decryptedMbrSk = s.decrypt(argEncryptedMbrSk);

System.out.println(argEncryptedMbrSk);
System.out.println(decryptedMbrSk);

			// Parse out the mbrSk from decrypted token
			String[] mbrSkTokens = decryptedMbrSk.split("[|]");
			String email = mbrSkTokens[0];
			String salt = mbrSkTokens[1];
			
			mbrSk = mbrSkTokens[2];
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".decryptMbrSk(): " + ex);
		}
		finally
		{
		}

		return mbrSk;
	}

	public String updateMemberStatusInd(
		HttpServletRequest request,
		String argMbrSk,
		String argStatusInd,
		String argMbrEmail) 
			throws SQLException 
	{
		Connection con = null;

		String htmlContent = "";

		try
		{
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con = ds.getConnection();

			con.setAutoCommit(false);

			MemberDAO mDAO = new MemberDAO();

			int statusCd = 
				mDAO.updateMemberStatusInd(
					con, 
					argMbrSk,
					argStatusInd);

			con.commit();

			if(con != null) con.close();
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".updateMemberStatusInd(): " + ex);
		}
		finally
		{
			if(con != null) con.close();
		}

		return htmlContent;
	}


}
