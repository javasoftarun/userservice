package com.yatranow.userservice.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtils {

	/**
	 * Encrypts a password using SHA-1.
	 *
	 * @param password the plain text password
	 * @return the encrypted password
	 */
	public static String encryptPassword(String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			byte[] hash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error encrypting password", e);
		}
	}
}
