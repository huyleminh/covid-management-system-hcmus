package payment_system.models;

public class PaymentAccount {
	private int paymentId;
	private int balance;
	private String fullname;
	private String userIdentifierNumber;

	public PaymentAccount(
			int paymentId,
			int balance,
			String fullname,
			String userIdentifierNumber
	) {
		this.paymentId = paymentId;
		this.balance = balance;
		this.fullname = fullname;
		this.userIdentifierNumber = userIdentifierNumber;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public int getBalance() {
		return balance;
	}

	public String getFullname() {
		return fullname;
	}

	public String getUserIdentifierNumber() {
		return userIdentifierNumber;
	}
}
