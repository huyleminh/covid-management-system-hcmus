package com.models;

public class User {
	// Constants
	public static final String[] STATUS_NAMES = {"F0", "F1", "F2", "F3", "Undangerous", "Recovery", "Unknown"};

	private int userId;
	private String identifierNumber;
	private String fullname;
	private short yearOfBirth;
	private int locationId;
    private byte status;
	private int userInvolvedId;
	private String street;
	private int wardId;
	private int districtId;
	private int provinceId;

	public User(int userId, String identifierNumber,
				String fullname, short yearOfBirth,
				int locationId, byte status, int userInvolvedId,
				String street, int wardId, int districtId, int provinceId
	) {
		this.userId = userId;
		this.identifierNumber = identifierNumber;
		this.fullname = fullname;
		this.yearOfBirth = yearOfBirth;
		this.locationId = locationId;
		this.status = status;
		this.userInvolvedId = userInvolvedId;
		this.street = street;
		this.wardId = wardId;
		this.districtId = districtId;
		this.provinceId = provinceId;
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

	public int getUserInvolvedId() {
		return userInvolvedId;
	}

	public String getStreet() {
		return street;
	}

	public int getWardId() {
		return wardId;
	}

	public int getDistrictId() {
		return districtId;
	}

	public int getProvinceId() {
		return provinceId;
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
		System.out.println("userInvolvedId 	 = " + userInvolvedId);
		System.out.println("street 			 = " + street);
		System.out.println("wardId 			 = " + wardId);
		System.out.println("districtId 		 = " + districtId);
		System.out.println("provinceId 		 = " + provinceId);
	}
}
