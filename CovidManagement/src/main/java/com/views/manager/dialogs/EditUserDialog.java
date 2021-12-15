package com.views.manager.dialogs;

import com.utilities.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class EditUserDialog extends JDialog {
	// Constants
	private static final int LEFT_PADDING = 45;
	private static final int MIN_WIDTH = 160;
	private static final int MAX_WIDTH = 340;

	// Components
	private JCheckBox currentStatusCheckBox;
	private JComboBox<String> currentStatusOptions;
	private JCheckBox quarantineCheckBox;
	private JComboBox<String> quarantineOptions;
	private JTextField availableSlotsTextField;
	private JButton cancelButton;
	private JButton saveButton;

	public EditUserDialog(JFrame frame) {
		super(frame, "Edit User", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(590, 180));
		initComponents(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		// Current status check box.
		currentStatusCheckBox = new JCheckBox("Current status", true);
		currentStatusCheckBox.setBounds(LEFT_PADDING, 20, MIN_WIDTH, Constants.TEXT_HEIGHT);
		currentStatusCheckBox.addItemListener((event) -> {
			currentStatusOptions.setEnabled(event.getStateChange() == ItemEvent.SELECTED);
		});
		panel.add(currentStatusCheckBox);

		// Current status options.
		currentStatusOptions = new JComboBox<>();
		currentStatusOptions.setBounds(205, 20, MAX_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(currentStatusOptions);

		// Quarantine location check box.
		quarantineCheckBox = new JCheckBox("Quarantine location", true);
		quarantineCheckBox.setBounds(LEFT_PADDING, 60, MIN_WIDTH, Constants.TEXT_HEIGHT);
		quarantineCheckBox.addItemListener((event) -> {
			quarantineOptions.setEnabled(event.getStateChange() == ItemEvent.SELECTED);
		});
		panel.add(quarantineCheckBox);

		// Quarantine location options.
		quarantineOptions = new JComboBox<>();
		quarantineOptions.setBounds(205, 60, MAX_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(quarantineOptions);

		// Available slots label.
		JLabel availableSlotsLabel = new JLabel("Available slots");
		availableSlotsLabel.setBounds(67, 100, 138, Constants.TEXT_HEIGHT);
		panel.add(availableSlotsLabel);

		// Available slots text field.
		availableSlotsTextField = new JTextField("0 ");
		availableSlotsTextField.setBounds(205, 100, 80, Constants.TEXT_HEIGHT);
		availableSlotsTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		availableSlotsTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		availableSlotsTextField.setEditable(false);
		panel.add(availableSlotsTextField);

		// Cancel button.
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(205, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
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

		// Save button.
		saveButton = new JButton("Save");
		saveButton.setBounds(295, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		saveButton.setHorizontalTextPosition(JButton.CENTER);
		saveButton.setBackground(Constants.GREEN);
		saveButton.setForeground(Color.WHITE);
		panel.add(saveButton);
		saveButton.addActionListener((event) -> {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure to save this information?", null, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				System.out.println("Save: Yes");
				this.setVisible(false);
			}
			else
				System.out.println("Save: No");
		});
	}

	public JCheckBox getCurrentStatusCheckBox() {
		return currentStatusCheckBox;
	}

	public JComboBox<String> getCurrentStatusOptions() {
		return currentStatusOptions;
	}

	public JCheckBox getQuarantineCheckBox() {
		return quarantineCheckBox;
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
