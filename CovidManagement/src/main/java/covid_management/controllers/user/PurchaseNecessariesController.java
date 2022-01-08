package covid_management.controllers.user;

import covid_management.views.user.tabbed_panes.PurchaseNecessariesTabbed;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PurchaseNecessariesController implements ChangeListener {
	final private PurchaseNecessariesTabbed purchaseNecessariesTabbed;
	final private NecessariesListController necessariesListController;
	final private CartController cartController;
	final private ConnectionErrorDialog connectionErrorDialog;
	private int currentTabIndex;

	public PurchaseNecessariesController(
			JFrame mainFrame,
			PurchaseNecessariesTabbed purchaseNecessariesTabbed,
			int userId
	) {
		this.purchaseNecessariesTabbed = purchaseNecessariesTabbed;
		this.cartController = new CartController(
				mainFrame,
				purchaseNecessariesTabbed.getCartPanel(),
				userId
		);
		this.necessariesListController = new NecessariesListController(
				mainFrame,
				purchaseNecessariesTabbed.getNecessariesListPanel(),
				cartController,
				userId
		);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.currentTabIndex = PurchaseNecessariesTabbed.LIST_OF_NECESSARIES_INDEX;

		this.connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
			connectionErrorDialog.setExitOnCloseButton(false);
			connectionErrorDialog.setVisible(false);

			SingletonDBConnection.getInstance().connect();
			preprocessOf(currentTabIndex);
		});

		this.purchaseNecessariesTabbed.addChangeListener(this);
	}

	public void preprocessAndDisplayUI() {
		if (purchaseNecessariesTabbed.getSelectedIndex() == PurchaseNecessariesTabbed.LIST_OF_NECESSARIES_INDEX)
			preprocessOf(PurchaseNecessariesTabbed.LIST_OF_NECESSARIES_INDEX);
		else
			purchaseNecessariesTabbed.setSelectedIndex(PurchaseNecessariesTabbed.LIST_OF_NECESSARIES_INDEX);

		purchaseNecessariesTabbed.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		final int index = purchaseNecessariesTabbed.getSelectedIndex();
		this.currentTabIndex = index;

		preprocessOf(index);
	}

	// Preprocessing of a tab of the PurchaseNecessariesTabbed.
	private void preprocessOf(int tabIndex) {
		switch (tabIndex) {
			case PurchaseNecessariesTabbed.LIST_OF_NECESSARIES_INDEX -> {
				necessariesListController.preprocess();
			}
			case PurchaseNecessariesTabbed.CART_INDEX -> {
				necessariesListController.preprocess();
				cartController.preprocess();
			}
		}
	}
}
