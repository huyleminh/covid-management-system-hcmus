package covid_management.views.user.dialogs;

import covid_management.views.shared.panels.PasswordFieldPanel;
import shared.utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {
	// Constants
	private static final int LEFT_PADDING = 25;
	private static final int MIN_WIDTH = 150;
	private static final int MAX_WIDTH = 250;

	// Components
	private PasswordFieldPanel currentPasswordFieldPanel;
	private PasswordFieldPanel newPasswordFieldPanel;
	private PasswordFieldPanel confirmNewPasswordFieldPanel;
	private JButton cancelButton;
	private JButton saveButton;

	public ChangePasswordDialog(JFrame frame) {
		super(frame, "Change Password", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(460, 180));

		initComponents(panel);

		setAlwaysOnTop(true);
		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		// Current password label
		JLabel currentPasswordLabel = new JLabel("Current password");
		currentPasswordLabel.setBounds(LEFT_PADDING, 20, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(currentPasswordLabel);

		currentPasswordFieldPanel = new PasswordFieldPanel(MAX_WIDTH);
		currentPasswordFieldPanel.setBounds(185, 20, MAX_WIDTH, Constants.TEXT_HEIGHT);
		currentPasswordFieldPanel.setVisible(true);
		panel.add(currentPasswordFieldPanel);

		// New password label
		JLabel newPasswordLabel = new JLabel("New password");
		newPasswordLabel.setBounds(LEFT_PADDING, 60, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(newPasswordLabel);

		newPasswordFieldPanel = new PasswordFieldPanel(MAX_WIDTH);
		newPasswordFieldPanel.setBounds(185, 60, MAX_WIDTH, Constants.TEXT_HEIGHT);
		newPasswordFieldPanel.setVisible(true);
		panel.add(newPasswordFieldPanel);

		// Confirm new password label
		JLabel confirmNewPasswordLabel = new JLabel("Confirm new password");
		confirmNewPasswordLabel.setBounds(LEFT_PADDING, 100, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(confirmNewPasswordLabel);

		confirmNewPasswordFieldPanel = new PasswordFieldPanel(MAX_WIDTH);
		confirmNewPasswordFieldPanel.setBounds(185, 100, MAX_WIDTH, Constants.TEXT_HEIGHT);
		confirmNewPasswordFieldPanel.setVisible(true);
		panel.add(confirmNewPasswordFieldPanel);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(145, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Create button
		saveButton = new JButton("Save");
		saveButton.setBounds(235, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		saveButton.setHorizontalTextPosition(JButton.CENTER);
		saveButton.setBackground(Constants.GREEN);
		saveButton.setForeground(Color.WHITE);
		panel.add(saveButton);
	}

	public PasswordFieldPanel getCurrentPasswordFieldPanel() {
		return currentPasswordFieldPanel;
	}

	public PasswordFieldPanel getNewPasswordFieldPanel() {
		return newPasswordFieldPanel;
	}

	public PasswordFieldPanel getConfirmNewPasswordFieldPanel() {
		return confirmNewPasswordFieldPanel;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getSaveButton() {
		return saveButton;
	}
}
