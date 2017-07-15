package com.mydrawer.service;

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.json.JSONObject;

import com.mydrawer.bean.MemberDrawer;
import com.mydrawer.model.MemberDrawerDAO;

public class MemberDrawerService
{
	public String getMemberDrawerList(
		HttpServletRequest request,
		String argMbrSk)
			throws SQLException 
	{
		Connection con = null;

		String listJson = "";

		try
		{
			MemberDrawerDAO cDAO = new MemberDrawerDAO();

			// Get the Db connection
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con =  ds.getConnection();

			con.setAutoCommit(false);

			// Get the content to display
			ArrayList list = 
				cDAO.selectMemberDrawerList(con, argMbrSk);

			if(con != null) con.close();

			ArrayList hmList = new ArrayList();

			for(int i=0; i<list.size(); i++)
			{
				MemberDrawer md = (MemberDrawer)list.get(i);

				String drwSk = md.getDrwSk();
				String mbrSk = md.getMbrSk();
				String traSk = md.getTraSk();
				String typeId = md.getTypeId();
				String insertedDt = md.getInsertedDt();
				String updatedDt = md.getUpdatedDt();
				String title = md.getTitle();
				String text = md.getText();
				String url = md.getUrl();

				HashMap hmItems = new HashMap();
				hmItems.put("drwSk", drwSk);
				hmItems.put("mbrSk", mbrSk);
				hmItems.put("traSk", traSk);
				hmItems.put("typeId", typeId);
				hmItems.put("insertedDt", insertedDt);
				hmItems.put("updatedDt", updatedDt);
				hmItems.put("title", title);
				hmItems.put("text", text);
				hmItems.put("url", url);

				hmList.add(hmItems);
			}

			// Convert the hashmap to a JSON string
			JSONObject joPayload = new JSONObject(hmList);
			listJson = joPayload.toString();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".getMemberDrawerList(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return listJson;
	}

	public int addMemberDrawer(
		HttpServletRequest request,
		String argMbrSk,
		String argTraSk,
		String argTypeId,
		String argTitle,
		String argText,
		String argUrl)
			throws SQLException 
	{
		Connection con = null;

		int statusCd = 0;

		try
		{
			String title = "";

			if(argTitle.length() > 100)
			{
				title = argTitle.substring(0, 95) + "...";
			}
			else
			{
				title = argTitle;
			}

			// Insert into the Content Db
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con =  ds.getConnection();

			con.setAutoCommit(false);

			MemberDrawerDAO cDAO = new MemberDrawerDAO();

			con.commit();

			statusCd = 
				cDAO.insertMemberDrawer(
					con, 
					argMbrSk,
					argTraSk,
					argTypeId,
					title,
					argText,
					argUrl);

			con.commit();

			if(con != null) con.close();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".addMemberDrawer(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return statusCd;
	}

	public int updateMemberDrawer(
		HttpServletRequest request,
		String argDwrSk,
		String argTraSk,
		String argTitle,
		String argText,
		String argUrl)
			throws SQLException 
	{
		Connection con = null;

		int statusCd = 0;

		try
		{
			String title = "";

			if(argTitle.length() > 100)
			{
				title = argTitle.substring(0, 95) + "...";
			}
			else
			{
				title = argTitle;
			}

			// Insert into the Content Db
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con =  ds.getConnection();

			con.setAutoCommit(false);

			MemberDrawerDAO cDAO = new MemberDrawerDAO();

			con.commit();

			statusCd = 
				cDAO.updateMemberDrawer(
					con, 
					argDwrSk,
					argTraSk,
					title,
					argText,
					argUrl);

			con.commit();

			if(con != null) con.close();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".updateMemberDrawer(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return statusCd;
	}

}