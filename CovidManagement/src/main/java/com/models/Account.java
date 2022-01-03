package com.models;

import com.utilities.UtilityFunctions;

public class Account {
	// An object to check whether connection of the database is unavailable or not.
	// Using at the login view.
	public static final Account emptyInstance = new Account("", "", (byte) -1, (byte) -1, -1);

	public static final byte ADMIN = 0;
	public static final byte MANAGER = 1;
	public static final byte USER = 2;
	public static final byte ACTIVE = 0;
	public static final byte INACTIVE = 1;

	private String username;
	private String password;
	private byte role;
	private byte isActive;
	private int userId;

	public Account(String username, String password, byte role, byte isActive, int userId) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.isActive = isActive;
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	public byte getIsActive() {
		return isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isEmpty() {
		return equals(Account.emptyInstance);
	}

	public boolean equals(Account account) {
		return UtilityFunctions.compareTwoStrings(username, account.username) &&
				UtilityFunctions.compareTwoStrings(password, account.password) &&
				role == account.role &&
				isActive == account.isActive &&
				userId == account.userId;
	}

	public static Account createEmpty() {
		return new Account(
				emptyInstance.username,
				emptyInstance.password,
				emptyInstance.role,
				emptyInstance.isActive,
				emptyInstance.userId
		);
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> Account <<<");
		System.out.println("username = " + username);
		System.out.println("password = " + password);
		System.out.println("role 	 = " + role);
		System.out.println("isActive = " + isActive);
		System.out.println("userId 	 = " + userId);
	}
}
