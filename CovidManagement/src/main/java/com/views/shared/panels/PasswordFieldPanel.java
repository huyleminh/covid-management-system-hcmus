package com.views.shared.panels;

import com.utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class PasswordFieldPanel extends JPanel {
	// Constants
	private static final Icon SHOW_PASSWORD_ICON = new ImageIcon(Constants.SHOW_PASSWORD_ICON_FILE_PATH);
	private static final Icon HIDE_PASSWORD_ICON = new ImageIcon(Constants.HIDE_PASSWORD_ICON_FILE_PATH);
	private static final char HIDE_PASSWORD_ECHO_CHAR = (char) UIManager.get("PasswordField.echoChar");
	private static final char SHOW_PASSWORD_ECHO_CHAR = (char) 0;  // null

	private JPasswordField passwordField;
	private JToggleButton visibilityPasswordToggle;

	public PasswordFieldPanel() {
		super();

		setLayout(null);
		setPreferredSize(new Dimension(250, 30));

		initComponents();

		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		setBackground(passwordField.getBackground());
		passwordField.setBorder(null);
	}

	private void initComponents() {
		// password text field
		passwordField = new JPasswordField();
		passwordField.setBounds(1, 1, 217, Constants.TEXT_HEIGHT - 2);
		add(passwordField);

		// Visibility password toggle
		visibilityPasswordToggle = new JToggleButton(HIDE_PASSWORD_ICON, false);
		visibilityPasswordToggle.setBounds(223, 3, 24, 24);
		visibilityPasswordToggle.setOpaque(false);
		visibilityPasswordToggle.setContentAreaFilled(false);
		visibilityPasswordToggle.setBorderPainted(false);
		add(visibilityPasswordToggle);

		// Add action for the "visibility password toggle" button.
		visibilityPasswordToggle.addActionListener((event) -> {
			setPasswordVisible(visibilityPasswordToggle.isSelected());
		});
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public void setPasswordVisible(boolean isVisible) {
		if (isVisible) {
			visibilityPasswordToggle.setSelected(true);
			visibilityPasswordToggle.setIcon(SHOW_PASSWORD_ICON);
			passwordField.setEchoChar(SHOW_PASSWORD_ECHO_CHAR);
		} else {
			visibilityPasswordToggle.setSelected(false);
			visibilityPasswordToggle.setIcon(HIDE_PASSWORD_ICON);
			passwordField.setEchoChar(HIDE_PASSWORD_ECHO_CHAR);
		}
	}
}
