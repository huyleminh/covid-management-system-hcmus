package com.models;

import java.sql.Timestamp;

public class UserHistory {
	public static final UserHistory emptyUserHistory = new UserHistory(
			-1, "empty", -1, null, "emtpy", (byte) -1
	);  // An object to check whether connection of the database is unavailable or not.
	// Using at the login view.

	public static final byte ADD_NEW_USER = 1;
	public static final byte CHANGE_STATUS = 2;
	public static final byte CHANGE_QUARANTINE = 3;

	private int historyId;
	private String managerUsername;
	private int userId;
	private Timestamp date;
	private String description;
	private byte operationType;

	public UserHistory(
			int historyId,
			String managerUsername,
			int userId,
			Timestamp date,
			String description,
			byte operationType
	) {
		this.historyId = historyId;
		this.managerUsername = managerUsername;
		this.userId = userId;
		this.date = date;
		this.description = description;
		this.operationType = operationType;
	}

	public int getHistoryId() {
		return historyId;
	}

	public String getManagerUsername() {
		return managerUsername;
	}

	public int getUserId() {
		return userId;
	}

	public Timestamp getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public byte getOperationType() {
		return operationType;
	}

	public boolean isEmpty() {
		return equals(UserHistory.emptyUserHistory);
	}

	public boolean equals(UserHistory userHistory) {
		return historyId == userHistory.historyId &&
				managerUsername.equals(userHistory.managerUsername) &&
				userId == userHistory.userId &&
				date == userHistory.date &&
				description.equals(userHistory.description) &&
				operationType == userHistory.operationType;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> UserHistory <<<");
		System.out.println("historyId       = " + historyId);
		System.out.println("managerUsername = " + managerUsername);
		System.out.println("userId 		    = " + userId);
		System.out.println("date 		    = " + date.toString());
		System.out.println("description     = " + description);
		System.out.println("operationType   = " + operationType);
	}
}