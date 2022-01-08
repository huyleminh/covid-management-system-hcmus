package covid_management.models;

import shared.utilities.UtilityFunctions;

public class User {
	// Constants
	public static final String[] STATUS_NAMES = {"Bình phục", "F0", "F1", "F2", "F3", "Không rõ"};

	private int userId;
	private String identifierNumber;
	private String fullname;
	private short yearOfBirth;
	private int locationId;
    private byte status;
	private int infectiousUserId;
	private String address;

	public User(
			int userId,
			String identifierNumber,
			String fullname,
			short yearOfBirth,
			int locationId,
			byte status,
			int infectiousUserId,
			String address
	) {
		this.userId = userId;
		this.identifierNumber = identifierNumber;
		this.fullname = fullname;
		this.yearOfBirth = yearOfBirth;
		this.locationId = locationId;
		this.status = status;
		this.infectiousUserId = infectiousUserId;
		this.address = address;
	}

	public int getUserId() {
		return userId;
	}

	public String getIdentifierNumber() {
		return identifierNumber;
	}

	public String getFullname() {
		return fullname;
	}

	public short getYearOfBirth() {
		return yearOfBirth;
	}

	public int getLocationId() {
		return locationId;
	}

	public byte getStatus() {
		return status;
	}

	public int getInfectiousUserId() {
		return infectiousUserId;
	}

	public String getAddress() {
		return address;
	}

	public boolean equals(User user) {
		return userId == user.userId &&
				UtilityFunctions.compareTwoStrings(identifierNumber, user.identifierNumber) &&
				UtilityFunctions.compareTwoStrings(fullname, user.fullname) &&
				yearOfBirth == user.yearOfBirth &&
				locationId == user.locationId &&
				status == user.status &&
				infectiousUserId == user.infectiousUserId &&
				UtilityFunctions.compareTwoStrings(address, user.address);
	}

	public static byte byteValueOfStatus(String status) {
		byte statusAsByte = -1;

		switch (status) {
			case "Bình phục" -> statusAsByte = 0;
			case "F0" -> statusAsByte = 1;
			case "F1" -> statusAsByte = 2;
			case "F2" -> statusAsByte = 3;
			case "F3" -> statusAsByte = 4;
			case "Không rõ" -> statusAsByte = 5;
		}

		return statusAsByte;
	}
}
