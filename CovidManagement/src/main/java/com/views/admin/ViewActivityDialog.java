package com.views.admin;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.shared.ScrollableTablePane;

import javax.swing.*;
import java.awt.*;

public class ViewActivityDialog extends JDialog {
//	public static void main(String[] args) {
//		try {
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		SwingUtilities.invokeLater(() -> {
//			ManagerManagementView mainView = new ManagerManagementView();
//			CreateAccountDialog createAccountDialog = new CreateAccountDialog(mainView.getFrame());
//			ViewActivityDialog viewActivityDialog = new ViewActivityDialog(mainView.getFrame());
//
//			mainView.getCreateButton().addActionListener((event) -> {
//				createAccountDialog.setVisible(true);
//			});
//			mainView.getViewActivityButton().addActionListener((event) -> {
//				viewActivityDialog.setVisible(true);
//			});
//			mainView.display();
//		});
//	}

	JLabel managerNameValueLabel;
	ScrollableTablePane scrollableTable;

	ViewActivityDialog(JFrame frame) {
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
		JPanel managerNamePanel = new JPanel();
		managerNamePanel.setLayout(null);
		managerNamePanel.setBounds(10, 10, 645, 30);
		managerNamePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		panel.add(managerNamePanel);

		JLabel managerNameLabel = new JLabel("Manager: ", SwingConstants.LEFT);
		managerNameLabel.setBounds(5, 0, 60, 30);
		managerNamePanel.add(managerNameLabel);

		managerNameValueLabel = new JLabel("Lê Hoàng Anh", SwingConstants.LEFT);
		managerNameValueLabel.setBounds(65, 0, 580, 30);
		managerNamePanel.add(managerNameValueLabel);

		final String[] columnNames = {"Description", "Date"};

		NonEditableTableModel tableModel = new NonEditableTableModel(columnNames, 0);
		scrollableTable = new ScrollableTablePane(
				new JTable(tableModel),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		scrollableTable.setRowSelectionAllowed(false);
		scrollableTable.setColumnWidths(new int[]{480, 150});
		panel.add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();
		final int verticalScrollbarWidth = scrollableTable.getVerticalScrollBar().getPreferredSize().width;

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(10, 50, tableWidth + verticalScrollbarWidth, 440);
	}

	public JLabel getManagerNameValueLabel() {
		return managerNameValueLabel;
	}

	public ScrollableTablePane getScrollableTable() {
		return scrollableTable;
	}
}
