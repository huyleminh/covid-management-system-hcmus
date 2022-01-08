package covid_management.views.admin.panels;

import shared.components.NonEditableTableModel;
import shared.components.panels.ScrollableTablePanel;
import shared.utilities.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class QuarantineManagementPanel extends JPanel {
	// Components
	private JButton editButton;
	private JButton createButton;
	private ScrollableTablePanel scrollableTable;

	public QuarantineManagementPanel() {
		super();

		setLayout(null);
		initComponents();
	}

	private void initComponents() {
		// Edit button
		editButton = new JButton("Edit");
		editButton.setBounds(610, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		editButton.setBackground(Constants.DARK_YELLOW);
		editButton.setHorizontalTextPosition(JButton.CENTER);
		editButton.setForeground(Color.BLACK);
		add(editButton);

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(700, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setForeground(Color.WHITE);
		add(createButton);


		// Scrollable table
		final String[] columnNames = {"id", "Quarantine Location Name", "Capacity", "Current Slots"};
		final int[] columnWidths = {0, 562, 100, 100};
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
		scrollableTable.setBounds(0, 40, tableWidth + verticalScrollbarWidth, 540);
	}

	public JButton getEditButton() {
		return editButton;
	}

	public JButton getCreateButton() {
		return createButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}
}
