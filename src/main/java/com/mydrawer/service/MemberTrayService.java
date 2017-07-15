package com.mydrawer.service;

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mydrawer.bean.MemberTray;
import com.mydrawer.model.MemberTrayDAO;

public class MemberTrayService
{
	public String getMemberTrayList(
		HttpServletRequest request,
		String argMbrSk)
			throws SQLException 
	{
		Connection con = null;

		String listJson = "";

		try
		{
			MemberTrayDAO cDAO = new MemberTrayDAO();

			// Get the Db connection
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con =  ds.getConnection();

			con.setAutoCommit(false);

			// Get the content to display
			ArrayList list = 
				cDAO.selectMemberTrayList(con, argMbrSk);

			if(con != null) con.close();

			ArrayList hmList = new ArrayList();

			for(int i=0; i<list.size(); i++)
			{
				MemberTray mt = (MemberTray)list.get(i);

				String traSk = mt.getTraSk();
				String mbrSk = mt.getMbrSk();
				String name = mt.getName();

				HashMap hm = new HashMap();
				hm.put("traSk", traSk);
				hm.put("mbrSk", mbrSk);
				hm.put("name", name);

				hmList.add(hm);
			}

			// Convert the hashmap to a JSON string
			JSONArray joPayload = new JSONArray(hmList);
			listJson = joPayload.toString();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".getMemberTrayList(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return listJson;
	}

	public int addMemberTray(
		HttpServletRequest request,
		String argMbrSk,
		String argName)
			throws SQLException 
	{
		Connection con = null;

		int statusCd = 0;

		try
		{
			// Insert into the Content Db
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con =  ds.getConnection();

			con.setAutoCommit(false);

			MemberTrayDAO cDAO = new MemberTrayDAO();

			con.commit();

			statusCd = 
				cDAO.insertMemberTray(
					con, 
					argMbrSk,
					argName);

			con.commit();

			if(con != null) con.close();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".addMemberTray(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return statusCd;
	}

	public int updateMemberTray(
		HttpServletRequest request,
		String argTraSk,
		String argName)
			throws SQLException 
	{
		Connection con = null;

		int statusCd = 0;

		try
		{
			// Insert into the Content Db
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con =  ds.getConnection();

			con.setAutoCommit(false);

			MemberTrayDAO cDAO = new MemberTrayDAO();

			con.commit();

			statusCd = 
				cDAO.updateMemberTray(
					con, 
					argTraSk,
					argName);

			con.commit();

			if(con != null) con.close();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".updateMemberTray(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return statusCd;
	}

	public int deleteMemberTray(
		HttpServletRequest request,
		String argTraSk)
			throws SQLException 
	{
		Connection con = null;

		int statusCd = 0;

		try
		{
			// Insert into the Content Db
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con =  ds.getConnection();

			con.setAutoCommit(false);

			MemberTrayDAO cDAO = new MemberTrayDAO();

			con.commit();

			statusCd = 
				cDAO.deleteMemberTray(
					con, 
					argTraSk);

			con.commit();

			if(con != null) con.close();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".deleteMemberTray(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return statusCd;
	}

}
