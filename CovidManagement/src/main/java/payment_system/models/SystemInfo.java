package payment_system.models;

public class SystemInfo {
	private int id;
	private byte firstLoggedIn;
	private String bankAccountNumber;
	private int balance;
	private int defaultBalanceOfNewAccount;

	public SystemInfo(
			int id,
			byte firstLoggedIn,
			String bankAccountNumber,
			int balance,
			int defaultBalanceOfNewAccount
	) {
		this.id = id;
		this.firstLoggedIn = firstLoggedIn;
		this.bankAccountNumber = bankAccountNumber;
		this.balance = balance;
		this.defaultBalanceOfNewAccount = defaultBalanceOfNewAccount;
	}

	public int getId() {
		return id;
	}

	public byte getFirstLoggedIn() {
		return firstLoggedIn;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public int getBalance() {
		return balance;
	}

	public int getDefaultBalanceOfNewAccount() {
		return defaultBalanceOfNewAccount;
	}
}
