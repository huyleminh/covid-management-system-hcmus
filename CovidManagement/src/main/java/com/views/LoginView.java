package com.views;

import com.utilities.Constants;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {

	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;

	private static final int LEFT_PADDING = 50;
	private static final int MIN_WIDTH = 80;
	private static final int MAX_WIDTH = 250;


	public LoginView() {
		super();

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
		add(usernameField);

		// password label
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(LEFT_PADDING, 100, MIN_WIDTH, Constants.TEXT_HEIGHT);
		add(passwordLabel);

		// password text field
		passwordField = new JPasswordField();
		passwordField.setBounds(LEFT_PADDING, 130, MAX_WIDTH, Constants.TEXT_HEIGHT);
		add(passwordField);

		// login button
		loginButton = new JButton("Login");
		loginButton.setBounds(LEFT_PADDING, 180, MAX_WIDTH, Constants.BUTTON_HEIGHT);
		loginButton.setForeground(Color.WHITE);
		loginButton.setBackground(Color.BLACK);
		loginButton.setOpaque(true);
		add(loginButton);
	}

	public void display() {
		JFrame loginFrame = new JFrame("Login");
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setResizable(false);
		loginFrame.add(this);
		loginFrame.pack();
		loginFrame.setLocationRelativeTo(null);
		loginFrame.setVisible(true);
	}

	public JTextField getUsernameField() {
		return usernameField;
	}

	public JTextField getPasswordField() {
		return passwordField;
	}

	public JButton getLoginButton() {
		return loginButton;
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			LoginView loginView = new LoginView();
			loginView.display();
		});
	}
}
