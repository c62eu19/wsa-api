package com.mydrawer.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.json.JSONObject;

import com.mydrawer.service.MemberDrawerService;

@WebServlet(name = "imageentryws",urlPatterns = {"/imageentryws/*"})
public class ImageEntryWS extends HttpServlet
{
	private static final long serialVersionUID = 2857847752169838915L;

	protected void doPost(
		HttpServletRequest request, 
		HttpServletResponse response) 
			throws ServletException, IOException
	{
		response.setContentType("application/json");

		OutputStream os = null;

		PrintWriter out = response.getWriter();

		try
		{
			String inputJSON = request.getParameter("inputJSON");

			JSONObject jo;
			jo = new JSONObject(inputJSON);
			jo = jo.getJSONObject("inputArgs");

			// Token that identifies a member
			String mbrSk = jo.get("mbrSk").toString();

			String traSk = jo.get("traSk").toString();
			String title = jo.get("title").toString();
			String text = jo.get("text").toString();
			String fileType = jo.get("fileType").toString();
			String base64Code = jo.get("base64Code").toString();

			// Split out the data:image/png;base64, from the base64 String
			String[] base64CommaDelimTokens = base64Code.split(",");
			String base64Identity = base64CommaDelimTokens[0];
			String base64Image = base64CommaDelimTokens[1];

			// Split out the fileType
			String fileExtension = fileType.split("/")[1];

			String seconds = Long.toString(System.currentTimeMillis());

			// Save the file to the server
			String fileName = 
				"picture-" + mbrSk + "-" + seconds + "." + fileExtension;

			// Convert the base64 String to a byte array
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

			String filePath = System.getenv("OPENSHIFT_DATA_DIR") + "media/" + fileName;

			File contentFile = new File(filePath);

			if(!contentFile.exists())
			{
				contentFile.createNewFile();
			}

			os = new FileOutputStream(contentFile, false);
			os.write(imageBytes);
			os.flush();
			os.close();

			String url = "/uploads/media/" + fileName;

			// Media
			String typeId = "3";

			// Add the new post
			MemberDrawerService mds = new MemberDrawerService();

			mds.addMemberDrawer(
				request,
				mbrSk,
				traSk,
				typeId,
				title,
				text,
				url);

			String jsonResponse = 
				mds.getMemberDrawerList(request, mbrSk);

			out.println(jsonResponse);
			out.flush();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".doPost(): " + e);
		}
	}

}
