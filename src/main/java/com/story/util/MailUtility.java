package com.story.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailUtility {

	private static final Logger logger = Logger.getLogger(MailUtility.class.getName());

	public static boolean isEmailValid(String email) {

		boolean isValid = false;

		try {
			String emailPattern = 
				"^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

			Pattern p = Pattern.compile(emailPattern);

			Matcher m = p.matcher(email);

			if(m.matches()) {
				isValid = true;
			} else {
				isValid = false;
			}
		}
		catch(Exception e) {
			isValid = false;
			logger.log(
				Level.SEVERE, "MailUtility.isEmailValid(): ", e);
		}

		return isValid;
	}

}
