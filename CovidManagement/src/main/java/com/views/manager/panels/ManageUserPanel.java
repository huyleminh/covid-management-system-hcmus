package com.views.manager.panels;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.manager.dialogs.CreateUserDialog;
import com.views.manager.dialogs.EditUserDialog;
import com.views.manager.dialogs.ViewUserDetailDialog;
import com.views.shared.dialogs.SortDialog;
import com.views.shared.panels.ScrollableTablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ManageUserPanel extends JPanel {
	// Components
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
		editButton.setBounds(550, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		editButton.setBackground(Constants.DARK_YELLOW);
		editButton.setHorizontalTextPosition(JButton.CENTER);
		editButton.setForeground(Color.BLACK);
		add(editButton);

		// ------------------------------------------------------
		EditUserDialog editUserDialog = new EditUserDialog(null);
		editButton.addActionListener((event) -> editUserDialog.setVisible(true));
		// ------------------------------------------------------

		// View detail button
		viewDetailButton = new JButton("View Detail");
		viewDetailButton.setBounds(640, 0, Constants.BUTTON_LARGE_WIDTH, Constants.BUTTON_HEIGHT);
		viewDetailButton.setBackground(Constants.LIGHT_BLUE);
		viewDetailButton.setHorizontalTextPosition(JButton.CENTER);
		viewDetailButton.setForeground(Color.WHITE);
		add(viewDetailButton);

		// ------------------------------------------------------
		ViewUserDetailDialog viewUserDetailDialog = new ViewUserDetailDialog(null);
		viewDetailButton.addActionListener((event) -> viewUserDetailDialog.setVisible(true));
		// ------------------------------------------------------

		// Scrollable table
		final String[] columnNames = {"ID Card", "Full name", "Year of Birth", "Current Status"};
		final int [] columnWidths = {105, 457, 100, 100}; // 780 - 15 - 3
		final int[] columnHorizontalAlignments = {
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

		// ------------------------------------------------------
		SortDialog sortDialog = new SortDialog(null, "Sort User");
		sortButton.addActionListener((event) -> sortDialog.setVisible(true));
		// ------------------------------------------------------

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(700, 550, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setForeground(Color.WHITE);
		add(createButton);

		// ------------------------------------------------------
		CreateUserDialog createUserDialog = new CreateUserDialog(null);
		createButton.addActionListener((event) -> createUserDialog.setVisible(true));
		// ------------------------------------------------------

//		NonEditableTableModel tableModel = (NonEditableTableModel) scrollableTable.getTable().getModel();
//		tableModel.addRow(new String[] {"021234569", "Bạch Minh Khôi", "1997", "F0"});
//		tableModel.addRow(new String[] {"011234568", "Lê Hoàng Anh", "1998", "F1"});
//		tableModel.addRow(new String[] {"011234569", "Lê Minh Huy", "1999", "F1"});
//		tableModel.addRow(new String[] {"011234570", "Nguyễn Nhật Cường", "2000", "F2"});
//		tableModel.addRow(new String[] {"011234571", "Nguyễn Đinh Hồng Phúc", "2001", "F2"});
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
