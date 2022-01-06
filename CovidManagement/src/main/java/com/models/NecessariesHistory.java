package com.models;

import com.utilities.UtilityFunctions;

import java.sql.Timestamp;

public class NecessariesHistory {
	public static final byte ADD_NEW_NECESSARIES = 1;
	public static final byte REMOVE_NECESSARIES = 2;
	public static final byte CHANGE_NECESSARIES_NAME = 3;
	public static final byte CHANGE_LIMIT_QUANTITY = 4;
	public static final byte CHANGE_DATE = 5;
	public static final byte CHANGE_PRICE = 6;
	public static final NecessariesHistory emptyInstance = new NecessariesHistory(
			-1, "", null, "", (byte) -1
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
		return equals(NecessariesHistory.emptyInstance);
	}

	public boolean equals(NecessariesHistory necessariesHistory) {
		return historyId == necessariesHistory.historyId &&
				UtilityFunctions.compareTwoStrings(managerUsername, necessariesHistory.managerUsername) &&
				UtilityFunctions.compareTwoTimestamps(date, necessariesHistory.date) &&
				UtilityFunctions.compareTwoStrings(description, necessariesHistory.description) &&
				operationType == necessariesHistory.operationType;
	}

	public static String generateDescriptionWithoutFormatting(byte operationType) {
		String description = "";

		switch (operationType) {
			case ADD_NEW_NECESSARIES -> description = "Thêm mới gói %s";
			case REMOVE_NECESSARIES -> description = "Xóa gói %s";
			case CHANGE_NECESSARIES_NAME -> description = "Thay đổi tên gói từ %s thành %s";
			case CHANGE_LIMIT_QUANTITY -> description = "Thay đổi mức giới hạn từ %d thành %d";
			case CHANGE_DATE -> description = "Thay đổi thời gian gới hạn từ (%s -> %s) thành (%s -> %s)";
			case CHANGE_PRICE -> description = "Thay đổi đơn giá từ %d thành %d";
		}

		return description;
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
