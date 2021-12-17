package com.models;

public class Account {
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

	public String getPassword() {
		return password;
	}

	public byte getRole() {
		return role;
	}

	public byte getIsActive() {
		return isActive;
	}

	public int getUserId() {
		return userId;
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
