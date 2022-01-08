package covid_management.views.admin.dialogs;

import shared.components.NonEditableTableModel;
import shared.components.panels.ScrollableTablePanel;
import shared.utilities.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
		panel.setPreferredSize(new Dimension(900, 560));
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
		managerUsernameTextField.setBounds(10, 10, 875, 30);
		managerUsernameTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		managerUsernameTextField.setEditable(false);
		panel.add(managerUsernameTextField);

		// Scrollable table
		final String[] columnNames = {"Description", "Date"};
		final int[] columnWidths = {1410, 159};  // 470, 159
		final int[] columnHorizontalAlignments = {DefaultTableCellRenderer.LEFT, DefaultTableCellRenderer.CENTER};

		scrollableTable = new ScrollableTablePanel(
				new JTable(new NonEditableTableModel(columnNames, 0)) {
					public boolean getScrollableTracksViewportWidth() {
						return getPreferredSize().width < getParent().getWidth();
					}
				},
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
		);
		scrollableTable.setRowSelectionAllowed(false);
		scrollableTable.setColumnWidths(columnWidths);
		scrollableTable.setColumnHorizontalAlignments(columnHorizontalAlignments);
		panel.add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(10, 50, 875, 500);
	}

	public JTextField getManagerUsernameTextField() {
		return managerUsernameTextField;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}
}
