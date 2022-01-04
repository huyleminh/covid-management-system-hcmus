package com.views.login;

import com.utilities.Constants;
import com.views.shared.panels.PasswordFieldPanel;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
	// Constants
	private static final int LEFT_PADDING = 50;
	private static final int MIN_WIDTH = 80;
	private static final int MAX_WIDTH = 250;

	// Components
	private JTextField usernameField;
	private JLabel passwordLabel;
	private PasswordFieldPanel passwordFieldPanel;
	private JButton loginButton;

	// Main frame
	private JFrame mainFrame;

	public LoginView(JFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;

		setLayout(null);
		initComponents();
		setPreferredSize(new Dimension(Constants.LOGIN_VIEW_WIDTH, Constants.LOGIN_VIEW_HEIGHT));
	}

	private void initComponents() {
		// username label
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(LEFT_PADDING, 30, MIN_WIDTH, Constants.TEXT_HEIGHT);
		add(usernameLabel);

		// username text field
		usernameField = new JTextField();
		usernameField.setBounds(LEFT_PADDING, 60, MAX_WIDTH, Constants.TEXT_HEIGHT);
		usernameField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		add(usernameField);

		// password label
		passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(LEFT_PADDING, 100, MIN_WIDTH, Constants.TEXT_HEIGHT);
		passwordLabel.setVisible(false);
		add(passwordLabel);

		passwordFieldPanel = new PasswordFieldPanel(MAX_WIDTH);
		passwordFieldPanel.setBounds(LEFT_PADDING, 130, MAX_WIDTH, Constants.TEXT_HEIGHT);
		passwordFieldPanel.setVisible(false);
		add(passwordFieldPanel);

		// login button
		loginButton = new JButton("Next");
		loginButton.setActionCommand("Next");
		loginButton.setBounds(LEFT_PADDING, 100, MAX_WIDTH, Constants.BUTTON_HEIGHT);
		loginButton.setBackground(Constants.LIGHT_BLUE);
		loginButton.setForeground(Color.WHITE);
		add(loginButton);
	}

	public void displayBeforeValidatingUsername() {
		usernameField.setText("");
		passwordFieldPanel.getPasswordField().setText("");
		passwordFieldPanel.setPasswordVisible(false);

		passwordLabel.setVisible(false);
		passwordFieldPanel.setVisible(false);

		loginButton.setText("Next");
		loginButton.setActionCommand("Next");
		loginButton.setBounds(LEFT_PADDING, 100, MAX_WIDTH, Constants.BUTTON_HEIGHT);
	}

	public void displayAfterValidatingUsername() {
		passwordFieldPanel.getPasswordField().setText("");

		passwordLabel.setVisible(true);
		passwordFieldPanel.setVisible(true);
		passwordFieldPanel.setPasswordVisible(false);

		loginButton.setText("Login");
		loginButton.setActionCommand("Login");
		loginButton.setBounds(LEFT_PADDING, 180, MAX_WIDTH, Constants.BUTTON_HEIGHT);
	}

	public void display() {
		mainFrame.setVisible(false);
		mainFrame.setResizable(true);

		displayBeforeValidatingUsername();

		mainFrame.setTitle("Login");
		mainFrame.setResizable(false);
		mainFrame.setContentPane(this);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public JTextField getUsernameField() {
		return usernameField;
	}

	public JPasswordField getPasswordField() {
		return passwordFieldPanel.getPasswordField();
	}

	public JButton getLoginButton() {
		return loginButton;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}
}
