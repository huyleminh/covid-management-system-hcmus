package com.views.admin;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.shared.panels.ImagePanel;
import com.views.shared.panels.ScrollableTablePanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ManagerManagementView extends JSplitPane {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			ManagerManagementView managerManagementView = new ManagerManagementView();
			managerManagementView.display();
		});
	}

	// Constants for the table.
	private static final int[] MANAGE_MANAGER_COLUMN_WIDTHS = {0, 613, 150};
	private static final int[] MANAGE_QUARANTINE_COLUMN_WIDTHS = {0, 562, 100, 100};
	private static final int[] MANAGE_QUARANTINE_COLUMN_HORIZONTAL_ALIGNMENTS = {
			DefaultTableCellRenderer.LEFT,
			DefaultTableCellRenderer.LEFT,
			DefaultTableCellRenderer.RIGHT,
			DefaultTableCellRenderer.RIGHT
	};

	// Constants for main frame
	private static final int LEFT_PANE_WIDTH = 202;
	private static final int RIGHT_PANE_WIDTH = 800;

	// Constants for selecting feature.
	public static final int MANAGE_MANAGER = 0;
	public static final int MANAGE_QUARANTINE = 1;

	// Status
	boolean isInitManageQuarantinePane = false;
	boolean isInitManageManagerPane = false;
	int selectingFeature = MANAGE_MANAGER;

	// Main frame.
	JFrame frame;

	// Components at the left pane.
	JPanel leftPane;
	JLabel nameLabel;
	JButton manageManagerButton;
	JButton manageQuarantineButton;
	JButton logoutButton;
	JButton quitButton;

	// Components at the right pane.
	JPanel rightPane;
	JLabel selectingFeatureLabel;
	JButton createButton;
	ScrollableTablePanel scrollableTable;

	// Components for "Manage Manager" feature at the right pane.
	JButton lockOrUnlockButton;
	JButton viewActivityButton;

	// Components for "Manage Quarantine" feature at the right pane.
	JButton editQuarantineButton;

	// Models for the table at the right pane
	NonEditableTableModel manageManagerTableModel;
	NonEditableTableModel manageQuarantineTableModel;

	ManagerManagementView() {
		super();

		setDividerLocation(LEFT_PANE_WIDTH);
		setDividerSize(0);
		setPreferredSize(new Dimension(LEFT_PANE_WIDTH + RIGHT_PANE_WIDTH, Constants.APP_HEIGHT));

		initLeftPaneComponents();
		initRightPaneComponents();

		setLeftComponent(leftPane);
		setRightComponent(rightPane);
	}

	private void initLeftPaneComponents() {
		leftPane = new JPanel();
		leftPane.setLayout(null);
		leftPane.setPreferredSize(new Dimension(LEFT_PANE_WIDTH, Constants.APP_HEIGHT));

		Border lineBorderRightEdge = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK);
		leftPane.setBorder(lineBorderRightEdge);

		ImagePanel profileImage = new ImagePanel(Constants.PROFILE_ICON_FILE_PATH, 128, 128);
		profileImage.setBounds(36, Constants.TOP_PADDING, 128, 128);
		leftPane.add(profileImage);

		nameLabel = new JLabel("Lê Hoàng Anh", SwingConstants.CENTER);
		nameLabel.setBounds(30, 148, 140, 25);
		leftPane.add(nameLabel);

		manageManagerButton = new JButton();
		manageManagerButton.setBounds(0, 190, LEFT_PANE_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		manageManagerButton.setBackground(Constants.DARK_BLUE);
		manageManagerButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		manageManagerButton.setHorizontalTextPosition(JButton.LEFT);
		manageManagerButton.setHorizontalAlignment(JButton.RIGHT);
		manageManagerButton.setText("<html><div align=left width=200px>Manage Manager</div></html>");
		manageManagerButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
		manageManagerButton.setForeground(Color.WHITE);
		leftPane.add(manageManagerButton);

		manageQuarantineButton = new JButton();
		manageQuarantineButton.setBounds(0, 230, LEFT_PANE_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		manageQuarantineButton.setBackground(Constants.LIGHT_BLUE);
		manageQuarantineButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		manageQuarantineButton.setHorizontalTextPosition(JButton.LEFT);
		manageQuarantineButton.setHorizontalAlignment(JButton.RIGHT);
		manageQuarantineButton.setText("<html><div align=left width=200px>Manage Quarantine</div></html>");
		manageQuarantineButton.setForeground(Color.WHITE);
		leftPane.add(manageQuarantineButton);

		logoutButton = new JButton("Log Out");
		logoutButton.setBounds(45, 520, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		logoutButton.setBackground(Constants.LIGHT_BLUE);
		logoutButton.setHorizontalTextPosition(JButton.CENTER);
		logoutButton.setForeground(Color.WHITE);
		leftPane.add(logoutButton);

		quitButton = new JButton("Quit");
		quitButton.setBounds(45, 560, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		quitButton.setBackground(Constants.RED);
		quitButton.setHorizontalTextPosition(JButton.CENTER);
		quitButton.setForeground(Color.WHITE);
		leftPane.add(quitButton);

		manageManagerButton.addActionListener((event) -> {
			setVisibleForManageQuarantinePane(false);

			manageQuarantineButton.setBackground(Constants.LIGHT_BLUE);
			manageQuarantineButton.setIcon(null);
			manageManagerButton.setBackground(Constants.DARK_BLUE);
			manageManagerButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
			selectingFeatureLabel.setText("Manage Manager");
			selectingFeature = MANAGE_MANAGER;

			setVisibleForManageManagerPane(true);
		});

		manageQuarantineButton.addActionListener((event) -> {
			setVisibleForManageManagerPane(false);

			manageManagerButton.setBackground(Constants.LIGHT_BLUE);
			manageManagerButton.setIcon(null);
			manageQuarantineButton.setBackground(Constants.DARK_BLUE);
			manageQuarantineButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
			selectingFeatureLabel.setText("Manage Quarantine");
			selectingFeature = MANAGE_QUARANTINE;

			if (!isInitManageQuarantinePane)
				initManageQuarantinePane();

			setVisibleForManageQuarantinePane(true);
		});
	}

	private void initRightPaneComponents() {
		rightPane = new JPanel();
		rightPane.setLayout(null);
		rightPane.setPreferredSize(new Dimension(RIGHT_PANE_WIDTH, Constants.APP_HEIGHT));

		selectingFeatureLabel = new JLabel("Manage Manager", SwingConstants.CENTER);
		selectingFeatureLabel.setBounds(0, 0, RIGHT_PANE_WIDTH, Constants.TEXT_LARGE_HEIGHT);
		selectingFeatureLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		rightPane.add(selectingFeatureLabel);

		initManageManagerPane();
//		initManageQuarantinePane();
	}

	private void initManageManagerPane() {
		isInitManageManagerPane = true;

		lockOrUnlockButton = new JButton("Lock/Unlock");
		lockOrUnlockButton.setBounds(410, 50, Constants.BUTTON_LARGE_WIDTH, Constants.BUTTON_HEIGHT);
		lockOrUnlockButton.setBackground(Constants.RED);
		lockOrUnlockButton.setHorizontalTextPosition(JButton.CENTER);
		lockOrUnlockButton.setForeground(Color.WHITE);
		rightPane.add(lockOrUnlockButton);

		viewActivityButton = new JButton("View Activity");
		viewActivityButton.setBounds(560, 50, Constants.BUTTON_LARGE_WIDTH, Constants.BUTTON_HEIGHT);
		viewActivityButton.setBackground(Constants.LIGHT_BLUE);
		viewActivityButton.setHorizontalTextPosition(JButton.CENTER);
		viewActivityButton.setForeground(Color.WHITE);
		rightPane.add(viewActivityButton);

		createButton = new JButton("Create");
		createButton.setBounds(710, 50, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setForeground(Color.WHITE);
		rightPane.add(createButton);


		final String[] columnNames = {"id", "Username", "Status"};

		manageManagerTableModel = new NonEditableTableModel(columnNames, 0);
		scrollableTable = new ScrollableTablePanel(
				new JTable(manageManagerTableModel),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		scrollableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollableTable.setColumnWidths(MANAGE_MANAGER_COLUMN_WIDTHS);
		rightPane.add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();
		final int verticalScrollbarWidth = scrollableTable.getVerticalScrollBar().getPreferredSize().width;

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(Constants.HORIZONTAL_PADDING, 90, tableWidth + verticalScrollbarWidth, 500);
	}

	private void initManageQuarantinePane() {
		isInitManageQuarantinePane = true;

		editQuarantineButton = new JButton("Edit");
		editQuarantineButton.setBounds(620, 50, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		editQuarantineButton.setBackground(Constants.DARK_YELLOW);
		editQuarantineButton.setHorizontalTextPosition(JButton.CENTER);
		editQuarantineButton.setForeground(Color.BLACK);
		rightPane.add(editQuarantineButton);

		final String[] columnNames = {"id", "Quarantine Location Name", "Capacity", "Current Slots"};
		manageQuarantineTableModel = new NonEditableTableModel(columnNames, 0);
	}

	private void swapTableModel() {
		if (selectingFeature == MANAGE_MANAGER) {
			scrollableTable.getTable().setModel(manageManagerTableModel);
			scrollableTable.setColumnWidths(MANAGE_MANAGER_COLUMN_WIDTHS);
		} else if (selectingFeature == MANAGE_QUARANTINE && isInitManageQuarantinePane) {
			scrollableTable.getTable().setModel(manageQuarantineTableModel);
			scrollableTable.setColumnWidths(MANAGE_QUARANTINE_COLUMN_WIDTHS);
			scrollableTable.setColumnHorizontalAlignments(MANAGE_QUARANTINE_COLUMN_HORIZONTAL_ALIGNMENTS);
		}
	}

	private void setVisibleForManageManagerPane(boolean status) {
		if (isInitManageManagerPane) {
			lockOrUnlockButton.setVisible(status);
			viewActivityButton.setVisible(status);
			swapTableModel();
		}
	}

	private void setVisibleForManageQuarantinePane(boolean status) {
		if (isInitManageQuarantinePane) {
			editQuarantineButton.setVisible(status);
			swapTableModel();
		}
	}

	public void display() {
		frame = new JFrame("Admin - Manage Manager");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void setSelectingFeature(int selectingFeature) {
		this.selectingFeature = selectingFeature;
	}

	public int getSelectingFeature() {
		return selectingFeature;
	}

	public JFrame getFrame() {
		return frame;
	}

	public JLabel getNameLabel() {
		return nameLabel;
	}

	public JButton getManageManagerButton() {
		return manageManagerButton;
	}

	public JButton getManageQuarantineButton() {
		return manageQuarantineButton;
	}

	public JButton getLogoutButton() {
		return logoutButton;
	}

	public JButton getQuitButton() {
		return quitButton;
	}

	public JLabel getSelectingFeatureLabel() {
		return selectingFeatureLabel;
	}

	public JButton getCreateButton() {
		return createButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}

	public JButton getLockOrUnlockButton() {
		return lockOrUnlockButton;
	}

	public JButton getViewActivityButton() {
		return viewActivityButton;
	}

	public JButton getEditQuarantineButton() {
		return editQuarantineButton;
	}
}

