package com.views.user.panels;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.shared.panels.ScrollableTablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class NecessariesListPanel extends JPanel {
	// Components
	private JTextField searchValueTextField;
	private JButton searchButton;
	private JButton addToCartButton;
	private ScrollableTablePanel scrollableTable;
	private JButton sortButton;
	private JButton filterButton;

	public NecessariesListPanel() {
		super();
		setLayout(null);

		// Search value text field
		searchValueTextField = new JTextField();
		searchValueTextField.setBounds(10, 10, 350, 30);
		searchValueTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		add(searchValueTextField);

		// Search button
		searchButton = new JButton("Search");
		searchButton.setBounds(370, 10, 80, 30);
		searchButton.setBackground(Color.LIGHT_GRAY);
		searchButton.setHorizontalTextPosition(JButton.CENTER);
		searchButton.setForeground(Color.BLACK);
		add(searchButton);

		// Add to order button
		addToCartButton = new JButton("Add To Cart");
		addToCartButton.setBounds(630, 10, 140, 30);
		addToCartButton.setBackground(Constants.GREEN);
		addToCartButton.setHorizontalTextPosition(JButton.CENTER);
		addToCartButton.setForeground(Color.WHITE);
		add(addToCartButton);

		// Scrollable table
		final String[] columnNames = {
				"necessariesId",
				"Necessaries Name",
				"Quantity",
				"Start Date",
				"End Date",
				"Price (VND)"
		};
		final int [] columnWidths = {0, 300, 60, 150, 150, 80}; // 745 - 5
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT
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
		scrollableTable.setBounds(10, 50, tableWidth + verticalScrollbarWidth, 455);

		// Sort button
		sortButton = new JButton("Sort");
		sortButton.setBounds(10, 515, 80, 30);
		sortButton.setBackground(Color.LIGHT_GRAY);
		sortButton.setHorizontalTextPosition(JButton.CENTER);
		sortButton.setForeground(Color.BLACK);
		add(sortButton);

		// Filter button
		filterButton = new JButton("Filter");
		filterButton.setBounds(100, 515, 80, 30);
		filterButton.setBackground(Color.LIGHT_GRAY);
		filterButton.setHorizontalTextPosition(JButton.CENTER);
		filterButton.setForeground(Color.BLACK);
		add(filterButton);
	}

	public JTextField getSearchValueTextField() {
		return searchValueTextField;
	}

	public JButton getSearchButton() {
		return searchButton;
	}

	public JButton getAddToCartButton() {
		return addToCartButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}

	public JButton getSortButton() {
		return sortButton;
	}

	public JButton getFilterButton() {
		return filterButton;
	}

	public void clearDataShowing() {
		((NonEditableTableModel) scrollableTable.getTableModel()).removeAllRows();
	}
}
