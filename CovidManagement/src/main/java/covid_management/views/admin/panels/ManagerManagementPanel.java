package covid_management.views.admin.panels;

import shared.components.NonEditableTableModel;
import shared.components.panels.ScrollableTablePanel;
import shared.utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class ManagerManagementPanel extends JPanel {
	// Components
	private JButton lockOrUnlockButton;
	private JButton viewActivityButton;
	private JButton createButton;
	private ScrollableTablePanel scrollableTable;

	public ManagerManagementPanel() {
		super();

		setLayout(null);
		initComponents();
	}

	private void initComponents() {
		// Lock or unlock button
		lockOrUnlockButton = new JButton("Lock/Unlock");
		lockOrUnlockButton.setBounds(400, 0, Constants.BUTTON_LARGE_WIDTH, Constants.BUTTON_HEIGHT);
		lockOrUnlockButton.setBackground(Constants.RED);
		lockOrUnlockButton.setHorizontalTextPosition(JButton.CENTER);
		lockOrUnlockButton.setForeground(Color.WHITE);
		add(lockOrUnlockButton);

		// View activity button
		viewActivityButton = new JButton("View Activity");
		viewActivityButton.setBounds(550, 0, Constants.BUTTON_LARGE_WIDTH, Constants.BUTTON_HEIGHT);
		viewActivityButton.setBackground(Constants.LIGHT_BLUE);
		viewActivityButton.setHorizontalTextPosition(JButton.CENTER);
		viewActivityButton.setForeground(Color.WHITE);
		add(viewActivityButton);

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(700, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setForeground(Color.WHITE);
		add(createButton);

		// Scrollable table
		final String[] columnNames = {"Username", "Status"};
		final int[] columnWidths = {614, 150};

		scrollableTable = new ScrollableTablePanel(
				new JTable(new NonEditableTableModel(columnNames, 0))
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

	public JButton getLockOrUnlockButton() {
		return lockOrUnlockButton;
	}

	public JButton getViewActivityButton() {
		return viewActivityButton;
	}

	public JButton getCreateButton() {
		return createButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}
}
