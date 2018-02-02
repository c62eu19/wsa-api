package com.mydrawer.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateUtility {

	private static final Logger logger = Logger.getLogger(DateUtility.class.getName());

	public static String getCurrentDateTime() {

		String currentDateTime = "";

		try {
			currentDateTime = 
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, "DateUtility.getCurrentDateTime(): ", e);
		}

		return currentDateTime;
	}

}
