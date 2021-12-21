package com.views.user.panels;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.shared.panels.ScrollableTablePanel;

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
				new JTable(new NonEditableTableModel(columnNames, 0))
		);
		scrollableTable.setRowSelectionAllowed(false);
		scrollableTable.setColumnWidths(columnWidths);
		if (columnHorizontalAlignments != null) {
			scrollableTable.setColumnHorizontalAlignments(columnHorizontalAlignments);
		}

		add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();
		final int verticalScrollbarWidth = scrollableTable.getVerticalScrollBar().getPreferredSize().width;

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(10, 10, tableWidth + verticalScrollbarWidth, 535);
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}

	public void clearDataShowing() {
		((NonEditableTableModel) scrollableTable.getTableModel()).removeAllRows();
	}
}
