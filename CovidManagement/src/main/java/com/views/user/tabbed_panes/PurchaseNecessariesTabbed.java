package com.views.user.tabbed_panes;

import com.views.user.panels.NecessariesListPanel;
import com.views.user.panels.CartPanel;

import javax.swing.*;

public class PurchaseNecessariesTabbed extends JTabbedPane {
	public static final String LIST_OF_NECESSARIES_TITLE = "List of Necessaries";
	public static final String CART_TITLE = "Cart";
	public static final int LIST_OF_NECESSARIES_INDEX = 0;
	public static final int CART_INDEX = 1;

	// Components
	private NecessariesListPanel necessariesListPanel;
	private CartPanel cartPanel;

	public PurchaseNecessariesTabbed() {
		super();

		// Necessaries list panel
		necessariesListPanel = new NecessariesListPanel();
		addTab("List of Necessaries", necessariesListPanel);

		// Cart panel
		cartPanel = new CartPanel();
		addTab("Cart", cartPanel);
	}

	public NecessariesListPanel getNecessariesListPanel() {
		return necessariesListPanel;
	}

	public CartPanel getCartPanel() {
		return cartPanel;
	}
}
