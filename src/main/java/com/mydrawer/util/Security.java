package com.mydrawer.util;

import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.*;

public class Security {

	private static final Logger logger = Logger.getLogger(Security.class.getName());

	private static final String ALGO = "AES";

	private static final byte[] keyValue = 
		new byte[] {'M', 'a', 'r', 'm', 'a', 'l', 'a','d', 'e', 'S', 'k','i', 'e', 's', 'J', 'L'};

	public String encryptPword(String email, String password) throws Exception {

		String encryptedPword = "";

		try {
			// Salt the password before encrypting
			String saltedPword = email.trim() + this.getSalt() + password.trim();
			encryptedPword = this.encrypt(saltedPword);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".encryptPword(): ", e);
		}
		finally {}

		return encryptedPword;
	}

	public String encryptCollectionName(String collectionName) {

		String encryptedValue = "";

		try {
			encryptedValue = this.encrypt(collectionName);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".encryptCollectionName(): ", e);
		}
		finally {}

		return encryptedValue;
	}

	public String decryptCollectionName(String argEncryptedCollectionName) {

		String decryptedValue = "";

		try {
			decryptedValue = this.decrypt(argEncryptedCollectionName);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".decryptCollectionName(): ", e);
		}
		finally {}

		return decryptedValue;
	}

	private String getSalt() {
		// Note: The leading and trailing pipes for parsing the actual value from the salt if needed
		return "|^~s$&-e-&$z~^|";
	}

	private String encrypt(String Data) {

		String encryptedValue = "";

		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encVal = c.doFinal(Data.getBytes());
		
			encryptedValue = new BASE64Encoder().encode(encVal);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".encrypt(): ", e);
		}
		
		return encryptedValue;
	}

	private String decrypt(String encryptedData) {

		String decryptedValue = "";

		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
			byte[] decValue = c.doFinal(decordedValue);

			decryptedValue = new String(decValue);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".decrypt(): ", e);
		}
		
		return decryptedValue;
	}

	private Key generateKey() {

		Key key = null;

		try {
			key = new SecretKeySpec(keyValue, ALGO);
		}
		catch(Exception e) {
			logger.log(
				Level.SEVERE, this.getClass().getName() + ".generateKey(): ", e);
		}

		return key;
	}

	public static void main(String[] args) {

		Security ee = new Security();

		String value = "c62eu19gmailcom";

		// Salt the password before encrypting
//		String saltedValue = email.trim() + ee.getSalt() + mbrSk.trim();
		String saltedValue = value.trim();
		String encryptedValue = ee.encrypt(saltedValue);

		String decryptedValue = ee.decrypt(encryptedValue);

		System.out.println(
			"input: " + value + " --> encrypted: " + encryptedValue + " --> decrypted: " + decryptedValue);

	}

}
