package covid_management.models;

import shared.utilities.UtilityFunctions;

import java.sql.Timestamp;

public class UserHistory {
	public static final byte ADD_NEW_USER = 1;
	public static final byte DIRECTLY_CHANGE_STATUS = 2;
	public static final byte INDIRECTLY_CHANGE_STATUS = 3;
	public static final byte CHANGE_QUARANTINE = 4;

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

	public boolean equals(UserHistory userHistory) {
		return historyId == userHistory.historyId &&
				UtilityFunctions.compareTwoStrings(managerUsername, userHistory.managerUsername) &&
				userId == userHistory.userId &&
				UtilityFunctions.compareTwoTimestamps(date, userHistory.date) &&
				UtilityFunctions.compareTwoStrings(description, userHistory.description) &&
				operationType == userHistory.operationType;
	}

	public static String generateDescriptionWithoutFormatting(byte operationType) {
		String description = "";

		switch (operationType) {
			case ADD_NEW_USER 			  -> description = "Thêm mới người dùng %s";
			case DIRECTLY_CHANGE_STATUS   -> description = "Thay đổi trạng thái từ \"%s\" sang \"%s\" của người dùng %s";
			case INDIRECTLY_CHANGE_STATUS -> description = "Thay đổi trạng thái từ \"%s\" sang \"%s\" của người dùng %s, vì người lây nhiễm %s bị chuyển từ \"%s\" thành \"%s\"";
			case CHANGE_QUARANTINE 		  -> description = "Thay đổi nơi điều trị từ \"%s\" sang \"%s\" của người dùng %s";
		}

		return description;
	}
}
