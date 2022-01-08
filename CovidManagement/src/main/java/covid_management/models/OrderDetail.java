package covid_management.models;

import shared.utilities.UtilityFunctions;

import java.sql.Timestamp;

public class OrderDetail {
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

	public boolean equals(OrderDetail orderDetail) {
		return detailNo == orderDetail.detailNo &&
				orderId == orderDetail.orderId &&
				necessariesId == orderDetail.necessariesId &&
				UtilityFunctions.compareTwoStrings(necessariesName, orderDetail.necessariesName) &&
				price == orderDetail.price &&
				quantity == orderDetail.quantity &&
				UtilityFunctions.compareTwoTimestamps(purchasedAt, orderDetail.purchasedAt);
	}
}
