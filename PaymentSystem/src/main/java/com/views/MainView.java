package com.views;

import com.models.NonEditableTableModel;
import com.utilities.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class MainView extends JPanel {
	private static final String[] COLUMN_NAMES = {"ID Card", "User name", "Price (VND)", "Date"};
	private static final int[] COLUMN_WIDTHS = {105, 520, 115, 150};
	private static final int INDEX_OF_PRICE_COLUMN = 2;

	// The value of this variable is calculated inside the initScrollableTable() method.
	// It always has a value before this JPanel is set size.
	private int width;

	JLabel bankIdValueLabel;
	JLabel balanceValueLabel;
	JTable table;

	public MainView() {
		super();

		setLayout(null);
		initComponents();
		setPreferredSize(new Dimension(width, Constants.APP_HEIGHT));
	}

	private void initComponents() {
		initBankInfoPane();
		initScrollableTable();
	}

	private void initBankInfoPane() {
		// Bank id pane
		JPanel bankIdPane = new JPanel();
		bankIdPane.setLayout(null);
		bankIdPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		bankIdPane.setBounds(Constants.HORIZONTAL_PADDING, Constants.TOP_PADDING, 220, Constants.TEXT_HEIGHT);
		add(bankIdPane);

		// Bank id label
		JLabel bankIdLabel = new JLabel("Bank Account ID:", SwingConstants.CENTER);
		bankIdLabel.setBounds(5, 0, 110, Constants.TEXT_HEIGHT);
		bankIdPane.add(bankIdLabel);

		// Bank id value
		bankIdValueLabel = new JLabel("", SwingConstants.CENTER);
		bankIdValueLabel.setBounds(115, 0, 105, Constants.TEXT_HEIGHT);
		bankIdPane.add(bankIdValueLabel);

		// Balance pane
		JPanel balancePane = new JPanel();
		balancePane.setLayout(null);
		balancePane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		balancePane.setBounds(240, Constants.TOP_PADDING, 200, Constants.TEXT_HEIGHT);
		add(balancePane);

		// Balance text label
		JLabel balanceTextLabel = new JLabel("Balance:");
		balanceTextLabel.setBounds(5, 0, 60, Constants.TEXT_HEIGHT);
		balancePane.add(balanceTextLabel);

		// Balance value label
		balanceValueLabel = new JLabel("0", SwingConstants.RIGHT);
		balanceValueLabel.setBounds(70, 0, 90, Constants.TEXT_HEIGHT);
		balancePane.add(balanceValueLabel);

		// Currency code label
		JLabel currencyCodeLabel = new JLabel("VND");
		currencyCodeLabel.setBounds(165, 0, 30, Constants.TEXT_HEIGHT);
		balancePane.add(currencyCodeLabel);
	}

	private void initScrollableTable() {
		// initialize table and scroll pane.
		table = new JTable(new NonEditableTableModel(COLUMN_NAMES, 0));
		JScrollPane scrollPane = new JScrollPane(
				table,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// set the width for each column.
		TableColumn column;
		for (int i = 0; i < COLUMN_WIDTHS.length; i++) {
			column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(COLUMN_WIDTHS[i]);
		}

		// make left alignment for the price column.
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		table.getColumnModel().getColumn(INDEX_OF_PRICE_COLUMN).setCellRenderer(cellRenderer);

		// calculate the width of the table, the scroll pane and this pane.
		final int verticalScrollBarWidth = scrollPane.getVerticalScrollBar().getPreferredSize().width;
		final int tableWidth = calculateTableWidth();
		final int scrollPaneWidth = tableWidth + verticalScrollBarWidth;
		final int scrollPaneHeight = Constants.APP_HEIGHT
				- Constants.TOP_PADDING
				- Constants.BOTTOM_PADDING
				- Constants.TEXT_HEIGHT
				- Constants.COMPONENT_VERTICAL_PADDING;

		this.width = scrollPaneWidth + (2 * Constants.HORIZONTAL_PADDING);

		// set size for table and scroll pane.
		scrollPane.setBounds(Constants.HORIZONTAL_PADDING, 60, scrollPaneWidth, scrollPaneHeight);
		table.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		table.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, 0));
		table.getTableHeader().setPreferredSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		table.getTableHeader().setResizingAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		add(scrollPane);
	}

	private int calculateTableWidth() {
		int tableWidth = COLUMN_WIDTHS.length - 1;
		for (int size : COLUMN_WIDTHS)
			tableWidth += size;

		return tableWidth;
	}

	public JLabel getBankIdValueLabel() {
		return bankIdValueLabel;
	}

	public JLabel getBalanceValueLabel() {
		return balanceValueLabel;
	}

	public JTable getTable() {
		return table;
	}

	public void display() {
		JFrame jFrame = new JFrame("Payment System");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setResizable(false);
		jFrame.setContentPane(this);
		jFrame.pack();
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			MainView mainView = new MainView();
			mainView.display();
		});
	}
}
