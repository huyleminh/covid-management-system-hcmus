package covid_management.views.user.panels;

import shared.components.NonEditableTableModel;
import shared.components.panels.ScrollableTablePanel;
import shared.utilities.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CartPanel extends JPanel {
	// Components
	final private JButton changeQuantityButton;
	final private JButton removeButton;
	final private ScrollableTablePanel scrollableTable;
	final private JLabel totalAmountValueLabel;
	final private JButton checkoutButton;

	public CartPanel() {
		super();
		setLayout(null);

		// Change quantity button
		changeQuantityButton = new JButton("Change Quantity");
		changeQuantityButton.setBounds(510, 10, 140, 30);
		changeQuantityButton.setBackground(Constants.GREEN);
		changeQuantityButton.setHorizontalTextPosition(JButton.CENTER);
		changeQuantityButton.setForeground(Color.WHITE);
		add(changeQuantityButton);

		// Remove button
		removeButton = new JButton("Remove");
		removeButton.setBounds(660, 10, 110, 30);
		removeButton.setBackground(Constants.RED);
		removeButton.setHorizontalTextPosition(JButton.CENTER);
		removeButton.setForeground(Color.WHITE);
		add(removeButton);

		// Scrollable table
		final String[] columnNames = {
				"necessariesId",
				"maxQuantity",
				"Necessaries Name",
				"Quantity",
				"Price (VND)",
				"Total Amount (VND)"
		};
		final int [] columnWidths = {0, 0, 525, 62, 95, 130}; // 745 - 3
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.RIGHT
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
		scrollableTable.setBounds(10, 50, 760, 455);

		// Total amount panel
		JPanel totalAmountPanel = new JPanel();
		totalAmountPanel.setLayout(null);
		totalAmountPanel.setBounds(400, 515, 250, 30);
		totalAmountPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		add(totalAmountPanel);

		// Total amount label
		JLabel totalAmountLabel = new JLabel("Total amount", SwingConstants.LEFT);
		totalAmountLabel.setBounds(5, 0, 95, 30);
		totalAmountPanel.add(totalAmountLabel);

		// Total amount value label
		totalAmountValueLabel = new JLabel("", SwingConstants.RIGHT);
		totalAmountValueLabel.setBounds(100, 0, 145, 30);
		totalAmountPanel.add(totalAmountValueLabel);

		// Checkout button
		checkoutButton = new JButton("Checkout");
		checkoutButton.setBounds(660, 515, 110, 30);
		checkoutButton.setBackground(Constants.GREEN);
		checkoutButton.setHorizontalTextPosition(JButton.CENTER);
		checkoutButton.setForeground(Color.WHITE);
		add(checkoutButton);
	}

	public JButton getChangeQuantityButton() {
		return changeQuantityButton;
	}

	public JButton getRemoveButton() {
		return removeButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}

	public JLabel getTotalAmountValueLabel() {
		return totalAmountValueLabel;
	}

	public int getTotalAmountValue() {
		int length = totalAmountValueLabel.getText().length();
		String valueFormatted = totalAmountValueLabel.getText().substring(0, length - 4);
		return Integer.parseInt(valueFormatted.replaceAll("\\.", ""));
	}

	public JButton getCheckoutButton() {
		return checkoutButton;
	}
}
