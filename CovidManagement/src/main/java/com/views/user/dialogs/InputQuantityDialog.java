package com.views.user.dialogs;

import com.utilities.Constants;
import com.views.shared.panels.NumberFieldWithButton;

import javax.swing.*;
import java.awt.*;

public class InputQuantityDialog extends JDialog {
	// Components
	private NumberFieldWithButton quantityField;
	private JButton cancelButton;
	private JButton okButton;

	public InputQuantityDialog(JFrame frame, int min, int max) {
		super(frame, "Input Quantity", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(230, 100));
		initComponents(panel, min, max);

		setAlwaysOnTop(true);
		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel, int min, int max) {
		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setBounds(20, 20, 80, 30);
		panel.add(quantityLabel);

		quantityField = new NumberFieldWithButton(min, max, 110);
		quantityField.setBounds(100, 20, 110, 30);
		panel.add(quantityField);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(30, 60, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Add button
		okButton = new JButton("Ok");
		okButton.setBounds(120, 60, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		okButton.setHorizontalTextPosition(JButton.CENTER);
		okButton.setBackground(Constants.LIGHT_BLUE);
		okButton.setForeground(Color.WHITE);
		panel.add(okButton);
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getOkButton() {
		return okButton;
	}

	public int getQuantity() {
		return quantityField.getValue();
	}

	public void setRange(int min, int max) {
		quantityField.setRange(min, max);
	}
}
