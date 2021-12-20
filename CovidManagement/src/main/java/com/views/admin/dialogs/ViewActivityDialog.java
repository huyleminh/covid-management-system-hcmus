package com.views.admin.dialogs;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.shared.panels.ScrollableTablePanel;

import javax.swing.*;
import java.awt.*;

public class ViewActivityDialog extends JDialog {
	// Components
	private JTextField managerUsernameTextField;
	private ScrollableTablePanel scrollableTable;

	public ViewActivityDialog(JFrame frame) {
		super(frame);
		this.setTitle("View Activity");

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(665, 500));
		initComponents(panel);

		this.setResizable(false);
		this.setContentPane(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setModal(true);
	}

	private void initComponents(JPanel panel) {
		// Manager username text field
		managerUsernameTextField = new JTextField();
		managerUsernameTextField.setBounds(10, 10, 645, 30);
		managerUsernameTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		managerUsernameTextField.setEditable(false);
		panel.add(managerUsernameTextField);

		// Scrollable table
		final String[] columnNames = {"Description", "Date"};
		final int[] columnWidths = {480, 149};

		scrollableTable = new ScrollableTablePanel(
				new JTable(new NonEditableTableModel(columnNames, 0))
		);
		scrollableTable.setRowSelectionAllowed(false);
		scrollableTable.setColumnWidths(columnWidths);
		panel.add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();
		final int verticalScrollbarWidth = scrollableTable.getVerticalScrollBar().getPreferredSize().width;

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(10, 50, tableWidth + verticalScrollbarWidth, 440);
	}

	public JTextField getManagerUsernameTextField() {
		return managerUsernameTextField;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}
}
