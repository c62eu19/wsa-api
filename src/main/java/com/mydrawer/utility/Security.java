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

		String valueEmail = "lifelink!";
		String valuePassword = "thehealthshare!";

		// Salt the password before encrypting
		String saltedPword = valueEmail.trim() + "^^" + valuePassword.trim();
		String encryptedPword = Security.encrypt(saltedPword);

		System.out.println(valueEmail + " - " + encryptedPword);

		String valueEnc = "";
		String valueDec = "";

		valueEnc = encrypt(valueEmail);
		valueDec = decrypt(valueEnc);

		System.out.println("Email: " + valueEmail);
		System.out.println("Encrypted Text : " + valueEnc);
		System.out.println("Decrypted Text : " + valueDec);

		valueEnc = encrypt(valuePassword);
		valueDec = decrypt(valueEnc);

		System.out.println("Password: " + valuePassword);
		System.out.println("Encrypted Text : " + valueEnc);
		System.out.println("Decrypted Text : " + valueDec);

	}

}
