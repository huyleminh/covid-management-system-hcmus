package payment_system.models;

import java.sql.Timestamp;

public class Transaction {
	private int transactionId;
	private int sourceAccount;
	private Timestamp transactionDate;
	private int paymentAmount;

	public Transaction(
			int transactionId,
			int sourceAccount,
			Timestamp transactionDate,
			int paymentAmount
	) {
		this.transactionId = transactionId;
		this.sourceAccount = sourceAccount;
		this.transactionDate = transactionDate;
		this.paymentAmount = paymentAmount;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public int getSourceAccount() {
		return sourceAccount;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public int getPaymentAmount() {
		return paymentAmount;
	}
}
