package com.models;

import com.utilities.UtilityFunctions;

import java.sql.Timestamp;

public class OrderDetail {
	public static final OrderDetail emptyInstance = new OrderDetail(
			-1, -1, -1, "", -1, (byte) -1, null
	);  // An object to check whether connection of the database is unavailable or not.
	// Using at the login view.

	private int detailNo;
	private int orderId;
	private int necessariesId;
	private String necessariesName;
	private int price;
	private byte quantity;
	private Timestamp purchasedAt;

	public OrderDetail(
			int detailNo,
			int orderId,
			int necessariesId,
			String necessariesName,
			int price,
			byte quantity,
			Timestamp purchasedAt
	) {
		this.detailNo = detailNo;
		this.orderId = orderId;
		this.necessariesId = necessariesId;
		this.necessariesName = necessariesName;
		this.price = price;
		this.quantity = quantity;
		this.purchasedAt = purchasedAt;
	}

	public int getDetailNo() {
		return detailNo;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getNecessariesId() {
		return necessariesId;
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

	public Timestamp getPurchasedAt() {
		return purchasedAt;
	}

	public boolean isEmpty() {
		return equals(OrderDetail.emptyInstance);
	}

	public boolean equals(OrderDetail orderDetail) {
		return detailNo == orderDetail.detailNo &&
				orderId == orderDetail.orderId &&
				necessariesId == orderDetail.necessariesId &&
				UtilityFunctions.compareTwoStrings(necessariesName, orderDetail.necessariesName) &&
				price == orderDetail.price &&
				quantity == orderDetail.quantity &&
				UtilityFunctions.compareTwoTimestamps(purchasedAt, orderDetail.purchasedAt);
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> OrderDetail <<<");
		System.out.println("detailNo 		= " + detailNo);
		System.out.println("orderId 		= " + orderId);
		System.out.println("necessariesId	= " + necessariesId);
		System.out.println("necessariesName = " + necessariesName);
		System.out.println("price 			= " + price);
		System.out.println("quantity 		= " + quantity);
		System.out.println("purchasedAt		= " + purchasedAt.toString());
	}
}
