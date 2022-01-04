package com.models;

import com.utilities.UtilityFunctions;

import java.sql.Timestamp;

public class Necessaries {
	public static final Necessaries emptyInstance = new Necessaries(
			-1, "", (byte) -1, null, null, -1
	);

	private int necessariesId;
	private String necessariesName;
    private byte limit;
	private Timestamp startDate;
	private Timestamp expiredDate;
	private int price;

	public Necessaries(
			int necessariesId,
			String necessariesName,
			byte limit,
			Timestamp startDate,
			Timestamp expiredDate,
			int price
	) {
		this.necessariesId = necessariesId;
		this.necessariesName = necessariesName;
		this.limit = limit;
		this.startDate = startDate;
		this.expiredDate = expiredDate;
		this.price = price;
	}

	public int getNecessariesId() {
		return necessariesId;
	}

	public String getNecessariesName() {
		return necessariesName;
	}

	public byte getLimit() {
		return limit;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public Timestamp getExpiredDate() {
		return expiredDate;
	}

	public int getPrice() {
		return price;
	}

	public boolean isEmpty() {
		return equals(Necessaries.emptyInstance);
	}

	public boolean equals(Necessaries necessaries) {
		return necessariesId == necessaries.necessariesId &&
				UtilityFunctions.compareTwoStrings(necessariesName, necessaries.necessariesName) &&
				limit == necessaries.limit &&
				UtilityFunctions.compareTwoTimestamps(startDate, necessaries.startDate) &&
				UtilityFunctions.compareTwoTimestamps(expiredDate, necessaries.expiredDate) &&
				price == necessaries.price;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> Necessaries <<<");
		System.out.println("necessariesId 	= " + necessariesId);
		System.out.println("necessariesName = " + necessariesName);
		System.out.println("limit 			= " + limit);
		System.out.println("startDate 		= " + startDate.toString());
		System.out.println("expiredDate 	= " + expiredDate.toString());
		System.out.println("price 			= " + price);
	}
}
