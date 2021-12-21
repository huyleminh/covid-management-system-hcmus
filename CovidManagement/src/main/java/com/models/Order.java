package com.models;

import java.sql.Timestamp;

public class Order {
	private int orderId;
	private int userId;
	private Timestamp createdDate;
	private int totalPrice;

	public Order(int orderId, int userId, Timestamp createdDate, short totalPrice) {
		this.orderId = orderId;
		this.userId = userId;
		this.createdDate = createdDate;
		this.totalPrice = totalPrice;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getUserId() {
		return userId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> Order <<<");
		System.out.println("orderId 	= " + orderId);
		System.out.println("userId 		= " + userId);
		System.out.println("createdDate = " + createdDate.toString());
		System.out.println("totalPrice  = " + totalPrice);
	}
}
