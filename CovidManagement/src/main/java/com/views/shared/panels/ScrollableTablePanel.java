package com.views.shared.panels;

import com.utilities.UtilityFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class ScrollableTablePanel extends JScrollPane {
	private final JTable table;
	private int tableWidth = -1;

	public ScrollableTablePanel(JTable table) {
		super(table);
		this.table = table;

		this.table.setFillsViewportHeight(true);
	}

	public ScrollableTablePanel(JTable table, int vsbPolicy, int hsbPolicy) {
		super(table, vsbPolicy, hsbPolicy);
		this.table = table;

		this.table.setFillsViewportHeight(true);
	}

	public void setFillsViewportHeight(boolean fillsViewportHeight) {
		table.setFillsViewportHeight(fillsViewportHeight);
	}

	public void setRowSelectionAllowed(boolean rowSelectionAllowed) {
		table.setRowSelectionAllowed(rowSelectionAllowed);
	}

	public void setSelectionMode(int mode) {
		table.setSelectionMode(mode);
	}

	public void setHeaderResizingAllowed(boolean resizingAllowed) {
		table.getTableHeader().setResizingAllowed(resizingAllowed);
	}

	public void setAutoResizeMode(int mode) {
		table.setAutoResizeMode(mode);
	}

	public void setRowHeight(int rowHeight) {
		table.setRowHeight(rowHeight);
	}

	public void setIntercellSpacing(Dimension intercellSpacing) {
		table.setIntercellSpacing(intercellSpacing);
	}

	public void setHeaderSize(Dimension headerSize) {
		table.getTableHeader().setPreferredSize(headerSize);
	}

	public void setColumnWidths(int[] columnWidths) {
		TableColumn column;

		for (int i = 0; i < columnWidths.length; i++) {
			column = table.getColumnModel().getColumn(i);
			column.setMinWidth(columnWidths[i]);
			column.setMaxWidth(columnWidths[i]);
			column.setWidth(columnWidths[i]);
		}

		tableWidth = UtilityFunctions.sum(columnWidths) + columnWidths.length - 1;
	}

	public void setColumnHorizontalAlignments(int[] horizontalAlignments) {
		TableColumn column;
		DefaultTableCellRenderer cellRenderer;

		for (int i = 0; i < horizontalAlignments.length; i++) {
			column = table.getColumnModel().getColumn(i);

			cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(horizontalAlignments[i]);
			column.setCellRenderer(cellRenderer);
		}
	}

	public int getTableWidth() {
		if (tableWidth == -1)
			tableWidth = table.getWidth() + table.getColumnCount() - 1;
		return tableWidth;
	}

	public JTable getTable() {
		return table;
	}
}
