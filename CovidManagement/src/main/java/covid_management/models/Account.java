package covid_management.models;

import shared.utilities.UtilityFunctions;

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

	public boolean equals(Account account) {
		return UtilityFunctions.compareTwoStrings(username, account.username) &&
				UtilityFunctions.compareTwoStrings(password, account.password) &&
				role == account.role &&
				isActive == account.isActive &&
				userId == account.userId;
	}

	public static Account createEmpty() {
		return new Account("", "", (byte) -1, (byte) -1, -1);
	}
}
