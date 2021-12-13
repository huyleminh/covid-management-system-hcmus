package com.views.user.tabbed_panes;

import com.views.user.panels.NecessariesListPanel;
import com.views.user.panels.OrderDetailPanel;

import javax.swing.*;

public class PurchaseNecessariesTabbed extends JTabbedPane {
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
