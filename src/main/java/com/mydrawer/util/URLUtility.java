package com.mydrawer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mydrawer.obj.Media;

public class URLUtility
{
	public HashMap parseUrlPageHeaderInfo(String argUrl) 
		throws Exception
	{
		HashMap<String,String>hm = new HashMap<String,String>();

		try
		{
			Connection con = Jsoup.connect(argUrl);

			Document doc = con.get();

			// Different Formats [meta]
			// title, description, image
			// og:title, og:description, og:image

			String title = "";
			Elements metaTitle;

			metaTitle = doc.select("meta[property=og:title]");

			if(metaTitle.isEmpty())
			{
				metaTitle = doc.select("meta[name=dcterms:title]");

				if(metaTitle.isEmpty())
				{
					title = doc.title();
				}
				else
				{
					title = metaTitle.attr("content");
				}
			}
			else
			{
				title = metaTitle.attr("content");
			}

//System.out.println("TITLE: " + title);

			String descr = "";
			Elements metaDescr;

			metaDescr = doc.select("meta[property=og:description]");

			if(metaDescr.isEmpty())
			{
				metaDescr = doc.select("meta[name=description]");

				if(metaDescr.isEmpty())
				{
					descr = "";
				}
				else
				{
					descr = metaDescr.attr("content");
				}
			}
			else
			{
				descr = metaDescr.attr("content");
			}

//System.out.println("DESCR: " + descr);

			String imageUrl = "";
			Elements metaImage;

			metaImage = doc.select("meta[property=og:image]");

			if(metaImage.isEmpty())
			{
				metaImage = doc.select("link[rel=image_src]");

				if(metaImage.isEmpty())
				{
					imageUrl = "";
				}
				else
				{
					imageUrl = metaImage.attr("href");
				}
			}
			else
			{
				imageUrl = metaImage.attr("content");
			}

//System.out.println("IMAGE: " + imageUrl);

			// Always do the null check
			if(imageUrl == null) imageUrl = "";
			if(title == null) title = "";
			if(descr == null) descr = "";

			if(!title.equals(""))
			{
				// Remove single and back quotes
				title = title.replaceAll("\u2018", "");
				title = title.replace("\u2019", "");
				title = title.replaceAll("'", " ");
				title = title.replaceAll("\"", "");
			}

			if(!descr.equals(""))
			{
				// Remove single and back quotes
				descr = descr.replaceAll("\u2018", "");
				descr = descr.replace("\u2019", "");
				descr = descr.replaceAll("'", " ");
				descr = descr.replaceAll("\"", " ");
			}

			// If no image from the web page use the stock one
			if(imageUrl.equals(""))
			{
				imageUrl = "/images/topic-article.png";
			}

			hm.put("statusCd", "0");
			hm.put("statusMsg", "");
			hm.put("mediaType", "article");
			hm.put("imageUrl", imageUrl);
			hm.put("title", title);
			hm.put("descr", descr);
		}
		catch(Exception e)
		{
			hm.put("statusCd", "-1");
			hm.put("statusMsg", "The Link was not found.");
			hm.put("imageUrl", "");
			hm.put("title", "");
			hm.put("descr", "");

			System.out.println("EXCEPTION: " + this.getClass().getName() + ".parseUrlPageHeaderInfo(): " + e);
		}

		return hm;
	}

	public String getUrlComponent(
		String argUrl,
		String argComponent) 
			throws Exception
	{
		String urlComponent = "";

		try
		{
			URL aURL = new URL(argUrl);

			if(argComponent.equalsIgnoreCase("protocol")) urlComponent = aURL.getProtocol();
			if(argComponent.equalsIgnoreCase("authority")) urlComponent = aURL.getAuthority();
			if(argComponent.equalsIgnoreCase("host")) urlComponent = aURL.getHost();
			if(argComponent.equalsIgnoreCase("port")) urlComponent = Integer.toString(aURL.getPort());
			if(argComponent.equalsIgnoreCase("path")) urlComponent = aURL.getPath();
			if(argComponent.equalsIgnoreCase("query")) urlComponent = aURL.getQuery();
			if(argComponent.equalsIgnoreCase("filename")) urlComponent = aURL.getFile();
			if(argComponent.equalsIgnoreCase("ref")) urlComponent = aURL.getRef();

			if(urlComponent == null) urlComponent = "";
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".getUrlComponent(): " + e);
		}

		return urlComponent;
	}

	public static String getContentType(String filename)
    {
        String g = URLConnection.guessContentTypeFromName(filename);
        if( g == null)
        {
            g = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(filename);
        }
 
        return g;
    }

	public static void main(String[] args) throws Exception
	{
		URLUtility r = new URLUtility();

/*		String link = "https://vimeo.com/88268560";
		String protocol = r.getUrlComponent(link, "protocol");
		String host = r.getUrlComponent(link, "host");
		String query = r.getUrlComponent(link, "query");
		String path = r.getUrlComponent(link, "path");
System.out.println(protocol);
System.out.println(host);
System.out.println(query);
System.out.println(path);
System.out.println(protocol + "://player.vimeo.com/video" + path);

//src="https://embed-ssl.ted.com/talks/annie_murphy_paul_what_we_learn_before_we_re_born.html" 
*/

		String url = 
			//"http://www.chop.edu/news/strep-throat-do-you-really-know-symptoms?utm_source=philly.com&utm_medium=sponsored%20content&utm_term=strep&utm_content=signs%20of%20strep%20throat&utm_campaign=philly.com%20health%20tips%202016&strref=http%3A%2F%2Fwww.philly.com#.V8X_YpgrLIU"
			//"http://www.foxnews.com/health/2016/08/30/drug-quickly-counteracts-deadly-bleeding-in-people-on-blood-thinners.html"
			"https://api.fda.gov/drug/label.json?search=brand_name:tylenol+generic_name:tylenol&limit=100"
			//"https://wsearch.nlm.nih.gov/ws/query?db=healthTopics&term=neck&rettype=all";

			//"https://www.youtube.com/watch?v=4Jej2a6SjkY"
			//"http://www.tgsus.com/executive-search/executive-search-for-u-s-president/"
			;

		PrintWriter pw = new PrintWriter(new File("c:/App-Data/FDA.txt"));

		//String tt = r.readUrlToString(url, "JSON");

//JSONObject joRoot = new JSONObject(tt);

//JSONObject joMeta = joRoot.getJSONObject("meta");

//JSONObject joMetaResults = joMeta.getJSONObject("results");

// Get the JSONObject value associated with the search result key.
//JSONArray jaResults = joRoot.getJSONArray("results");
//System.out.println(jaResults.toString());

// Get the number of search results in this set
/*int resultCount = jaResults.length();
System.out.println(resultCount);

// Loop over each result and print the title, summary, and URL
for(int i=0; i<resultCount; i++)
{
	JSONObject resultsObject = jaResults.getJSONObject(i);

	if(resultsObject.has("effective_time"))
	{
		if(resultsObject.get("effective_time") != null)
			pw.println(resultsObject.get("effective_time").toString());
	}
	if(resultsObject.has("id"))
	{
		if(resultsObject.get("id") != null)
			pw.println(resultsObject.get("id").toString());
	}

	// OpenFda Inner Loop
	if(resultsObject.has("openfda"))
	{
		JSONObject joOpenFda = resultsObject.getJSONObject("openfda");

		for(int x=0; x<joOpenFda.getJSONArray("brand_name").length(); x++)
		{
			pw.println(joOpenFda.getJSONArray("brand_name").get(x));
		}
	}
	
	pw.println(resultsObject.toString());

	JSONObject joTest = new JSONObject(resultsObject.toString());
	for(int x=0; x<joTest.getJSONArray("active_ingredient").length(); x++)
	{
		System.out.println(joTest.getJSONArray("active_ingredient").get(x));
	}

}

/*
		HashMap hm =
			r.parseUrlPageHeaderInfo(url);

		String statusCd = (String)hm.get("statusCd");
		String statusMsg = (String)hm.get("statusMsg");

		String imageUrl = (String)hm.get("imageUrl");
		String title = (String)hm.get("title");
		String descr = (String)hm.get("descr");

System.out.println(title);
System.out.println(descr);

		if(statusCd.equals("0"))
		{
			HTMLUtility h = new HTMLUtility();

			String mediaHtml = 
					h.buildLinkContentHtml(
							url,
							imageUrl,
							title,
							descr);

			System.out.println(mediaHtml);
		}

		String host = r.getUrlComponent(url, "host");

		if(host.startsWith("www."))
		{
			host = host.substring(4);
		}
		System.out.println(host);
		System.out.println(statusMsg);
*/	}

}
