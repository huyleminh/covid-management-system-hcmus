package com.views.manager.dialogs;

import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.views.shared.panels.ScrollableTablePanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ViewUserDetailDialog extends JDialog {
	// Constants
	private static final int LEFT_PADDING = 10;
	private static final int INFO_LABEL_WIDTH = 150;
	private static final int INFO_TEXT_FIELD_WIDTH = 430;

	// Components for basic information panel.
	private JTextField fullnameTextField;
	private JTextField idCardTextField;
	private JTextField yearBirthTextField;
	private JTextField addressTextField;
	private JTextField currentStatusTextField;
	private JTextField quarantineTextField;
	private JTextField infectiousPersonTextField;

	// Components for involved people list panel.
	private ScrollableTablePanel scrollableTable;

	public ViewUserDetailDialog(JFrame frame) {
		super(frame, "View User Detail", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(620, 640));
		initBasicInfoPanel(panel);
		initInvolvedPeopleListPanel(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initBasicInfoPanel(JPanel panel) {
		// Basic information panel
		JPanel basicInfoPanel = new JPanel();
		basicInfoPanel.setLayout(null);
		basicInfoPanel.setBounds(LEFT_PADDING, 10, 600, 300);
		basicInfoPanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(Color.GRAY, 1),
						"List of people involved",
						TitledBorder.CENTER,
						TitledBorder.TOP
				)
		);
		panel.add(basicInfoPanel);

		Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);

		// Full name label
		JLabel fullnameLabel = new JLabel("Full name");
		fullnameLabel.setBounds(LEFT_PADDING, 20, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(fullnameLabel);

		// Full name text field
		fullnameTextField = new JTextField(" Lê Hoàng Anh");
		fullnameTextField.setBounds(160, 20, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		fullnameTextField.setBorder(lineBorder);
		fullnameTextField.setEditable(false);
		basicInfoPanel.add(fullnameTextField);

		// ID card label
		JLabel idCardLabel = new JLabel("ID card");
		idCardLabel.setBounds(LEFT_PADDING, 60, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(idCardLabel);

		// ID card text field
		idCardTextField = new JTextField(" 123456789012");
		idCardTextField.setBounds(160, 60, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		idCardTextField.setBorder(lineBorder);
		idCardTextField.setEditable(false);
		basicInfoPanel.add(idCardTextField);

		// Year of birth label
		JLabel yearBirthLabel = new JLabel("Year of birth");
		yearBirthLabel.setBounds(LEFT_PADDING, 100, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(yearBirthLabel);

		// Year of birth text field
		yearBirthTextField = new JTextField(" 2001");
		yearBirthTextField.setBounds(160, 100, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		yearBirthTextField.setBorder(lineBorder);
		yearBirthTextField.setEditable(false);
		basicInfoPanel.add(yearBirthTextField);

		// Address label
		JLabel addressLabel = new JLabel("Address");
		addressLabel.setBounds(LEFT_PADDING, 140, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(addressLabel);

		// Address text field
		addressTextField = new JTextField(" 227 Nguyễn Văn Cừ, Quận 5, Thành phố Hồ Chí Minh");
		addressTextField.setBounds(160, 140, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		addressTextField.setBorder(lineBorder);
		addressTextField.setEditable(false);
		basicInfoPanel.add(addressTextField);

		// Current status label
		JLabel currentStatusLabel = new JLabel("Current status");
		currentStatusLabel.setBounds(LEFT_PADDING, 180, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(currentStatusLabel);

		// Current status text field
		currentStatusTextField = new JTextField(" F1");
		currentStatusTextField.setBounds(160, 180, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		currentStatusTextField.setBorder(lineBorder);
		currentStatusTextField.setEditable(false);
		basicInfoPanel.add(currentStatusTextField);

		// Quarantine location label
		JLabel quarantineLabel = new JLabel("Quarantine location");
		quarantineLabel.setBounds(LEFT_PADDING, 220, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(quarantineLabel);

		// Quarantine location text field
		quarantineTextField = new JTextField(" Bệnh viện dã chiến trung tâm Sài Gòn");
		quarantineTextField.setBounds(160, 220, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		quarantineTextField.setBorder(lineBorder);
		quarantineTextField.setEditable(false);
		basicInfoPanel.add(quarantineTextField);

		// Infectious person label
		JLabel infectiousPersonLabel = new JLabel("Infectious person");
		infectiousPersonLabel.setBounds(LEFT_PADDING, 260, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(infectiousPersonLabel);

		// Infectious person text field
		infectiousPersonTextField = new JTextField(" Nguyễn Văn A");
		infectiousPersonTextField.setBounds(160, 260, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		infectiousPersonTextField.setBorder(lineBorder);
		infectiousPersonTextField.setEditable(false);
		basicInfoPanel.add(infectiousPersonTextField);
	}

	private void initInvolvedPeopleListPanel(JPanel panel) {
		// Involved people list panel
		JPanel involvedPeopleListPanel = new JPanel();
		involvedPeopleListPanel.setLayout(null);
		involvedPeopleListPanel.setBounds(LEFT_PADDING, 330, 600, 300);
		involvedPeopleListPanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(Color.GRAY, 1),
						"List of people involved",
						TitledBorder.CENTER,
						TitledBorder.TOP
				)
		);
		panel.add(involvedPeopleListPanel);

		// Scrollable table
		final String[] columnNames = {"ID Card", "Full name", "Year of Birth", "Current Status"};
		final int [] columnWidths = {105, 257, 100, 100}; // 580 - 15 - 3
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
				DefaultTableCellRenderer.RIGHT
		};

		scrollableTable = new ScrollableTablePanel(new JTable(new NonEditableTableModel(columnNames, 0)));
		scrollableTable.setRowSelectionAllowed(false);
		scrollableTable.setColumnWidths(columnWidths);
		scrollableTable.setColumnHorizontalAlignments(columnHorizontalAlignments);
		involvedPeopleListPanel.add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();
		final int verticalScrollbarWidth = scrollableTable.getVerticalScrollBar().getPreferredSize().width;

		scrollableTable.setRowHeight(Constants.TABLE_CELL_HEIGHT);
		scrollableTable.setIntercellSpacing(new Dimension(Constants.TABLE_CELL_HORIZONTAL_PADDING, Constants.TABLE_CELL_VERTICAL_PADDING));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, Constants.TABLE_CELL_HEIGHT));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(LEFT_PADDING, 20, tableWidth + verticalScrollbarWidth, 270);
	}

	public JTextField getFullnameTextField() {
		return fullnameTextField;
	}

	public JTextField getIdCardTextField() {
		return idCardTextField;
	}

	public JTextField getYearBirthTextField() {
		return yearBirthTextField;
	}

	public JTextField getAddressTextField() {
		return addressTextField;
	}

	public JTextField getCurrentStatusTextField() {
		return currentStatusTextField;
	}

	public JTextField getQuarantineTextField() {
		return quarantineTextField;
	}

	public JTextField getInfectiousPersonTextField() {
		return infectiousPersonTextField;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}
}
