package com.mydrawer.exception;

@SuppressWarnings("serial")
public class InvalidSigninException extends Exception {

	public InvalidSigninException(String message) {
		super(message);
	}

	public String getMessage() {
		return super.getMessage();
	}
}
