package com.views.manager.panels;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.manager.dialogs.CreateNecessariesDialog;
import com.views.manager.dialogs.EditNecessariesDialog;
import com.views.shared.dialogs.FilterNecessariesDialog;
import com.views.shared.dialogs.SortDialog;
import com.views.shared.panels.ScrollableTablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ManageNecessariesPanel extends JPanel {
	// Components
	private JTextField searchValueTextField;
	private JButton searchButton;
	private JButton editButton;
	private JButton removeButton;
	private ScrollableTablePanel scrollableTable;
	private JButton sortButton;
	private JButton filterButton;
	private JButton createButton;

	public ManageNecessariesPanel() {
		super();

		setLayout(null);
		initComponents();
	}

	private void initComponents() {
		// Search value text field
		searchValueTextField = new JTextField();
		searchValueTextField.setBounds(0, 0, 350, Constants.TEXT_HEIGHT);
		searchValueTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		add(searchValueTextField);

		// Search button
		searchButton = new JButton("Search");
		searchButton.setBounds(360, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		searchButton.setBackground(Color.LIGHT_GRAY);
		searchButton.setHorizontalTextPosition(JButton.CENTER);
		searchButton.setForeground(Color.BLACK);
		add(searchButton);

		// Edit button
		editButton = new JButton("Edit");
		editButton.setBounds(580, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		editButton.setBackground(Constants.DARK_YELLOW);
		editButton.setHorizontalTextPosition(JButton.CENTER);
		editButton.setForeground(Color.BLACK);
		add(editButton);

		// ---------------------------------------------------------
		EditNecessariesDialog editNecessariesDialog = new EditNecessariesDialog(null);
		editButton.addActionListener((event) -> editNecessariesDialog.setVisible(true));
		// ---------------------------------------------------------

		// View detail button
		removeButton = new JButton("Remove");
		removeButton.setBounds(670, 0, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		removeButton.setBackground(Constants.RED);
		removeButton.setHorizontalTextPosition(JButton.CENTER);
		removeButton.setForeground(Color.WHITE);
		add(removeButton);

		// Scrollable table
		final String[] columnNames = {"id", "Necessaries Name", "Limited Quantity", "Start Date", "End Date", "Price (VND)"};
		final int [] columnWidths = {0, 370, 105, 95, 95, 95}; // 780 - 15 - 3
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
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
		scrollableTable.setBounds(0, 40, tableWidth + verticalScrollbarWidth, 500);

		// Sort button
		sortButton = new JButton("Sort");
		sortButton.setBounds(0, 550, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		sortButton.setBackground(Color.LIGHT_GRAY);
		sortButton.setHorizontalTextPosition(JButton.CENTER);
		sortButton.setForeground(Color.BLACK);
		add(sortButton);

		// ---------------------------------------------------------
		SortDialog sortDialog = new SortDialog(null, "Sort Necessaries");
		sortButton.addActionListener((event) -> sortDialog.setVisible(true));
		// ---------------------------------------------------------

		// Filter button
		filterButton = new JButton("Filter");
		filterButton.setBounds(90, 550, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		filterButton.setBackground(Color.LIGHT_GRAY);
		filterButton.setHorizontalTextPosition(JButton.CENTER);
		filterButton.setForeground(Color.BLACK);
		add(filterButton);

		// ---------------------------------------------------------
		FilterNecessariesDialog filterNecessariesDialog = new FilterNecessariesDialog(null, "Filter Necessaries");
		filterButton.addActionListener((event) -> filterNecessariesDialog.setVisible(true));
		// ---------------------------------------------------------

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(700, 550, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setForeground(Color.WHITE);
		add(createButton);

		// ---------------------------------------------------------
		CreateNecessariesDialog createNecessariesDialog = new CreateNecessariesDialog(null);
		createButton.addActionListener((event) -> createNecessariesDialog.setVisible(true));
		// ---------------------------------------------------------

//		NonEditableTableModel tableModel = (NonEditableTableModel) scrollableTable.getTable().getModel();
//		tableModel.addRow(new String[] {"1", "Combo 1 (Thịt, Rau củ, Nước uống)", "8", "2021-12-08", "2021-12-09", "150.000"});
//		tableModel.addRow(new String[] {"2", "Combo 2 (Cá, Rau củ, Nước uống)", "15", "2021-12-08", "2021-12-10", "150.000"});
//		tableModel.addRow(new String[] {"3", "Combo 3 (Thịt, Cá, Trứng, Nước uống)", "20", "2021-12-06", "2021-12-09", "200.000"});
//		tableModel.addRow(new String[] {"4", "Combo 4 (Thịt, Cá, Rau Củ, Nước uống)", "4", "2021-12-09", "2021-12-09", "180.000"});
//		tableModel.addRow(new String[] {"5", "Combo 5 (Thịt, Cá, Trứng, Rau củ, Nước uống)", "12", "2021-12-10", "2021-12-12", "250.000"});
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

	public JButton getRemoveButton() {
		return removeButton;
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

	public JButton getCreateButton() {
		return createButton;
	}
}
