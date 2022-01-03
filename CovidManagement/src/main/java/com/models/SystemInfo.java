package com.models;

public class SystemInfo {
	public static final byte NOT_INITIALIZE_YET = 0;
	public static final byte INITIALIZED = 1;

	private int id;
	private byte firstLoggedIn;

	public SystemInfo(int id, byte firstLoggedIn) {
		this.id = id;
		this.firstLoggedIn = firstLoggedIn;
	}

	public int getId() {
		return id;
	}

	public byte getFirstLoggedIn() {
		return firstLoggedIn;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> SystemInfo <<<");
		System.out.println("id 			  = " + id);
		System.out.println("firstLoggedIn = " + firstLoggedIn);
	}
}
