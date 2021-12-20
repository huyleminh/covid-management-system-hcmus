package com.views.admin.dialogs;

import com.utilities.Constants;
import com.views.shared.panels.PasswordFieldPanel;

import javax.swing.*;
import java.awt.*;

public class CreateAccountDialog extends JDialog {
	// Constants
	private static final int LEFT_PADDING = 45;
	private static final int MIN_WIDTH = 80;
	private static final int MAX_WIDTH = 255;

	// Components
	private JTextField usernameField;
	private PasswordFieldPanel passwordFieldPanel;
	private JComboBox<String> roleComboBox;
	private JButton cancelButton;
	private JButton createButton;

	public CreateAccountDialog(JFrame frame) {
		super(frame);
		this.setTitle("Create Manager Account");

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(435, 180));
		initComponents(panel);

		this.setResizable(false);
		this.setContentPane(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setModal(true);
	}

	private void initComponents(JPanel panel) {
		// Username label
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(LEFT_PADDING, 20, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(usernameLabel);

		// Username text field
		usernameField = new JTextField();
		usernameField.setBounds(135, 20, MAX_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(usernameField);

		// Password label
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(LEFT_PADDING, 60, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(passwordLabel);

		// Password field panel
		passwordFieldPanel = new PasswordFieldPanel();
		passwordFieldPanel.setBounds(135, 60, MAX_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(passwordFieldPanel);

		// Role label
		JLabel roleLabel = new JLabel("Role");
		roleLabel.setBounds(LEFT_PADDING, 100, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(roleLabel);

		// Role combo box
		roleComboBox = new JComboBox<>(new String[] {"Manager"});
		roleComboBox.setBounds(135, 100, MAX_WIDTH / 2, Constants.TEXT_HEIGHT);
		panel.add(roleComboBox);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(135, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(225, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setForeground(Color.WHITE);
		panel.add(createButton);
	}

	public JTextField getUsernameField() {
		return usernameField;
	}

	public PasswordFieldPanel getPasswordFieldPanel() {
		return passwordFieldPanel;
	}

	public JComboBox<String> getRoleComboBox() {
		return roleComboBox;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getCreateButton() {
		return createButton;
	}
}
