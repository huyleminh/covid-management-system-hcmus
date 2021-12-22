package com.views.admin.dialogs;

import com.utilities.Constants;
import com.utilities.UtilityFunctions;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;

public class CreateQuarantineDialog extends JDialog {
	// Constants
	private static final int LEFT_PADDING = 45;
	private static final int MIN_WIDTH = 100;
	private static final int MAX_WIDTH = 270;

	// Components
	private JTextField locationNameField;
	private JFormattedTextField capacityField;
	private JButton cancelButton;
	private JButton createButton;

	public CreateQuarantineDialog(JFrame frame) {
		super(frame);
		this.setTitle("Create Quarantine Location");

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(460, 140));
		initComponents(panel);

		this.setResizable(false);
		this.setContentPane(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setModal(true);
	}

	private void initComponents(JPanel panel) {
		// Location name label
		JLabel locationNameLabel = new JLabel("Location name");
		locationNameLabel.setBounds(LEFT_PADDING, 20, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(locationNameLabel);

		// Location name text field
		locationNameField = new JTextField();
		locationNameField.setBounds(145, 20, MAX_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(locationNameField);

		// Capacity label
		JLabel capacityLabel = new JLabel("Capacity");
		capacityLabel.setBounds(LEFT_PADDING, 60, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(capacityLabel);

		// Capacity input number field
		NumberFormatter numberFormatter = UtilityFunctions.getIntegerNumberFormatter(1, 32767, false);
		capacityField = new JFormattedTextField(numberFormatter);
		capacityField.setBounds(145, 60, 100, Constants.TEXT_HEIGHT);
		capacityField.setValue(1);
		capacityField.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(capacityField);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(145, 100, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(235, 100, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setForeground(Color.WHITE);
		panel.add(createButton);
	}

	public JTextField getLocationNameField() {
		return locationNameField;
	}

	public JFormattedTextField getCapacityField() {
		return capacityField;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getCreateButton() {
		return createButton;
	}
}
