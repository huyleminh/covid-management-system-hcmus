package com.views.user.tabbed_panes;

import com.views.user.panels.BasicInfoPanel;
import com.views.user.panels.UserPersonalInfoTablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class PersonalInfoTabbed extends JTabbedPane {
	public static final String BASIC_INFORMATION_TITLE = "Basic Information";
	public static final String MANAGED_HISTORY_TITLE = "Managed History";
	public static final String PURCHASED_NECESSARIES_TITLE = "Purchased Necessaries";
	public static final String DEBT_TITLE = "Debt";
	public static final String PAYMENT_HISTORY_TITLE = "Payment History";

	// Components
	private BasicInfoPanel basicInfoPanel;
	private UserPersonalInfoTablePanel managedHistoryPanel;
	private UserPersonalInfoTablePanel purchasedNecessariesPanel;
	private UserPersonalInfoTablePanel debtPanel;
	private UserPersonalInfoTablePanel paymentHistoryPanel;

	public PersonalInfoTabbed() {
		super();

		initBasicInfoPanel();
		initManagedHistoryPanel();
		initPurchasedNecessariesPanel();
		initDebtPanel();
		initPaymentHistoryPanel();
	}

	private void initBasicInfoPanel() {
		basicInfoPanel = new BasicInfoPanel();
		addTab(BASIC_INFORMATION_TITLE, basicInfoPanel);
	}

	private void initManagedHistoryPanel() {
		final String[] columnNames = {"Manager Name", "Description", "Date"};
		final int [] columnWidths = {180, 413, 150};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT
		};

		managedHistoryPanel = new UserPersonalInfoTablePanel(
				columnNames, columnWidths, columnHorizontalAlignments
		);
		addTab(MANAGED_HISTORY_TITLE, managedHistoryPanel);
	}

	private void initPurchasedNecessariesPanel() {
		final String[] columnNames = {"Necessaries Name", "Quantity", "Total Amount (VND)", "Date"};
		final int[] columnWidths = {400, 62, 130, 150};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.LEFT
		};

		purchasedNecessariesPanel = new UserPersonalInfoTablePanel(
				columnNames, columnWidths, columnHorizontalAlignments
		);
		addTab(PURCHASED_NECESSARIES_TITLE, purchasedNecessariesPanel);
	}

	private void initDebtPanel() {
		final String[] columnNames = {"Total Debt (VND)", "Date"};
		final int[] columnWidths = {372, 372};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.LEFT
		};

		debtPanel = new UserPersonalInfoTablePanel(
				columnNames, columnWidths, columnHorizontalAlignments
		);
		addTab(DEBT_TITLE, debtPanel);
	}

	private void initPaymentHistoryPanel() {
		final String[] columnNames = {"Amount Paid (VND)", "Date"};
		final int[] columnWidths = {372, 372};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.LEFT
		};

		paymentHistoryPanel = new UserPersonalInfoTablePanel(
				columnNames, columnWidths, columnHorizontalAlignments
		);
		addTab(PAYMENT_HISTORY_TITLE, paymentHistoryPanel);
	}

	public BasicInfoPanel getBasicInfoPanel() {
		return basicInfoPanel;
	}

	public UserPersonalInfoTablePanel getManagedHistoryPanel() {
		return managedHistoryPanel;
	}

	public UserPersonalInfoTablePanel getPurchasedNecessariesPanel() {
		return purchasedNecessariesPanel;
	}

	public UserPersonalInfoTablePanel getDebtPanel() {
		return debtPanel;
	}

	public UserPersonalInfoTablePanel getPaymentHistoryPanel() {
		return paymentHistoryPanel;
	}
}
