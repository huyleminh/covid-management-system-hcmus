package com.models;

public class SystemInfo {
	public static final byte INITIALIZE_FIRST_TIME = 0;
	public static final byte HAS_INITIALIZED_FIRST_TIME = 1;

	private int id;
	private byte firstLoggedIn;
	private String bankAccountNumber;
	private int balance;

	public SystemInfo(int id, byte firstLoggedIn, String bankAccountNumber, int balance) {
		this.id = id;
		this.firstLoggedIn = firstLoggedIn;
		this.bankAccountNumber = bankAccountNumber;
		this.balance = balance;
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

	// Testing
	public void logToScreen() {
		System.out.println(">>> SystemInfo <<<");
		System.out.println("id 				  = " + id);
		System.out.println("firstLoggedIn 	  = " + firstLoggedIn);
		System.out.println("bankAccountNumber = " + bankAccountNumber);
		System.out.println("balance 		  = " + balance);
	}
}
