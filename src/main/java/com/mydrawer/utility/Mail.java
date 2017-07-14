package com.mydrawer.utility;

import java.util.*;
import java.io.*;

import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.smtp.SMTPTransport;

public class Mail 
{
	public int validateEmailAddress(String email)
	{
		int statusCd = 0;

		try
		{
			// Create InternetAddress object and validated the supplied
			// address which is this case is an email address.
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			statusCd = 0;
		}
		catch(AddressException e)
		{
			statusCd = -1;
			System.out.println("EXCEPTION: " + this.getClass().getName() + ".validateEmailAddress(): " + e);
		}

		return statusCd;
	}

}
