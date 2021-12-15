package com.views.manager.dialogs;

import com.utilities.Constants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CreateUserDialog extends JDialog {
	// Constants
	private static final short MIN_YEAR = 1930;
	private static final short MAX_YEAR = 2021;
	private static final int LEFT_PADDING = 10;
	private static final int INFO_LABEL_WIDTH = 150;
	private static final int INFO_TEXT_FIELD_WIDTH = 430;
	private static final String[] STATUS_OPTION_NAMES = {"F0", "F1", "F2", "F3", "F4"};
	private static final String[] QUARANTINE_OPTION_NAMES = {
			"Khu điều trị thu dung số 1",
			"Bệnh viện dã chiến ngoại ô TP",
			"Bệnh viện dã chiến trung tâm Sài Gòn",
			"Khu điều trị thu dung số 2",
			"Khu điều trị thu dung số 3"
	};

	// Components for basic information panel.
	private JTextField fullnameTextField;
	private JTextField idCardTextField;
	private JComboBox<Short> yearBirthOptions;
	private JTextField addressTextField;
	private JComboBox<String> currentStatusOptions;
	private JComboBox<String> quarantineOptions;

	// Components for infectious person panel.
	private JButton findButton;
	private JButton removeButton;
	private JTextField infectiousPersonFullnameTextField;
	private JTextField infectiousPersonIdCardTextField;
	private JTextField infectiousPersonCurrentStatusTextField;

	// Decision buttons.
	private JButton cancelButton;
	private JButton createButton;

	public CreateUserDialog(JFrame frame) {
		super(frame, "Create User", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(620, 522));

		initBasicInfoPanel(panel);
		initInfectiousPersonPanel(panel);
		initDecisionButtons(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initBasicInfoPanel(JPanel panel) {
		// Basic information panel
		JPanel basicInfoPanel = new JPanel();
		basicInfoPanel.setLayout(null);
		basicInfoPanel.setBounds(10, 10, 600, 260);
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
		basicInfoPanel.add(fullnameTextField);

		// ID card label
		JLabel idCardLabel = new JLabel("ID card");
		idCardLabel.setBounds(LEFT_PADDING, 60, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(idCardLabel);

		// ID card text field
		idCardTextField = new JTextField(" 123456789012");
		idCardTextField.setBounds(160, 60, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		idCardTextField.setBorder(lineBorder);
		basicInfoPanel.add(idCardTextField);

		// Year of birth label
		JLabel yearBirthLabel = new JLabel("Year of birth");
		yearBirthLabel.setBounds(LEFT_PADDING, 100, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(yearBirthLabel);

		// Range of years
		short count = (short) (MAX_YEAR - MIN_YEAR + 1);
		Short[] years = new Short[count];
		for (short i = 0; i < count; i++)
			years[i] = (short) (MIN_YEAR + i);

		// Year of birth text field
		yearBirthOptions = new JComboBox<>(years);
		yearBirthOptions.setBounds(160, 100, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		yearBirthOptions.setBorder(lineBorder);
		basicInfoPanel.add(yearBirthOptions);

		// Address label
		JLabel addressLabel = new JLabel("Address");
		addressLabel.setBounds(LEFT_PADDING, 140, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(addressLabel);

		// Address text field
		addressTextField = new JTextField(" 227 Nguyễn Văn Cừ, Quận 5, Thành phố Hồ Chí Minh");
		addressTextField.setBounds(160, 140, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		addressTextField.setBorder(lineBorder);
		basicInfoPanel.add(addressTextField);

		// Current status label
		JLabel currentStatusLabel = new JLabel("Current status");
		currentStatusLabel.setBounds(LEFT_PADDING, 180, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(currentStatusLabel);

		// Current status text field
		currentStatusOptions = new JComboBox<>(STATUS_OPTION_NAMES);
		currentStatusOptions.setBounds(160, 180, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(currentStatusOptions);

		// Quarantine location label
		JLabel quarantineLabel = new JLabel("Quarantine location");
		quarantineLabel.setBounds(LEFT_PADDING, 220, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(quarantineLabel);

		// Quarantine location text field
		quarantineOptions = new JComboBox<>(QUARANTINE_OPTION_NAMES);
		quarantineOptions.setBounds(160, 220, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		basicInfoPanel.add(quarantineOptions);
	}

	private void initInfectiousPersonPanel(JPanel panel) {
		// Involved people list panel
		JPanel infectiousPersonPanel = new JPanel();
		infectiousPersonPanel.setLayout(null);
		infectiousPersonPanel.setBounds(LEFT_PADDING, 290, 600, 180);
		infectiousPersonPanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(Color.GRAY, 1),
						"Infectious person",
						TitledBorder.CENTER,
						TitledBorder.TOP
				)
		);
		panel.add(infectiousPersonPanel);

		// Edit button
		findButton = new JButton("Find");
		findButton.setBounds(390, 20, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		findButton.setBackground(Color.LIGHT_GRAY);
		findButton.setHorizontalTextPosition(JButton.CENTER);
		findButton.setForeground(Color.BLACK);
		infectiousPersonPanel.add(findButton);

		// ------------------------------------------------
		FindUserDialog findUserDialog = new FindUserDialog(null);
		findButton.addActionListener((event) -> findUserDialog.setVisible(true));
		// ------------------------------------------------

		// View detail button
		removeButton = new JButton("Remove");
		removeButton.setBounds(480, 20, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		removeButton.setBackground(Constants.RED);
		removeButton.setHorizontalTextPosition(JButton.CENTER);
		removeButton.setForeground(Color.WHITE);
		infectiousPersonPanel.add(removeButton);

		Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);

		// Full name label
		JLabel fullnameLabel = new JLabel("Full name");
		fullnameLabel.setBounds(LEFT_PADDING, 60, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		infectiousPersonPanel.add(fullnameLabel);

		// Full name text field
		infectiousPersonFullnameTextField = new JTextField(" Lê Hoàng Anh");
		infectiousPersonFullnameTextField.setBounds(160, 60, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		infectiousPersonFullnameTextField.setBorder(lineBorder);
		infectiousPersonFullnameTextField.setEditable(false);
		infectiousPersonPanel.add(infectiousPersonFullnameTextField);

		// ID card label
		JLabel idCardLabel = new JLabel("ID card");
		idCardLabel.setBounds(LEFT_PADDING, 100, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		infectiousPersonPanel.add(idCardLabel);

		// ID card text field
		infectiousPersonIdCardTextField = new JTextField(" 123456789012");
		infectiousPersonIdCardTextField.setBounds(160, 100, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		infectiousPersonIdCardTextField.setBorder(lineBorder);
		infectiousPersonIdCardTextField.setEditable(false);
		infectiousPersonPanel.add(infectiousPersonIdCardTextField);

		JLabel currentStatusLabel = new JLabel("Current status");
		currentStatusLabel.setBounds(LEFT_PADDING, 140, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		infectiousPersonPanel.add(currentStatusLabel);

		// Current status text field
		infectiousPersonCurrentStatusTextField = new JTextField(" F1");
		infectiousPersonCurrentStatusTextField.setBounds(160, 140, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		infectiousPersonCurrentStatusTextField.setBorder(lineBorder);
		infectiousPersonCurrentStatusTextField.setEditable(false);
		infectiousPersonPanel.add(infectiousPersonCurrentStatusTextField);
	}

	private void initDecisionButtons(JPanel panel) {
		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(225, 480, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);
		cancelButton.addActionListener((event) -> {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure to close?", null, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				System.out.println("Cancel: Yes");
				this.setVisible(false);
			}
			else
				System.out.println("Cancel: No");
		});

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(315, 480, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setForeground(Color.WHITE);
		panel.add(createButton);
		createButton.addActionListener((event) -> {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure to create this user?", null, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				System.out.println("Create: Yes");
				this.setVisible(false);
			}
			else
				System.out.println("Create: No");
		});
	}

	public JTextField getFullnameTextField() {
		return fullnameTextField;
	}

	public JTextField getIdCardTextField() {
		return idCardTextField;
	}

	public JComboBox<Short> getYearBirthOptions() {
		return yearBirthOptions;
	}

	public JTextField getAddressTextField() {
		return addressTextField;
	}

	public JComboBox<String> getCurrentStatusOptions() {
		return currentStatusOptions;
	}

	public JComboBox<String> getQuarantineOptions() {
		return quarantineOptions;
	}

	public JButton getFindButton() {
		return findButton;
	}

	public JButton getRemoveButton() {
		return removeButton;
	}

	public JTextField getInfectiousPersonFullnameTextField() {
		return infectiousPersonFullnameTextField;
	}

	public JTextField getInfectiousPersonIdCardTextField() {
		return infectiousPersonIdCardTextField;
	}

	public JTextField getInfectiousPersonCurrentStatusTextField() {
		return infectiousPersonCurrentStatusTextField;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getCreateButton() {
		return createButton;
	}
}
