package com.models;

public class Province {
	private int provinceId;
	private String provinceName;

	public Province(int provinceId, String provinceName) {
		this.provinceId = provinceId;
		this.provinceName = provinceName;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> Province <<<");
		System.out.println("provinceId   = " + provinceId);
		System.out.println("provinceName = " + provinceName);
	}
}
