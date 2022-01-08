package covid_management.views.user.panels;

import shared.components.NonEditableTableModel;
import shared.components.panels.ScrollableTablePanel;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DebtPaymentPanel extends JPanel {
	// Components
//	private JLabel maximumDebtValueLabel;
	private JLabel totalDebtValueLabel;
	private JButton payButton;
	private ScrollableTablePanel scrollableTable;

	public DebtPaymentPanel() {
		super();

		setLayout(null);
		initComponents();
	}

	private void initComponents() {
		// Maximum debt pane
		JPanel maximumDebtPane = new JPanel();
		maximumDebtPane.setLayout(null);
		maximumDebtPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		maximumDebtPane.setBounds(0, 0, 255, Constants.TEXT_HEIGHT);
		add(maximumDebtPane);

		// Maximum debt label
		JLabel maximumDebtLabel = new JLabel("Maximum Debt", SwingConstants.LEFT);
		maximumDebtLabel.setBounds(5, 0, 100, Constants.TEXT_HEIGHT);
		maximumDebtPane.add(maximumDebtLabel);

		// Maximum debt value
		JLabel maximumDebtValueLabel = new JLabel(
				UtilityFunctions.formatMoneyVND(Constants.MAX_DEBT) + " VND",
				SwingConstants.RIGHT
		);
		maximumDebtValueLabel.setBounds(110, 0, 140, Constants.TEXT_HEIGHT);
		maximumDebtPane.add(maximumDebtValueLabel);

		JPanel totalDebtPane = new JPanel();
		totalDebtPane.setLayout(null);
		totalDebtPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		totalDebtPane.setBounds(430, 0, 255, Constants.TEXT_HEIGHT);
		add(totalDebtPane);

		// Total debt label
		JLabel totalDebtLabel = new JLabel("Total Debt", SwingConstants.LEFT);
		totalDebtLabel.setBounds(5, 0, 100, Constants.TEXT_HEIGHT);
		totalDebtPane.add(totalDebtLabel);

		// Total debt value
		totalDebtValueLabel = new JLabel("", SwingConstants.RIGHT);
		totalDebtValueLabel.setBounds(110, 0, 140, Constants.TEXT_HEIGHT);
		totalDebtPane.add(totalDebtValueLabel);

		// Pay button
		payButton = new JButton("Pay");
		payButton.setBounds(700, 0, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		payButton.setBackground(Constants.GREEN);
		payButton.setHorizontalTextPosition(JButton.CENTER);
		payButton.setForeground(Color.WHITE);
		add(payButton);

		// Scrollable table
		final String[] columnNames = {"Total Debt (VND)", "Date"};
		final int[] columnWidths = {381, 381};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.LEFT
		};

		NonEditableTableModel tableModel = new NonEditableTableModel(columnNames, 0);
		scrollableTable = new ScrollableTablePanel(new JTable(tableModel));
		scrollableTable.setRowSelectionAllowed(false);
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

	public JLabel getTotalDebtValueLabel() {
		return totalDebtValueLabel;
	}

	public int getTotalDebtValue() {
		int length = totalDebtValueLabel.getText().length();
		String valueFormatted = totalDebtValueLabel.getText().substring(0, length - 4);
		return Integer.parseInt(valueFormatted.replaceAll("\\.", ""));
	}

	public JButton getPayButton() {
		return payButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}
}
