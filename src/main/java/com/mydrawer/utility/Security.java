package com.mydrawer.utility;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.*;

public class Security
{
	private static final String ALGO = "AES";

	private static final byte[] keyValue = 
//        new byte[] {'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y'};
		new byte[] {'M', 'a', 'r', 'm', 'a', 'l', 'a','d', 'e', 'S', 'k','i', 'e', 's', 'J', 'L'};

	public static String getSalt() 
	{
		// Note: The leading and trailing pipes for parsing the actual value from the salt if needed
		return "|^~s$&-e-&$z~^|";
	}

	public static String encrypt(String Data) 
		throws Exception
	{
		String encryptedValue = "";

		try
		{
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encVal = c.doFinal(Data.getBytes());
		
			encryptedValue = new BASE64Encoder().encode(encVal);
		}
		catch(Exception e)
		{
			
		}
		
		return encryptedValue;
	}

	public static String decrypt(String encryptedData) 
		throws Exception
	{
		String decryptedValue = "";

		try
		{
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
			byte[] decValue = c.doFinal(decordedValue);

			decryptedValue = new String(decValue);
		}
		catch(Exception e)
		{
			
		}
		
		return decryptedValue;
	}

	private static Key generateKey() throws Exception
	{
		Key key = null;

		try
		{
			key = new SecretKeySpec(keyValue, ALGO);
		}
		catch(Exception e)
		{
			
		}

		return key;
	}

	public static void main(String[] args) throws Exception
	{
		Security ee = new Security();

		String email = "c62eu19@gmail.com";
		String mbrSk = "2";

		// Salt the password before encrypting
		String saltedValue = email.trim() + ee.getSalt() + mbrSk.trim();
		String encryptedValue = ee.encrypt(saltedValue);

		System.out.println(email + " - " + mbrSk);

		String decryptedValue = ee.decrypt(encryptedValue);

		System.out.println("Decrypted Text : " + decryptedValue);

	}

}
