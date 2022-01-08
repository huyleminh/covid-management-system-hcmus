package covid_management.models;

import shared.utilities.UtilityFunctions;

import java.sql.Timestamp;

public class Debt {
	private int debtId;
	private int userId;
	private Timestamp debtDate;
	private int totalDebt;

	public Debt(int debtId, int userId, Timestamp debtDate, int totalDebt) {
		this.debtId = debtId;
		this.userId = userId;
		this.debtDate = debtDate;
		this.totalDebt = totalDebt;
	}

	public int getDebtId() {
		return debtId;
	}

	public int getUserId() {
		return userId;
	}

	public Timestamp getDebtDate() {
		return debtDate;
	}

	public int getTotalDebt() {
		return totalDebt;
	}

	public boolean equals(Debt debt) {
		return debtId == debt.debtId &&
				userId == debt.userId &&
				UtilityFunctions.compareTwoTimestamps(debtDate, debt.debtDate) &&
				totalDebt == debt.totalDebt;
	}
}
