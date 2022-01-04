package com.controllers.user;

import com.utilities.SingletonDBConnection;
import com.views.shared.dialogs.ConnectionErrorDialog;
import com.views.user.tabbed_panes.PurchaseNecessariesTabbed;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PurchaseNecessariesController implements ChangeListener {
	final private PurchaseNecessariesTabbed purchaseNecessariesTabbed;
	final private NecessariesListController necessariesListController;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private int userId;
	private int currentTabIndex;

	public PurchaseNecessariesController(
			JFrame mainFrame,
			PurchaseNecessariesTabbed purchaseNecessariesTabbed,
			int userId
	) {
		this.purchaseNecessariesTabbed = purchaseNecessariesTabbed;
		this.necessariesListController = new NecessariesListController(
				mainFrame,
				purchaseNecessariesTabbed.getNecessariesListPanel(),
				userId
		);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.userId = userId;
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
		preprocessOf(PurchaseNecessariesTabbed.LIST_OF_NECESSARIES_INDEX);
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
				purchaseNecessariesTabbed.getNecessariesListPanel().clearDataShowing();
				necessariesListController.preprocessAndDisplayUI();
			}
		}
	}
}
