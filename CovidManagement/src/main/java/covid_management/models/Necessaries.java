package covid_management.models;

import shared.utilities.UtilityFunctions;

import java.sql.Timestamp;

public class Necessaries {
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

	public boolean equals(Necessaries necessaries) {
		return necessariesId == necessaries.necessariesId &&
				UtilityFunctions.compareTwoStrings(necessariesName, necessaries.necessariesName) &&
				limit == necessaries.limit &&
				UtilityFunctions.compareTwoTimestamps(startDate, necessaries.startDate) &&
				UtilityFunctions.compareTwoTimestamps(expiredDate, necessaries.expiredDate) &&
				price == necessaries.price;
	}
}
