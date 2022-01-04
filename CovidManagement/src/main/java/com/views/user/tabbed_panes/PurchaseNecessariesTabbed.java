package com.views.user.tabbed_panes;

import com.views.user.panels.NecessariesListPanel;
import com.views.user.panels.OrderDetailPanel;

import javax.swing.*;

public class PurchaseNecessariesTabbed extends JTabbedPane {
	public static final String LIST_OF_NECESSARIES_TITLE = "List of Necessaries";
	public static final String ORDER_DETAIL_TITLE = "Order Detail";
	public static final int LIST_OF_NECESSARIES_INDEX = 0;
	public static final int ORDER_DETAIL_INDEX = 1;

	// Components
	private NecessariesListPanel necessariesListPanel;
	private OrderDetailPanel orderDetailPanel;

	public PurchaseNecessariesTabbed() {
		super();

		// Necessaries list panel
		necessariesListPanel = new NecessariesListPanel();
		addTab("List of Necessaries", necessariesListPanel);

		// Order detail panel
		orderDetailPanel = new OrderDetailPanel();
		addTab("Order Detail", orderDetailPanel);
	}

	public NecessariesListPanel getNecessariesListPanel() {
		return necessariesListPanel;
	}

	public OrderDetailPanel getOrderDetailPanel() {
		return orderDetailPanel;
	}
}
