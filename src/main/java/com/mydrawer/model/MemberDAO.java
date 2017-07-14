package com.mydrawer.model;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.sql.*;

import com.mydrawer.bean.*;

public class MemberDAO 
{
	public Member selectMemberSignin(
		Connection argCon, 
		String argEmail,
		String argEncryptedPword)
			throws SQLException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rowCount = 0;

		Member member = new Member();

		try
		{
			String sql = 
				"SELECT mbr_sk, " + 
				 "email, " +
				 "name," +
				 "status_ind " +
				"FROM member " + 
				"WHERE (email = ?) AND " +
				 "(password = ?)";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argEmail);
			ps.setString(2, argEncryptedPword);

			rs = ps.executeQuery();

			// Process the resultset
			while (rs.next())
			{
				rowCount++;

				String tMbrSk = rs.getString("mbr_sk");
				String tEmail = rs.getString("email");
				String tName = rs.getString("name");
				String tStatusInd = rs.getString("status_ind");

				if(tMbrSk == null) tMbrSk = "0";
				if(tEmail == null) tEmail = "";
				if(tName == null) tName = "";
				if(tStatusInd == null) tStatusInd = "N";

				member.setMbrSk(tMbrSk);
				member.setEmail(tEmail);
				member.setName(tName);
				member.setStatusInd(tStatusInd);
			}
		}
		catch(Exception ex)
		{
			member.setMbrSk("-1");
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".selectMemberSignin(): " + ex);
		}
		finally
		{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}

		if(rowCount <= 0)
		{
			member.setMbrSk("-1");
		}

		return member;
	}

	public Member selectMemberSignin(
		Connection argCon, 
		String argMbrSk)
			throws SQLException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rowCount = 0;

		Member member = new Member();

		try
		{
			String sql = 
				"SELECT mbr_sk, " + 
				 "email," +
				 "name," +
				 "status_ind " +
				"FROM member " + 
				"WHERE (mbr_sk = CAST(? AS integer))";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argMbrSk);

			rs = ps.executeQuery();

			// Process the resultset
			while (rs.next())
			{
				rowCount++;

				String tMbrSk = rs.getString("mbr_sk");
				String tEmail = rs.getString("email");
				String tName = rs.getString("name");
				String tStatusInd = rs.getString("status_ind");

				if(tMbrSk == null) tMbrSk = "0";
				if(tEmail == null) tEmail = "";
				if(tName == null) tName = "";
				if(tStatusInd == null) tStatusInd = "N";

				member.setMbrSk(tMbrSk);
				member.setEmail(tEmail);
				member.setName(tName);
				member.setStatusInd(tStatusInd);
			}
		}
		catch(Exception ex)
		{
			member.setMbrSk("-1");
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".selectMemberSignin(): " + ex);
		}
		finally
		{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}

		if(rowCount <= 0)
		{
			member.setMbrSk("-1");
		}

		return member;
	}

	public int updateMemberSignin(
		Connection argCon, 
		String argMbrSk) 
			throws SQLException
	{
		PreparedStatement ps = null;

		int statusCd = 0;

		try
		{
			String sql = 
				"UPDATE member " +
				"SET last_login_dt = CURRENT_TIMESTAMP, " +
				 "login_cnt = login_cnt + 1 " +
				"WHERE (mbr_sk = CAST(? AS integer))";

			ps = argCon.prepareStatement(sql);

			// Set the parms")
			ps.setString(1, argMbrSk);

			ps.executeUpdate();
		} 
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".updateMemberSignin(): " + ex);
		}
		finally
		{
			if (ps != null) ps.close();
		}

		return statusCd;
	}

	public Member selectMember(
		Connection argCon, 
		String argMbrSk)
			throws SQLException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rowCount = 0;

		Member member = new Member();

		try
		{
			String sql = 
				"SELECT mbr_sk, " + 
				 "email," +
				 "name," +
				 "password, " +
				 "coalesce(to_char(registration_dt, 'MM-DD-YYYY HH24:MI:SS'), '') as registration_dt, " +
				 "coalesce(to_char(last_login_dt, 'MM-DD-YYYY HH24:MI:SS'), '') as last_login_dt, " +
				 "login_cnt, " +
				 "status_ind " +
				"FROM member " + 
				"WHERE (mbr_sk = CAST(? AS integer))";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argMbrSk);

			rs = ps.executeQuery();

			// Process the resultset
			while (rs.next())
			{
				rowCount++;

				String tMbrSk = rs.getString("mbr_sk");
				String tEmail = rs.getString("email");
				String tName = rs.getString("name");
				String tRegistrationDt = rs.getString("registration_dt");
				String tLastLoginDt = rs.getString("last_login_dt");
				String tLoginCnt = rs.getString("login_cnt");
				String tStatusInd = rs.getString("status_ind");

				if(tMbrSk == null) tMbrSk = "0";
				if(tEmail == null) tEmail = "";
				if(tName == null) tName = "";
				if(tRegistrationDt == null) tRegistrationDt = "";
				if(tLastLoginDt == null) tLastLoginDt = "";
				if(tLoginCnt == null) tLoginCnt = "";
				if(tStatusInd == null) tStatusInd = "N";

				member.setMbrSk(tMbrSk);
				member.setEmail(tEmail);
				member.setName(tName);
				member.setRegistrationDt(tRegistrationDt);
				member.setLastLoginDt(tLastLoginDt);
				member.setLoginCnt(tLoginCnt);
				member.setStatusInd(tStatusInd);
			}
		}
		catch(Exception ex)
		{
			member.setMbrSk("-1");
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".selectMember(): " + ex);
		}
		finally
		{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}

		if(rowCount <= 0)
		{
			member.setMbrSk("-1");
		}

		return member;
	}

	public ArrayList selectMemberList(Connection argCon)
		throws SQLException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rowCount = 0;

		ArrayList list = new ArrayList();

		try
		{
			String sql = 
				"SELECT mbr_sk, " + 
				 "email," +
				 "name," +
				 "password, " +
				 "last_login_dt as sort_dt, " +
				 "coalesce(replace(replace(to_char((registration_dt::timestamptz at time zone 'EST5EDT')::timestamptz, 'MM/DD/YYYY HH12:MI:SSAM TZ'), 'CST', 'EST'), 'CDT', 'EDT'), '') as registration_dt, " +
				 "coalesce(replace(replace(to_char((last_login_dt::timestamptz at time zone 'EST5EDT')::timestamptz, 'MM/DD/YYYY HH12:MI:SSAM TZ'), 'CST', 'EST'), 'CDT', 'EDT'), '') as last_login_dt, " +
				 "login_cnt, " +
				 "status_ind " +
				"FROM member " + 
				"WHERE (mbr_sk > 0)" +
				"ORDER BY sort_dt DESC";

			ps = argCon.prepareStatement(sql);

			// Set the parms
//			ps.setString(1, argMbrSk);

			rs = ps.executeQuery();

			// Process the resultset
			while (rs.next())
			{
				rowCount++;

				String tMbrSk = rs.getString("mbr_sk");
				String tEmail = rs.getString("email");
				String tName = rs.getString("name");

				String tRegistrationDt = rs.getString("registration_dt");
				String tLastLoginDt = rs.getString("last_login_dt");
				String tLoginCnt = rs.getString("login_cnt");
				String tStatusInd = rs.getString("status_ind");

				if(tMbrSk == null) tMbrSk = "";
				if(tEmail == null) tEmail = "";
				if(tName == null) tName = "";

				if(tRegistrationDt == null) tRegistrationDt = "";
				if(tLastLoginDt == null) tLastLoginDt = "";
				if(tLoginCnt == null) tLoginCnt = "";
				if(tStatusInd == null) tStatusInd = "";

				Member member = new Member();

				member.setMbrSk(tMbrSk);
				member.setEmail(tEmail);
				member.setName(tName);
				member.setStatusInd(tStatusInd);

				member.setRegistrationDt(tRegistrationDt);
				member.setLastLoginDt(tLastLoginDt);
				member.setLoginCnt(tLoginCnt);

				list.add(member);
			}
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".selectMemberList(): " + ex);
		}
		finally
		{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}

		return list;
	}

	public int selectMemberCount(
		Connection argCon,
		String argEmail) 
			throws SQLException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rowCount = 0;

		try
		{
			String sql = 
				"SELECT count(*) as row_count " +
				"FROM member " +
				"WHERE (email = ?)";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argEmail);

			rs = ps.executeQuery();

			// Process the resultset
			while (rs.next())
			{
				rowCount = rs.getInt("row_count");
			}
		}
		catch(Exception ex)
		{
			rowCount = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".selectMemberCount(): " + ex);
		}
		finally
		{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}

		return rowCount;
	}

	public String selectMbrSkNextval(Connection argCon)
		throws SQLException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String mbrSkNextval = "0";

		try
		{
			String sql = 
				"SELECT nextval('mbr_sk_seq') as mbrSkNextval";

			ps = argCon.prepareStatement(sql);

			rs = ps.executeQuery();

			// Process the resultset
			while (rs.next())
			{
				mbrSkNextval = rs.getString("mbrSkNextval");
			}
		}
		catch(Exception ex)
		{
			mbrSkNextval = "0";
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".selectMbrSkNextval(): " + ex);
		}
		finally
		{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}

		return mbrSkNextval;
	}

	public int insertMember(
		Connection argCon, 
		String argMbrSk,
		String argEmail,
		String argPassword,
		String argName,
		String argPin) 
			throws SQLException
	{
		PreparedStatement ps = null;

		int statusCd = 0;

		try
		{
			String sql = 
				"INSERT INTO member " +
				"(mbr_sk, " +
				 "email, " +
				 "name, " +
				 "password, " +
				 "pin, " +
				 "registration_dt, " +
				 "last_login_dt, " +
				 "login_cnt, " +
				 "status_ind) " +
				"VALUES " + 
				 "(CAST(? AS integer), " + 
				  "?, " +
				  "?, " +
				  "?, " +
				  "?, " + 
				  "CURRENT_TIMESTAMP, " + 
				  "CURRENT_TIMESTAMP, " +
				  "'0', " + 
				  "'A')";

			ps = argCon.prepareStatement(sql);

			// Set the parms
			ps.setString(1, argMbrSk);
			ps.setString(2, argEmail);
			ps.setString(3, argName);
			ps.setString(4, argPassword);
			ps.setString(5, argPin);

			ps.executeUpdate();
		} 
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".insertMember(): " + ex);
		}
		finally
		{
			if(ps != null) ps.close();
		}

		return statusCd;
	}

	public int updateMemberPassword(
		Connection argCon, 
		String argMbrSk,
		String argEncryptedPassword) 
			throws SQLException
	{
		PreparedStatement ps = null;

		int statusCd = 0;

		try
		{
			String sql = 
				"UPDATE member " +
				"SET password = ? " +
				"WHERE (mbr_sk = CAST(? AS integer))";

			ps = argCon.prepareStatement(sql);

			// Set the parms")
			ps.setString(1, argEncryptedPassword);
			ps.setString(2, argMbrSk);

			ps.executeUpdate();
		} 
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".updateMemberPassword(): " + ex);
		}
		finally
		{
			if (ps != null) ps.close();
		}

		return statusCd;
	}

	public int updateMemberStatusInd(
		Connection argCon, 
		String argMbrSk,
		String argStatusInd) 
			throws SQLException
	{
		PreparedStatement ps = null;

		int statusCd = 0;

		try
		{
			String sql = 
				"UPDATE member " +
				"SET status_ind = ? " +
				"WHERE (mbr_sk = CAST(? AS integer))";

			ps = argCon.prepareStatement(sql);

			// Set the parms")
			ps.setString(1, argStatusInd);
			ps.setString(2, argMbrSk);

			ps.executeUpdate();
		} 
		catch(Exception ex)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".updateMemberStatusInd(): " + ex);
		}
		finally
		{
			if (ps != null) ps.close();
		}

		return statusCd;
	}

	public static void main(String[] args) 
		throws SQLException, IOException
	{
		Connection con = null;

		try
		{
			MemberDAO js = new MemberDAO();

			String url = "";
			url = "jdbc:postgresql://127.0.0.1:5432/life?user=admin31j4lw2&password=VZ__a4fqBVHX";
			con = DriverManager.getConnection(url);

		    // Load the JDBC driver
			Class.forName("oracle.jdbc.driver.OracleDriver");

//		    con = DriverManager.getConnection(url, "imblbch", "kw20tn8");

		}
		catch(Exception e)
		{
System.out.println("main(): " + e);
		}
		finally
		{
			if(con != null) con.close();
		}
	}

}
