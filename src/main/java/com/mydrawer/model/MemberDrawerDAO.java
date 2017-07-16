package com.mydrawer.model;

import java.util.*;
import java.sql.*;

import com.mydrawer.bean.*;

public class MemberDrawerDAO 
{
	public ArrayList selectMemberDrawerList(
		Connection argCon,
		String argMbrSk)
			throws SQLException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rowCount = 0;

		ArrayList list = new ArrayList();

		try
		{
			String sql = 
				"SELECT a.drw_sk, " +
				 "a.mbr_sk, " + 
				 "a.tra_sk, " + 
				 "to_char(a.inserted_dt, 'MM/DD/YYYY') as inserted_dt, " +
				 "to_char(a.updated_dt, 'MM/DD/YYYY') as updated_dt, " +
				 "a.type_id, " + 
				 "a.title, " + 
				 "a.text, " + 
				 "a.url, " +
				 "b.name " +
				"FROM member_drawer a, " +
				 "member_tray b " +
				"WHERE (a.mbr_sk = CAST(? AS integer)) AND " +
				 "(a.tra_sk = b.tra_sk) " +
				"ORDER BY b.name";

			ps = argCon.prepareStatement(sql);

			ps.setString(1,argMbrSk);

			rs = ps.executeQuery();

			// Process the resultset
			while (rs.next())
			{
				rowCount++;

				String drwSk = rs.getString("drw_sk");
				String mbrSk = rs.getString("mbr_sk");
				String traSk = rs.getString("tra_sk");
				String insertedDt = rs.getString("inserted_dt");
				String updatedDt = rs.getString("updated_dt");
				String typeId = rs.getString("type_id");
				String title = rs.getString("title");
				String text = rs.getString("text");
				String url = rs.getString("url");
				String name = rs.getString("name");

				MemberDrawer md = new MemberDrawer();

				md.setDrwSk(traSk);
				md.setMbrSk(mbrSk);
				md.setTraSk(traSk);
				md.setInsertedDt(insertedDt);
				md.setUpdatedDt(updatedDt);
				md.setTypeId(typeId);
				md.setTitle(title);
				md.setText(text);
				md.setUrl(url);

				md.setTraName(name);

				list.add(md);
			}
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".selectMemberDrawerList(): " + ex);
		}
		finally
		{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}

		return list;
	}

	public MemberDrawer selectMemberDrawer(
		Connection argCon,
		String argTraSk)
			throws SQLException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		MemberDrawer md = new MemberDrawer();

		try
		{
			String sql = 
				"SELECT a.drw_sk, " +
				 "a.mbr_sk, " + 
				 "tra_sk, " + 
				 "to_char(a.inserted_dt, 'MM/DD/YYYY') as inserted_dt, " +
				 "to_char(a.updated_dt, 'MM/DD/YYYY') as updated_dt, " +
				 "a.type_id, " + 
				 "a.title, " + 
				 "a.text, " + 
				 "a.url, " +
				 "b.name " +
				"FROM member_drawer a, " +
				 "member_tray b " +
				"WHERE (tra_sk = CAST(? AS integer)) AND " +
				 "a.tra_sk = b.tra_sk)";

			ps = argCon.prepareStatement(sql);

			ps.setString(1, argTraSk);

			rs = ps.executeQuery();

			// Process the resultset
			while (rs.next())
			{
				String drwSk = rs.getString("drw_sk");
				String mbrSk = rs.getString("mbr_sk");
				String traSk = rs.getString("tra_sk");
				String insertedDt = rs.getString("inserted_dt");
				String updatedDt = rs.getString("updated_dt");
				String typeId = rs.getString("type_id");
				String title = rs.getString("title");
				String text = rs.getString("text");
				String url = rs.getString("url");
				String name = rs.getString("name");

				md.setDrwSk(traSk);
				md.setMbrSk(mbrSk);
				md.setTraSk(traSk);
				md.setInsertedDt(insertedDt);
				md.setUpdatedDt(updatedDt);
				md.setTypeId(typeId);
				md.setTitle(title);
				md.setText(text);
				md.setUrl(url);

				md.setTraName(name);
			}
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".selectMemberDrawer(): " + ex);
		}
		finally
		{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}

		return md;
	}

	public int insertMemberDrawer(
		Connection argCon, 
		String argMbrSk,
		String argTraSk,
		String argTypeId,
		String argTitle,
		String argText,
		String argUrl)
			throws SQLException
	{
		PreparedStatement ps = null;

		int statusCd = 0;

		try
		{
			String sql = 
				"INSERT INTO member_drawer " +
				"(mbr_sk, " +
				 "tra_sk, " +
				 "inserted_dt, " +
				 "updated_dt, " +
				 "type_id, " +
				 "title, " +
				 "text, " +
				 "url) " +
				"VALUES " + 
				 "(CAST(? AS integer), " +
				 "(CAST(? AS integer), " +
				 "CURRENT_TIMESTAMP, " + 
				 "CURRENT_TIMESTAMP, " +
				 "(CAST(? AS integer), " +
				 "?, " +
				 "?, " +
				 "?)";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argMbrSk);
			ps.setString(2, argTraSk);
			ps.setString(3, argTypeId);
			ps.setString(4, argTitle);
			ps.setString(5, argText);
			ps.setString(6, argUrl);

			ps.executeUpdate();
		} 
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".insertMemberDrawer(): " + ex);
		}
		finally
		{
			if (ps != null) ps.close();
		}

		return statusCd;
	}

	public int updateMemberDrawer(
		Connection argCon, 
		String argDrwSk,
		String argTraSk,
		String argTitle,
		String argText,
		String argUrl)
			throws SQLException
	{
		PreparedStatement ps = null;

		int statusCd = 0;

		try
		{
			String sql = 
				"UPDATE member_drawer " +
				"SET tra_sk = CAST(? AS integer), " +
				 "title = ?, " +
				 "text = ?, " +
				 "url = ? " +
				"WHERE (drw_sk = CAST(? AS integer))";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argTraSk);
			ps.setString(2, argTitle);
			ps.setString(3, argText);
			ps.setString(4, argUrl);
			ps.setString(5, argDrwSk);

			ps.executeUpdate();
		} 
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".updateMemberDrawer(): " + ex);
		}
		finally
		{
			if (ps != null) ps.close();
		}
			return statusCd;
	}

	public int deleteMemberDrawer(
		Connection argCon, 
		String argDrwSk)
			throws SQLException
	{
		PreparedStatement ps = null;

		int statusCd = 0;

		try
		{
			String sql = 
				"DELETE FROM member_drawer " +
				"WHERE (drw_sk = CAST(? AS integer))";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argDrwSk);

			ps.executeUpdate();
		} 
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".deleteMemberDrawer(): " + ex);
		}
		finally
		{
			if (ps != null) ps.close();
		}
			return statusCd;
	}

}
