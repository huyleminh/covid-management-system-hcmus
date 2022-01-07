package com.views.user.panels;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.shared.panels.ScrollableTablePanel;

import javax.swing.*;
import java.awt.*;

public class DebtPaymentPanel extends JPanel {
	// Components
//	private JLabel minimumDebtValueLabel;
	private JLabel totalDebtValueLabel;
	private JButton payButton;
	private ScrollableTablePanel scrollableTable;

	public DebtPaymentPanel() {
		super();

		setLayout(null);
		initComponents();
	}

	private void initComponents() {
		// Minimum debt pane
		JPanel minimumDebtPane = new JPanel();
		minimumDebtPane.setLayout(null);
		minimumDebtPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		minimumDebtPane.setBounds(0, 0, 255, Constants.TEXT_HEIGHT);
		add(minimumDebtPane);

		// Minimum debt label
		JLabel minimumDebtLabel = new JLabel("Minimum Debt", SwingConstants.LEFT);
		minimumDebtLabel.setBounds(5, 0, 100, Constants.TEXT_HEIGHT);
		minimumDebtPane.add(minimumDebtLabel);

		// Minimum debt value
		JLabel minimumDebtValueLabel = new JLabel("2.000.000 VND", SwingConstants.RIGHT);
		minimumDebtValueLabel.setBounds(110, 0, 140, Constants.TEXT_HEIGHT);
		minimumDebtPane.add(minimumDebtValueLabel);

		JPanel totalDebtPane = new JPanel();
		totalDebtPane.setLayout(null);
		totalDebtPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		totalDebtPane.setBounds(430, 0, 255, Constants.TEXT_HEIGHT);
		add(totalDebtPane);

		// Minimum debt label
		JLabel totalDebtLabel = new JLabel("Total Debt", SwingConstants.LEFT);
		totalDebtLabel.setBounds(5, 0, 100, Constants.TEXT_HEIGHT);
		totalDebtPane.add(totalDebtLabel);

		// Minimum debt value
		totalDebtValueLabel = new JLabel("2.000.000 VND", SwingConstants.RIGHT);
		totalDebtValueLabel.setBounds(110, 0, 140, Constants.TEXT_HEIGHT);
		totalDebtPane.add(totalDebtValueLabel);

		// Pay button
		payButton = new JButton("Pay");
		payButton.setBounds(700, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		payButton.setBackground(Constants.GREEN);
		payButton.setHorizontalTextPosition(JButton.CENTER);
		payButton.setForeground(Color.WHITE);
		add(payButton);

		// Scrollable table
		final String[] columnNames = {"Total Debt (VND)", "Date"};
		final int[] columnWidths = {381, 381};

		NonEditableTableModel tableModel = new NonEditableTableModel(columnNames, 0);
		scrollableTable = new ScrollableTablePanel(new JTable(tableModel));
		scrollableTable.setRowSelectionAllowed(false);
		scrollableTable.setColumnWidths(columnWidths);
		add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();
		final int verticalScrollbarWidth = scrollableTable.getVerticalScrollBar().getPreferredSize().width;

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(0, 40, tableWidth + verticalScrollbarWidth, 540);
	}

	public JLabel getTotalDebtValueLabel() {
		return totalDebtValueLabel;
	}

	public int getTotalDebtValue() {
		int length = totalDebtValueLabel.getText().length();
		String valueFormatted = totalDebtValueLabel.getText().substring(0, length - 4);
		return Integer.parseInt(valueFormatted.replaceAll("\\.", ""));
	}

	public JButton getPayButton() {
		return payButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}
}
