package com.models;

import com.utilities.UtilityFunctions;

public class User {
	// Constants
	public static final String[] STATUS_NAMES = {"Recovery", "F0", "F1", "F2", "F3", "Unknown"};
	public static final User emptyInstance = new User(
			-1,
			"",
			"",
			(short) -1,
			-1,
			(byte) -1,
			-1,
			""
	);

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

	public boolean isEmpty() {
		return equals(User.emptyInstance);
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

	public static Object getEmptyAttribute(String field) {
		Object value = null;

		switch (field) {
			case "userId", "yearOfBirth", "locationId", "status",
					"infectiousUserId", "wardId", "districtId", "provinceId"  -> value = -1;

			case "identifierNumber", "fullname"			 					-> value = "";
		}

		return value;
	}

	public static boolean isEmptyAttribute(String field, Object value) {
		boolean isEqual = false;

		switch (field) {
			case "userId", "locationId", "infectiousUserId", "wardId", "districtId", "provinceId" -> {
				isEqual = (Integer.parseInt(String.valueOf(value)) == -1);
			}
			case "identifierNumber", "fullname" -> isEqual = ("".equals(value));
			case "yearOfBirth" 					-> isEqual = (Short.parseShort(String.valueOf(value)) == -1);
			case "status" 						-> isEqual = (Byte.parseByte(String.valueOf(value)) == -1);
		}

		return isEqual;
	}

	public static byte byteValueOfStatus(String status) {
		byte statusAsByte = 0;

		switch (status) {
			case "Recovery" -> statusAsByte = 0;
			case "F0" -> statusAsByte = 1;
			case "F1" -> statusAsByte = 2;
			case "F2" -> statusAsByte = 3;
			case "F3" -> statusAsByte = 4;
			case "Unknown" -> statusAsByte = 5;
		}

		return statusAsByte;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> User <<<");
		System.out.println("userId 			 = " + userId);
		System.out.println("identifierNumber = " + identifierNumber);
		System.out.println("fullname 		 = " + fullname);
		System.out.println("yearOfBirth 	 = " + yearOfBirth);
		System.out.println("locationId 		 = " + locationId);
		System.out.println("status 			 = " + status);
		System.out.println("infectiousUserId = " + infectiousUserId);
		System.out.println("address 		 = " + address);
	}
}
