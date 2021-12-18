package com.models;

public class Ward {
	private int wardId;
	private String wardName;
	private int districtId;

	public Ward(int wardId, String wardName, int districtId) {
		this.wardId = wardId;
		this.wardName = wardName;
		this.districtId = districtId;
	}

	public int getWardId() {
		return wardId;
	}

	public String getWardName() {
		return wardName;
	}

	public int getDistrictId() {
		return districtId;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> Ward <<<");
		System.out.println("wardId 	   = " + wardId);
		System.out.println("wardName   = " + wardName);
		System.out.println("districtId = " + districtId);
	}
}
