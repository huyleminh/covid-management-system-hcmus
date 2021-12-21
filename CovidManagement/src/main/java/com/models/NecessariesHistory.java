package com.models;

import java.sql.Timestamp;

public class NecessariesHistory {
	public static final NecessariesHistory emptyNecessariesHistory = new NecessariesHistory(
			-1, "empty", null, "empty", (byte) -1
	);  // An object to check whether connection of the database is unavailable or not.
	// Using at the login view.

	private int historyId;
	private String managerUsername;
	private Timestamp date;
	private String description;
	private byte operationType;

	public NecessariesHistory(
			int historyId,
			String managerUsername,
			Timestamp date,
			String description,
			byte operationType
	) {
		this.historyId = historyId;
		this.managerUsername = managerUsername;
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
		return equals(NecessariesHistory.emptyNecessariesHistory);
	}

	public boolean equals(NecessariesHistory necessariesHistory) {
		return historyId == necessariesHistory.historyId &&
				managerUsername.equals(necessariesHistory.managerUsername) &&
				date == necessariesHistory.date &&
				description.equals(necessariesHistory.description) &&
				operationType == necessariesHistory.operationType;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> NecessariesHistory <<<");
		System.out.println("historyId       = " + historyId);
		System.out.println("managerUsername = " + managerUsername);
		System.out.println("date 		    = " + date.toString());
		System.out.println("description     = " + description);
		System.out.println("operationType   = " + operationType);
	}
}
