package com.views.manager.dialogs;

import com.utilities.Constants;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class EditUserDialog extends JDialog {
	// Constants
	private static final int LEFT_PADDING = 20;

	// Radio buttons
	private JRadioButton statusEditableButton;
	private JRadioButton quarantineEditableButton;

	// Components for editing status person panel.
	private JComboBox<String> statusOptions;
	private JButton findButton;
	private JTextField infectiousPersonFullnameTextField;

	// Components for editing quarantine person panel.
	private JComboBox<String> quarantineOptions;
	private JTextField availableSlotsTextField;

	private JButton cancelButton;
	private JButton saveButton;

	public EditUserDialog(JFrame frame) {
		super(frame, "Edit User", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(540, 330));
		initComponents(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		JLabel editTypeLabel = new JLabel("Edit type:");
		editTypeLabel.setBounds(LEFT_PADDING, 20, 100, 30);
		panel.add(editTypeLabel);

		// Current status check box.
		statusEditableButton = new JRadioButton("Status", true);
		statusEditableButton.setBounds(120, 20, Constants.BUTTON_SMALL_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(statusEditableButton);

		// Quarantine location check box.
		quarantineEditableButton = new JRadioButton("Quarantine", true);
		quarantineEditableButton.setBounds(210, 20, Constants.BUTTON_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(quarantineEditableButton);

		// ButtonGroup
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(statusEditableButton);
		buttonGroup.add(quarantineEditableButton);

		initEditStatusPanel(panel);
		initEditQuarantinePanel(panel);

		// Cancel button.
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(185, 290, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Save button.
		saveButton = new JButton("Save");
		saveButton.setBounds(275, 290, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		saveButton.setHorizontalTextPosition(JButton.CENTER);
		saveButton.setBackground(Constants.GREEN);
		saveButton.setForeground(Color.WHITE);
		panel.add(saveButton);
	}

	private void initEditStatusPanel(JPanel panel) {
		// Edit status panel
		JPanel editStatusPanel = new JPanel();
		editStatusPanel.setLayout(null);
		editStatusPanel.setBounds(LEFT_PADDING, 60, 500, 100);
		editStatusPanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(Color.GRAY, 1),
						"Edit Status",
						TitledBorder.LEFT,
						TitledBorder.TOP
				)
		);
		panel.add(editStatusPanel);

		JLabel statusLabel = new JLabel("Status");
		statusLabel.setBounds(10, 20, 150, Constants.TEXT_HEIGHT);
		editStatusPanel.add(statusLabel);

		// status options.
		statusOptions = new JComboBox<>();
		statusOptions.setBounds(160, 20, 330, Constants.BUTTON_HEIGHT);
		editStatusPanel.add(statusOptions);

		// Edit button
		findButton = new JButton("Find");
		findButton.setBounds(410, 60, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		findButton.setBackground(Color.LIGHT_GRAY);
		findButton.setHorizontalTextPosition(JButton.CENTER);
		findButton.setForeground(Color.BLACK);
		editStatusPanel.add(findButton);

		// Full name label
		JLabel infectiousPersonFullnameLabel = new JLabel("Infectious person");
		infectiousPersonFullnameLabel.setBounds(10, 60, 150, Constants.TEXT_HEIGHT);
		editStatusPanel.add(infectiousPersonFullnameLabel);

		// Full name text field
		infectiousPersonFullnameTextField = new JTextField();
		infectiousPersonFullnameTextField.setBounds(160, 60, 240, Constants.TEXT_HEIGHT);
		infectiousPersonFullnameTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		infectiousPersonFullnameTextField.setEditable(false);
		editStatusPanel.add(infectiousPersonFullnameTextField);
	}

	private void initEditQuarantinePanel(JPanel panel) {
		// Edit quarantine panel
		JPanel editQuarantinePanel = new JPanel();
		editQuarantinePanel.setLayout(null);
		editQuarantinePanel.setBounds(LEFT_PADDING, 180, 500, 100);
		editQuarantinePanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(Color.GRAY, 1),
						"Edit Quarantine",
						TitledBorder.LEFT,
						TitledBorder.TOP
				)
		);
		panel.add(editQuarantinePanel);

		JLabel quarantineLabel = new JLabel("Quarantine location");
		quarantineLabel.setBounds(10, 20, 150, Constants.TEXT_HEIGHT);
		editQuarantinePanel.add(quarantineLabel);

		// Quarantine location options.
		quarantineOptions = new JComboBox<>();
		quarantineOptions.setBounds(160, 20, 330, Constants.BUTTON_HEIGHT);
		quarantineOptions.setEnabled(false);
		editQuarantinePanel.add(quarantineOptions);

		// Available slots label.
		JLabel availableSlotsLabel = new JLabel("Available slots");
		availableSlotsLabel.setBounds(10, 60, 150, Constants.TEXT_HEIGHT);
		editQuarantinePanel.add(availableSlotsLabel);

		// Available slots text field.
		availableSlotsTextField = new JTextField();
		availableSlotsTextField.setBounds(160, 60, 80, Constants.TEXT_HEIGHT);
		availableSlotsTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		availableSlotsTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		availableSlotsTextField.setEditable(false);
		editQuarantinePanel.add(availableSlotsTextField);
	}

	public JRadioButton getStatusEditableButton() {
		return statusEditableButton;
	}

	public JRadioButton getQuarantineEditableButton() {
		return quarantineEditableButton;
	}

	public JComboBox<String> getStatusOptions() {
		return statusOptions;
	}

	public JButton getFindButton() {
		return findButton;
	}

	public JTextField getInfectiousPersonFullnameTextField() {
		return infectiousPersonFullnameTextField;
	}

	public JComboBox<String> getQuarantineOptions() {
		return quarantineOptions;
	}

	public JTextField getAvailableSlotsTextField() {
		return availableSlotsTextField;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getSaveButton() {
		return saveButton;
	}
}
