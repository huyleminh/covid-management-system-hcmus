package covid_management.views.user.panels;

import shared.components.NonEditableTableModel;
import shared.components.panels.ScrollableTablePanel;
import shared.utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class UserPersonalInfoTablePanel extends JPanel {
	private ScrollableTablePanel scrollableTable;

	public UserPersonalInfoTablePanel(String[] columnNames, int[] columnWidths) {
		super();
		setLayout(null);
		initTable(columnNames, columnWidths, null);
	}

	public UserPersonalInfoTablePanel(String[] columnNames, int[] columnWidths, int[] columnHorizontalAlignments) {
		super();
		setLayout(null);
		initTable(columnNames, columnWidths, columnHorizontalAlignments);
	}

	private void initTable(String[] columnNames, int[] columnWidths, int[] columnHorizontalAlignments) {
		scrollableTable = new ScrollableTablePanel(
				new JTable(new NonEditableTableModel(columnNames, 0)) {
					public boolean getScrollableTracksViewportWidth() {
						return getPreferredSize().width < getParent().getWidth();
					}
				},
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		scrollableTable.setRowSelectionAllowed(false);
		scrollableTable.setColumnWidths(columnWidths);
		if (columnHorizontalAlignments != null) {
			scrollableTable.setColumnHorizontalAlignments(columnHorizontalAlignments);
		}

		add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(10, 10, 760, 535);
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}

	public void clearDataShowing() {
		((NonEditableTableModel) scrollableTable.getTableModel()).removeAllRows();
	}
}
