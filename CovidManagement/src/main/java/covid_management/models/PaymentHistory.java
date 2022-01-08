package covid_management.models;

import shared.utilities.UtilityFunctions;

import java.sql.Timestamp;

public class PaymentHistory {
	private int historyId;
	private int userId;
	private Timestamp date;
	private int paymentAmount;

	public PaymentHistory(int historyId, int userId, Timestamp date, int paymentAmount) {
		this.historyId = historyId;
		this.userId = userId;
		this.date = date;
		this.paymentAmount = paymentAmount;
	}

	public int getHistoryId() {
		return historyId;
	}

	public int getUserId() {
		return userId;
	}

	public Timestamp getDate() {
		return date;
	}

	public int getPaymentAmount() {
		return paymentAmount;
	}

	public boolean equals(PaymentHistory debt) {
		return historyId == debt.historyId &&
				userId == debt.userId &&
				UtilityFunctions.compareTwoTimestamps(date, debt.date) &&
				paymentAmount == debt.paymentAmount;
	}
}
