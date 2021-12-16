package com.views.login;

import com.utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class CreateAccountFirstLoginDialog extends JDialog {
	// Constants
	private static final int LEFT_PADDING = 25;
	private static final int MIN_WIDTH = 120;
	private static final int MAX_WIDTH = 250;

	// Components
	private JTextField usernameTextField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;
	private JButton cancelButton;
	private JButton createButton;

	public CreateAccountFirstLoginDialog(JFrame frame) {
		super(frame, "Create Account", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(430, 180));
		initComponents(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		// Username label
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(LEFT_PADDING, 20, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(usernameLabel);

		// Username text field
		usernameTextField = new JTextField("admin");
		usernameTextField.setBounds(155, 20, MAX_WIDTH, Constants.TEXT_HEIGHT);
		usernameTextField.setEditable(false);
		panel.add(usernameTextField);

		// Password label
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(LEFT_PADDING, 60, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(passwordLabel);

		// Password text field
		passwordField = new JPasswordField();
		passwordField.setBounds(155, 60, MAX_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(passwordField);

		// Confirm password label
		JLabel confirmPasswordLabel = new JLabel("Confirm password");
		confirmPasswordLabel.setBounds(LEFT_PADDING, 100, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(confirmPasswordLabel);

		// Confirm password text field
		confirmPasswordField = new JPasswordField();
		confirmPasswordField.setBounds(155, 100, MAX_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(confirmPasswordField);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(130, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(220, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setForeground(Color.WHITE);
		panel.add(createButton);
	}

	public JTextField getUsernameTextField() {
		return usernameTextField;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public JPasswordField getConfirmPasswordField() {
		return confirmPasswordField;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getCreateButton() {
		return createButton;
	}
}
