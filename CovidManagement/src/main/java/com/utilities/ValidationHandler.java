package com.utilities;

public class ValidationHandler {
	private ValidationHandler() {}

	public static boolean validateUsername(String username) {
		if (username.equals("admin"))  // admin
			return true;
		if (username.matches("[a-zA-Z][a-zA-Z\\d]{5,11}"))  // manager
			return true;
		return username.matches("\\d{9}|\\d{12}");  // user
	}

	public static boolean validatePassword(String password) {
		return password.matches("[a-zA-Z\\d]{6,20}");
	}
}
