package com.views.user.panels;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.shared.panels.ScrollableTablePanel;

import javax.swing.*;
import java.awt.*;

public class DebtPaymentPanel extends JPanel {
	// Components
//	private JLabel minimumDebtValueLabel;
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
		JLabel minimumDebtValueLabel = new JLabel("1.000.000 VND", SwingConstants.RIGHT);
		minimumDebtValueLabel.setBounds(110, 0, 140, Constants.TEXT_HEIGHT);
		minimumDebtPane.add(minimumDebtValueLabel);

		// Pay button
		payButton = new JButton("Pay");
		payButton.setBounds(700, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		payButton.setBackground(Constants.GREEN);
		payButton.setHorizontalTextPosition(JButton.CENTER);
		payButton.setForeground(Color.WHITE);
		add(payButton);

		// Scrollable table
		final String[] columnNames = {"id", "Total Debt (VND)", "Date"};
		final int[] columnWidths = {0, 381, 381};

		NonEditableTableModel tableModel = new NonEditableTableModel(columnNames, 0);
		scrollableTable = new ScrollableTablePanel(
				new JTable(tableModel),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		scrollableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

	public JButton getPayButton() {
		return payButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}
}
