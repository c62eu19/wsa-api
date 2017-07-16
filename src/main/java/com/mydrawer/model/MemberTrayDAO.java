package com.mydrawer.model;

import java.util.*;
import java.sql.*;

import com.mydrawer.bean.*;

public class MemberTrayDAO 
{
	public ArrayList selectMemberTrayList(
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
				"SELECT tra_sk, " + 
				 "mbr_sk, " + 
				 "name " +
				"FROM member_tray " +
				"WHERE (mbr_sk = CAST(? AS integer)) " +
				"ORDER BY name";

			ps = argCon.prepareStatement(sql);

			ps.setString(1,argMbrSk);

			rs = ps.executeQuery();

			// Process the resultset
			while (rs.next())
			{
				rowCount++;

				String traSk = rs.getString("tra_sk");
				String mbrSk = rs.getString("mbr_sk");
				String name = rs.getString("name");

				MemberTray mt = new MemberTray();

				mt.setTraSk(traSk);
				mt.setMbrSk(mbrSk);
				mt.setName(name);

				list.add(mt);
			}
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".selectMemberTrayList(): " + ex);
		}
		finally
		{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}

		return list;
	}

	public MemberTray selectMemberTray(
		Connection argCon,
		String argTraSk)
			throws SQLException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		MemberTray mt = new MemberTray();

		try
		{
			String sql = 
				"SELECT tra_sk, " + 
				 "mbr_sk, " + 
				 "name " +
				"FROM member_tray " +
				"WHERE (tra_sk = CAST(? AS integer))";

			ps = argCon.prepareStatement(sql);

			ps.setString(1, argTraSk);

			rs = ps.executeQuery();

			// Process the resultset
			while (rs.next())
			{
				String traSk = rs.getString("tra_sk");
				String mbrSk = rs.getString("mbr_sk");
				String name = rs.getString("name");

				mt.setTraSk(traSk);
				mt.setMbrSk(mbrSk);
				mt.setName(name);
			}
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".selectItem(): " + ex);
		}
		finally
		{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}

		return mt;
	}

	public int insertMemberTray(
		Connection argCon, 
		String argMbrSk,
		String argName)
			throws SQLException
	{
		PreparedStatement ps = null;

		int statusCd = 0;

		try
		{
			String sql = 
				"INSERT INTO member_tray " +
				"(mbr_sk, " +
				 "name) " +
				"VALUES " + 
				"(CAST(? AS integer), " +
				 "?)";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argMbrSk);
			ps.setString(2, argName);

			ps.executeUpdate();
		} 
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".insertMemberTray(): " + ex);
		}
		finally
		{
			if (ps != null) ps.close();
		}

		return statusCd;
	}

	public int updateMemberTray(
		Connection argCon, 
		String argTraSk,
		String argName)
			throws SQLException
	{
		PreparedStatement ps = null;

		int statusCd = 0;

		try
		{
			String sql = 
				"UPDATE member_tray " +
				"SET name = ? " +
				"WHERE (tra_sk = CAST(? AS integer))";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argName);
			ps.setString(2, argTraSk);

			ps.executeUpdate();
		} 
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".updateMemberTray(): " + ex);
		}
		finally
		{
			if (ps != null) ps.close();
		}
			return statusCd;
	}

	public int deleteMemberTray(
		Connection argCon, 
		String argTraSk)
			throws SQLException
	{
		PreparedStatement ps = null;

		int statusCd = 0;

		try
		{
			String sql = 
				"DELETE FROM member_tray " +
				"WHERE (tra_sk = CAST(? AS integer))";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argTraSk);

			ps.executeUpdate();
		} 
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".deleteMemberTray(): " + ex);
		}
		finally
		{
			if (ps != null) ps.close();
		}
			return statusCd;
	}

}
