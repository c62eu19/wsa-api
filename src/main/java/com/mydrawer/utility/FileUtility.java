package com.mydrawer.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class FileUtility
{
	public Properties readPropFile(String argPropFile) 
		throws Exception 
	{
		FileInputStream fis = null; 

		Properties prop = new Properties();

		try
		{
			String pagesFile = 
				System.getenv("OPENSHIFT_DATA_DIR") + argPropFile;

			fis = new FileInputStream(pagesFile);
			prop.load(fis);    
			fis.close();
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".readPropFile(): " + e);
		}
		finally
		{
			fis.close();
		}

		return prop;
	}

	public String readFile(File argFile) 
		throws Exception 
	{
		InputStream is = null; 
		BufferedReader br = null;

		String output = "";

		try
		{
			is = new FileInputStream(argFile);

			br = new BufferedReader(new InputStreamReader(is));

			String line = "";
			while((line = br.readLine()) != null)
			{
				output += line;
			}
		}
		catch(Exception e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".readFile(): " + e);
		}
		finally
		{
			is.close();
			br.close();
		}

		return output;
	}

}
