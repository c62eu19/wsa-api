package com.story.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
			fis = new FileInputStream(argPropFile);
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

	public String readDirectoryOfFiles(String argDir)
	{
		String output = "";

		Path dir = Paths.get(argDir);

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir))
		{
			for (Path file: stream)
			{
				output += readFile(file.toFile());
		    }
		}
		catch (IOException | DirectoryIteratorException e)
		{
			System.out.println(
				"EXCEPTION: " + this.getClass().getName() + ".readDirectoryOfFiles(): " + e);
		}

		return output;
	}

	public String readFile(File argFile) 
		throws IOException 
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
				output += line.trim();
			}
		}
		catch(IOException e)
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

	public String getFileExtension(File argFile)
	{
		String fileExt = "";

		try
		{
			// Get file Name first
			String fileName = argFile.getName();

			// If fileName do not contain "." or starts with "." then it is not a valid file
			if(fileName.contains(".") && fileName.lastIndexOf(".")!= 0)
			{
				fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
			}
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".getFileExtension(): " + e);
		}
		finally
		{
		}

		return fileExt;
	}

}
