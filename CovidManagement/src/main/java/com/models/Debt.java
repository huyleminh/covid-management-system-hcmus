package com.models;

import java.sql.Timestamp;

public class Debt {
	// An object to check whether connection of the database is unavailable or not.
	public static final Debt emptyDebt = new Debt(-1, -1, null, -1);

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

	public boolean isEmpty() {
		return equals(Debt.emptyDebt);
	}

	public boolean equals(Debt debt) {
		boolean isEqualDate = false;

		if (debtDate != null) {
			if (!debtDate.equals(debt.debtDate))
				return false;
			else
				isEqualDate = true;
		}

		return debtId == debt.debtId &&
				userId == debt.userId &&
				(isEqualDate || debt.debtDate == null) &&
				totalDebt == debt.totalDebt;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> Debt <<<");
		System.out.println("debtId    = " + debtId);
		System.out.println("userId    = " + userId);
		System.out.println("debtDate  = " + debtDate.toString());
		System.out.println("totalDebt = " + totalDebt);
	}
}
