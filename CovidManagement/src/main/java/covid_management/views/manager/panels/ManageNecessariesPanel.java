package covid_management.views.manager.panels;

import shared.components.NonEditableTableModel;
import shared.components.panels.ScrollableTablePanel;
import shared.utilities.Constants;

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

		// View detail button
		removeButton = new JButton("Remove");
		removeButton.setBounds(670, 0, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		removeButton.setBackground(Constants.RED);
		removeButton.setHorizontalTextPosition(JButton.CENTER);
		removeButton.setForeground(Color.WHITE);
		add(removeButton);

		// Scrollable table
		final String[] columnNames = {
				"necessariesId",
				"Necessaries Name",
				"Quantity",
				"Start Date",
				"End Date",
				"Price (VND)"
		};
		final int [] columnWidths = {0, 525, 60, 150, 150, 80}; // 780 - 15 - 5
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
		};

		scrollableTable = new ScrollableTablePanel(
				new JTable(new NonEditableTableModel(columnNames, 0)) {
					public boolean getScrollableTracksViewportWidth() {
						return getPreferredSize().width < getParent().getWidth();
					}
				},
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
		);
		scrollableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollableTable.setColumnWidths(columnWidths);
		scrollableTable.setColumnHorizontalAlignments(columnHorizontalAlignments);
		add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(0, 40, 780, 500);

		// Sort button
		sortButton = new JButton("Sort");
		sortButton.setBounds(0, 550, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		sortButton.setBackground(Color.LIGHT_GRAY);
		sortButton.setHorizontalTextPosition(JButton.CENTER);
		sortButton.setForeground(Color.BLACK);
		add(sortButton);

		// Filter button
		filterButton = new JButton("Filter");
		filterButton.setBounds(90, 550, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		filterButton.setBackground(Color.LIGHT_GRAY);
		filterButton.setHorizontalTextPosition(JButton.CENTER);
		filterButton.setForeground(Color.BLACK);
		add(filterButton);

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(700, 550, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setForeground(Color.WHITE);
		add(createButton);
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
