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

import com.mydrawer.bean.MemberDrawer;
import com.mydrawer.model.MemberDrawerDAO;

public class MemberDrawerService
{
	public String getMemberDrawerListByMbrSk(
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
				cDAO.selectMemberDrawerListByMbrSk(con, argMbrSk);

			if(con != null) con.close();

			listJson = 
				getMemberDrawerList(list);
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".getMemberDrawerListByMbrSk(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return listJson;
	}

	public String getMemberDrawerListByWildcard(
		HttpServletRequest request,
		String argMbrSk,
		String argSearchTerm)
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
				cDAO.selectMemberDrawerListByWildcard(con, argMbrSk, argSearchTerm);

			if(con != null) con.close();

			listJson = 
					getMemberDrawerList(list);
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".getMemberDrawerListByWildcard(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return listJson;
	}

	private String getMemberDrawerList(ArrayList argList)
		throws Exception 
	{
		String listJson = "";

		try
		{
			ArrayList hmList = new ArrayList();

			for(int i=0; i<argList.size(); i++)
			{
				MemberDrawer md = (MemberDrawer)argList.get(i);

				String drwSk = md.getDrwSk();
				String mbrSk = md.getMbrSk();
				String traSk = md.getTraSk();
				String typeId = md.getTypeId();
				String insertedDt = md.getInsertedDt();
				String updatedDt = md.getUpdatedDt();
				String title = md.getTitle();
				String text = md.getText();
				String url = md.getUrl();
				String traName = md.getTraName();

				String abbrTitle = "";

				if(title.length() > 100)
				{
					abbrTitle = title.substring(0, 95) + "...";
				}
				else
				{
					abbrTitle = title;
				}

				String abbrText = "";

				if(text.length() > 100)
				{
					abbrText = text.substring(0, 95) + "...";
				}
				else
				{
					abbrText = text;
				}

				HashMap hm = new HashMap();
				hm.put("drwSk", drwSk);
				hm.put("mbrSk", mbrSk);
				hm.put("traSk", traSk);
				hm.put("typeId", typeId);
				hm.put("insertedDt", insertedDt);
				hm.put("updatedDt", updatedDt);
				hm.put("title", abbrTitle);
				hm.put("abbrText", abbrText);
				hm.put("text", text);
				hm.put("url", url);
				hm.put("traName", traName);

				hmList.add(hm);
			}

			// Convert the hashmap to a JSON string
			JSONArray joPayload = new JSONArray(hmList);
			listJson = joPayload.toString();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".getMemberDrawerList(): " + e);
		}
		finally
		{
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
					argTitle,
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
		String argDrwSk,
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
			// Insert into the Content Db
			Context initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:jboss/datasources/PostgreSQLDS");
			con =  ds.getConnection();

			con.setAutoCommit(false);

			MemberDrawerDAO cDAO = new MemberDrawerDAO();

			con.commit();

			System.out.println(argDrwSk + "-" + argTraSk);

			statusCd = 
				cDAO.updateMemberDrawer(
					con, 
					argDrwSk,
					argTraSk,
					argTitle,
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

	public int deleteMemberDrawer(
		HttpServletRequest request,
		String argDrwSk)
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

			MemberDrawerDAO cDAO = new MemberDrawerDAO();

			con.commit();

			statusCd = 
				cDAO.deleteMemberDrawer(con, argDrwSk);

			con.commit();

			if(con != null) con.close();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".deleteMemberDrawer(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}

		return statusCd;
	}

}
