package com.models;

import com.utilities.UtilityFunctions;

public class OrderDetail {
	public static final OrderDetail emptyInstance = new OrderDetail(
			-1, -1, "", -1, (byte) -1
	);  // An object to check whether connection of the database is unavailable or not.
	// Using at the login view.

	private int detailNo;
	private int orderId;
	private String necessariesName;
	private int price;
	private byte quantity;

	public OrderDetail(int detailNo, int orderId, String necessariesName, int price, byte quantity) {
		this.detailNo = detailNo;
		this.orderId = orderId;
		this.necessariesName = necessariesName;
		this.price = price;
		this.quantity = quantity;
	}

	public int getDetailNo() {
		return detailNo;
	}

	public int getOrderId() {
		return orderId;
	}

	public String getNecessariesName() {
		return necessariesName;
	}

	public int getPrice() {
		return price;
	}

	public byte getQuantity() {
		return quantity;
	}

	public boolean isEmpty() {
		return equals(OrderDetail.emptyInstance);
	}

	public boolean equals(OrderDetail orderDetail) {
		return detailNo == orderDetail.detailNo &&
				orderId == orderDetail.orderId &&
				UtilityFunctions.compareTwoStrings(necessariesName, orderDetail.necessariesName) &&
				price == orderDetail.price &&
				quantity == orderDetail.quantity;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> OrderDetail <<<");
		System.out.println("detailNo 		= " + detailNo);
		System.out.println("orderId 		= " + orderId);
		System.out.println("necessariesName = " + necessariesName);
		System.out.println("price 			= " + price);
		System.out.println("quantity 		= " + quantity);
	}
}
