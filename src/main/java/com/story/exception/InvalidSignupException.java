package com.story.exception;

@SuppressWarnings("serial")
public class InvalidSignupException extends Exception {

	public InvalidSignupException(String message) {
		super(message);
	}

	public String getMessage() {
		return super.getMessage();
	}
}
