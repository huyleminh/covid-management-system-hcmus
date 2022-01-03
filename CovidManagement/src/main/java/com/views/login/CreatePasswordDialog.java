package com.views.login;

import com.utilities.Constants;
import com.views.shared.panels.PasswordFieldPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CreatePasswordDialog extends JDialog {
	// Constants
	private static final int LEFT_PADDING = 25;
	private static final int MIN_WIDTH = 120;
	private static final int MAX_WIDTH = 300;

	// Components
	private PasswordFieldPanel passwordFieldPanel;
	private PasswordFieldPanel confirmPasswordFieldPanel;
	private JButton cancelButton;
	private JButton createButton;

	public CreatePasswordDialog(JFrame frame) {
		super(frame, "Create Password", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(480, 140));

		initComponents(panel);

		setAlwaysOnTop(true);
		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		// Password label
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(LEFT_PADDING, 20, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(passwordLabel);

		passwordFieldPanel = new PasswordFieldPanel(MAX_WIDTH);
		passwordFieldPanel.setBounds(155, 20, MAX_WIDTH, Constants.TEXT_HEIGHT);
		passwordFieldPanel.setVisible(true);
		panel.add(passwordFieldPanel);

		// Confirm password label
		JLabel confirmPasswordLabel = new JLabel("Confirm password");
		confirmPasswordLabel.setBounds(LEFT_PADDING, 60, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(confirmPasswordLabel);

		confirmPasswordFieldPanel = new PasswordFieldPanel(MAX_WIDTH);
		confirmPasswordFieldPanel.setBounds(155, 60, MAX_WIDTH, Constants.TEXT_HEIGHT);
		confirmPasswordFieldPanel.setVisible(true);
		panel.add(confirmPasswordFieldPanel);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(155, 100, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(245, 100, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setForeground(Color.WHITE);
		panel.add(createButton);
	}

	public PasswordFieldPanel getPasswordFieldPanel() {
		return passwordFieldPanel;
	}

	public PasswordFieldPanel getConfirmPasswordFieldPanel() {
		return confirmPasswordFieldPanel;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getCreateButton() {
		return createButton;
	}
}
