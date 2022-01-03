package com.views.manager.panels;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.shared.panels.ScrollableTablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ManageUserPanel extends JPanel {
	private static final String[] SEARCH_OPTION_NAMES = {"ID Card", "Full Name"};

	// Components
	private JComboBox<String> searchOptions;
	private JTextField searchValueTextField;
	private JButton searchButton;
	private JButton editButton;
	private JButton viewDetailButton;
	private ScrollableTablePanel scrollableTable;
	private JButton sortButton;
	private JButton createButton;

	public ManageUserPanel() {
		super();

		setLayout(null);
		initComponents();
	}

	private void initComponents() {
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(null);
		searchPanel.setBounds(0, 0, 360, 30);
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		add(searchPanel);

		// Search options
		searchOptions = new JComboBox<>(SEARCH_OPTION_NAMES);
		searchOptions.setBounds(0, 0, 100, 30);
		searchPanel.add(searchOptions);

		// Search value text field
		searchValueTextField = new JTextField();
		searchValueTextField.setBounds(105, 1, 250, Constants.TEXT_HEIGHT - 2);
		searchValueTextField.setBorder(null);
		searchPanel.setBackground(searchValueTextField.getBackground());
		searchPanel.add(searchValueTextField);

		// Search button
		searchButton = new JButton("Search");
		searchButton.setBounds(370, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		searchButton.setBackground(Color.LIGHT_GRAY);
		searchButton.setHorizontalTextPosition(JButton.CENTER);
		searchButton.setForeground(Color.BLACK);
		add(searchButton);

		// Edit button
		editButton = new JButton("Edit");
		editButton.setBounds(550, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		editButton.setBackground(Constants.DARK_YELLOW);
		editButton.setHorizontalTextPosition(JButton.CENTER);
		editButton.setForeground(Color.BLACK);
		add(editButton);

		// View detail button
		viewDetailButton = new JButton("View Detail");
		viewDetailButton.setBounds(640, 0, Constants.BUTTON_LARGE_WIDTH, Constants.BUTTON_HEIGHT);
		viewDetailButton.setBackground(Constants.LIGHT_BLUE);
		viewDetailButton.setHorizontalTextPosition(JButton.CENTER);
		viewDetailButton.setForeground(Color.WHITE);
		add(viewDetailButton);

		// Scrollable table
		final String[] columnNames = {"id", "ID Card", "Full Name", "Status", "Quarantine Location", "infectiousUserId"};
		final int [] columnWidths = {0, 105, 250, 85, 320, 0}; // 780 - 15 - 5
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT
		};

		scrollableTable = new ScrollableTablePanel(new JTable(new NonEditableTableModel(columnNames, 0)));
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
		scrollableTable.setBounds(0, 40, tableWidth + verticalScrollbarWidth, 500);

		// Sort button
		sortButton = new JButton("Sort");
		sortButton.setBounds(0, 550, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		sortButton.setBackground(Color.LIGHT_GRAY);
		sortButton.setHorizontalTextPosition(JButton.CENTER);
		sortButton.setForeground(Color.BLACK);
		add(sortButton);

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(700, 550, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setForeground(Color.WHITE);
		add(createButton);
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

	public JButton getEditButton() {
		return editButton;
	}

	public JButton getViewDetailButton() {
		return viewDetailButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}

	public JButton getSortButton() {
		return sortButton;
	}

	public JButton getCreateButton() {
		return createButton;
	}
}
