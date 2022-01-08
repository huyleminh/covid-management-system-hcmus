package covid_management.views.user.tabbed_panes;

import covid_management.views.user.panels.CartPanel;
import covid_management.views.user.panels.NecessariesListPanel;

import javax.swing.*;

public class PurchaseNecessariesTabbed extends JTabbedPane {
	// Constants
	public static final String LIST_OF_NECESSARIES_TITLE = "List of Necessaries";
	public static final String CART_TITLE = "Cart";
	public static final int LIST_OF_NECESSARIES_INDEX = 0;
	public static final int CART_INDEX = 1;

	// Components
	final private NecessariesListPanel necessariesListPanel;
	final private CartPanel cartPanel;

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
