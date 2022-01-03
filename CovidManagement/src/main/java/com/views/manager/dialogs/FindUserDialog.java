package com.views.manager.dialogs;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.shared.panels.ScrollableTablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class FindUserDialog extends JDialog {
	// Constants
	private static final int LEFT_PADDING = 10;
	private static final String[] SEARCH_OPTION_NAMES = {"ID Card", "Full Name"};

	// Components
	private JComboBox<String> searchOptions;
	private JTextField searchValueTextField;
	private JButton searchButton;
	private ScrollableTablePanel scrollableTable;
	private JButton cancelButton;
	private JButton selectButton;

	public FindUserDialog(JFrame frame) {
		super(frame, "Find User", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(600, 450));

		initComponents(panel);

		setAlwaysOnTop(true);
		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		JPanel searchValuePanel = new JPanel();
		searchValuePanel.setLayout(null);
		searchValuePanel.setBounds(10, 20, 490, 30);
		searchValuePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		panel.add(searchValuePanel);

		// Search options
		searchOptions = new JComboBox<>(SEARCH_OPTION_NAMES);
		searchOptions.setBounds(0, 0, 100, 30);
		searchValuePanel.add(searchOptions);

		// Search value text field
		searchValueTextField = new JTextField();
		searchValueTextField.setBounds(105, 1, 380, Constants.TEXT_HEIGHT-2);
		searchValueTextField.setBorder(null);
		searchValuePanel.setBackground(searchValueTextField.getBackground());
		searchValuePanel.add(searchValueTextField);

		// Search button
		searchButton = new JButton("Search");
		searchButton.setBounds(510, 20, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		searchButton.setBackground(Color.LIGHT_GRAY);
		searchButton.setHorizontalTextPosition(JButton.CENTER);
		searchButton.setForeground(Color.BLACK);
		panel.add(searchButton);

		// Scrollable table
		final String[] columnNames = {"userId", "ID Card", "Full name", "Year of Birth", "Current Status"};
		final int [] columnWidths = {0, 105, 256, 100, 100}; // 580 - 15 - 3
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.RIGHT
		};

		scrollableTable = new ScrollableTablePanel(
				new JTable(new NonEditableTableModel(columnNames, 0))
		);
		scrollableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollableTable.setColumnWidths(columnWidths);
		scrollableTable.setColumnHorizontalAlignments(columnHorizontalAlignments);
		panel.add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();
		final int verticalScrollbarWidth = scrollableTable.getVerticalScrollBar().getPreferredSize().width;

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(LEFT_PADDING, 60, tableWidth + verticalScrollbarWidth, 340);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(215, 410, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Create button
		selectButton = new JButton("Select");
		selectButton.setBounds(305, 410, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		selectButton.setHorizontalTextPosition(JButton.CENTER);
		selectButton.setBackground(Constants.LIGHT_BLUE);
		selectButton.setForeground(Color.WHITE);
		panel.add(selectButton);
	}

	public JComboBox<String> getSearchOptions() {
		return searchOptions;
	}

	public JTextField getSearchValueTextField() {
		return searchValueTextField;
	}

	public JButton getSearchButton() {
		return searchButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getSelectButton() {
		return selectButton;
	}
}
