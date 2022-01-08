package covid_management.views.user.tabbed_panes;

import covid_management.views.user.panels.BasicInfoPanel;
import covid_management.views.user.panels.UserPersonalInfoTablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class PersonalInfoTabbed extends JTabbedPane {
	// Constants
	public static final String BASIC_INFORMATION_TITLE = "Basic Information";
	public static final String MANAGED_HISTORY_TITLE = "Managed History";
	public static final String PURCHASED_NECESSARIES_TITLE = "Purchased Necessaries";
	public static final String DEBT_TITLE = "Debt";
	public static final String PAYMENT_HISTORY_TITLE = "Payment History";
	public static final int BASIC_INFORMATION_INDEX = 0;
	public static final int MANAGED_HISTORY_INDEX = 1;
	public static final int PURCHASED_NECESSARIES_INDEX = 2;
	public static final int DEBT_INDEX = 3;
	public static final int PAYMENT_HISTORY_INDEX = 4;

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
		final String[] columnNames = {"Description", "Date"};
		final int [] columnWidths = {1410, 160};

		managedHistoryPanel = new UserPersonalInfoTablePanel(columnNames, columnWidths);
		addTab(MANAGED_HISTORY_TITLE, managedHistoryPanel);
	}

	private void initPurchasedNecessariesPanel() {
		final String[] columnNames = {"Necessaries Name", "Quantity", "Price Per One (VND)", "Date"};
		final int[] columnWidths = {525, 62, 130, 150};
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
		final int[] columnWidths = {371, 371};
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
		final int[] columnWidths = {371, 371};
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
