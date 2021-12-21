package com.models;

import java.sql.Timestamp;

public class PaymentHistory {
	// An object to check whether connection of the database is unavailable or not.
	public static final PaymentHistory emptyPaymentHistory = new PaymentHistory(
			-1, -1, null, -1
	);

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

	public boolean isEmpty() {
		return equals(PaymentHistory.emptyPaymentHistory);
	}

	public boolean equals(PaymentHistory debt) {
		boolean isEqualDate = false;

		if (date != null) {
			if (!date.equals(debt.date))
				return false;
			else
				isEqualDate = true;
		}

		return historyId == debt.historyId &&
				userId == debt.userId &&
				(isEqualDate || debt.date == null) &&
				paymentAmount == debt.paymentAmount;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> PaymentHistory <<<");
		System.out.println("historyId     = " + historyId);
		System.out.println("userId        = " + userId);
		System.out.println("date  		  = " + date.toString());
		System.out.println("paymentAmount = " + paymentAmount);
	}
}
