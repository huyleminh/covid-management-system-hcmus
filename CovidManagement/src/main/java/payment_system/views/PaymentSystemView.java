package payment_system.views;

import shared.components.NonEditableTableModel;
import shared.components.panels.ScrollableTablePanel;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PaymentSystemView extends JPanel {
	// Components
	private JLabel bankIdValueLabel;
	private JLabel balanceValueLabel;
	private ScrollableTablePanel scrollableTable;

	public PaymentSystemView() {
		super();

		setLayout(null);
		initComponents();
		setPreferredSize(new Dimension(930, Constants.APP_HEIGHT));
	}

	private void initComponents() {
		initBankInfoPane();
		initScrollableTable();
	}

	private void initBankInfoPane() {
		// Bank id pane
		JPanel bankIdPane = new JPanel();
		bankIdPane.setLayout(null);
		bankIdPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		bankIdPane.setBounds(10, 10, 235, Constants.TEXT_HEIGHT);
		add(bankIdPane);

		// Bank id label
		JLabel bankIdLabel = new JLabel("Bank Account ID:", SwingConstants.CENTER);
		bankIdLabel.setBounds(5, 0, 110, Constants.TEXT_HEIGHT);
		bankIdPane.add(bankIdLabel);

		// Bank id value
		bankIdValueLabel = new JLabel("", SwingConstants.CENTER);
		bankIdValueLabel.setBounds(115, 0, 120, Constants.TEXT_HEIGHT);
		bankIdPane.add(bankIdValueLabel);

		// Balance pane
		JPanel balancePane = new JPanel();
		balancePane.setLayout(null);
		balancePane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		balancePane.setBounds(260, 10, 200, Constants.TEXT_HEIGHT);
		add(balancePane);

		// Balance text label
		JLabel balanceTextLabel = new JLabel("Balance:");
		balanceTextLabel.setBounds(5, 0, 60, Constants.TEXT_HEIGHT);
		balancePane.add(balanceTextLabel);

		// Balance value label
		balanceValueLabel = new JLabel("0", SwingConstants.RIGHT);
		balanceValueLabel.setBounds(70, 0, 90, Constants.TEXT_HEIGHT);
		balancePane.add(balanceValueLabel);

		// Currency code label
		JLabel currencyCodeLabel = new JLabel("VND");
		currencyCodeLabel.setBounds(165, 0, 30, Constants.TEXT_HEIGHT);
		balancePane.add(currencyCodeLabel);
	}

	private void initScrollableTable() {
		// Scrollable table
		final String[] columnNames = {"ID Card", "User name", "Price (VND)", "Date"};
		final int [] columnWidths = {105, 522, 110, 155}; // 910 - 15 - 3
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.LEFT
		};

		scrollableTable = new ScrollableTablePanel(
				new JTable(new NonEditableTableModel(columnNames, 0))
		);
		scrollableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollableTable.setColumnWidths(columnWidths);
		scrollableTable.setColumnHorizontalAlignments(columnHorizontalAlignments);
		add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();
		final int verticalScrollbarWidth = scrollableTable.getVerticalScrollBar().getPreferredSize().width;

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(10, 50, tableWidth + verticalScrollbarWidth, 540);
	}

	public JLabel getBankIdValueLabel() {
		return bankIdValueLabel;
	}

	public JLabel getBalanceValueLabel() {
		return balanceValueLabel;
	}

	public int getBalanceValue() {
		return Integer.parseInt(balanceValueLabel.getText().replaceAll("\\.", ""));
	}

	public void setBalanceValue(int balance) {
		balanceValueLabel.setText(UtilityFunctions.formatMoneyVND(balance));
	}

	public JTable getTable() {
		return scrollableTable.getTable();
	}

	public void displayOn(JFrame mainFrame) {
		mainFrame.setResizable(false);
		mainFrame.setContentPane(this);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
}
