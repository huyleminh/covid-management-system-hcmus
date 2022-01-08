package covid_management.views.login;

import covid_management.views.shared.panels.PasswordFieldPanel;
import shared.utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class CreateAdminFirstLoginDialog extends JDialog {
	// Constants
	private static final int LEFT_PADDING = 25;
	private static final int MIN_WIDTH = 120;
	private static final int MAX_WIDTH = 250;

	// Components
	private JTextField usernameTextField;
	private PasswordFieldPanel passwordFieldPanel;
	private PasswordFieldPanel confirmPasswordFieldPanel;
	private JButton createButton;

	private boolean isCreatedAccount = false;

	public CreateAdminFirstLoginDialog(JFrame frame) {
		super(frame, "Create Account", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(430, 180));
		initComponents(panel);

		setAlwaysOnTop(true);
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
		usernameTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		usernameTextField.setEditable(false);
		panel.add(usernameTextField);

		// Password label
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(LEFT_PADDING, 60, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(passwordLabel);

		passwordFieldPanel = new PasswordFieldPanel(MAX_WIDTH);
		passwordFieldPanel.setBounds(155, 60, MAX_WIDTH, Constants.TEXT_HEIGHT);
		passwordFieldPanel.setVisible(true);
		panel.add(passwordFieldPanel);

		// Confirm password label
		JLabel confirmPasswordLabel = new JLabel("Confirm password");
		confirmPasswordLabel.setBounds(LEFT_PADDING, 100, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(confirmPasswordLabel);

		confirmPasswordFieldPanel = new PasswordFieldPanel(MAX_WIDTH);
		confirmPasswordFieldPanel.setBounds(155, 100, MAX_WIDTH, Constants.TEXT_HEIGHT);
		confirmPasswordFieldPanel.setVisible(true);
		panel.add(confirmPasswordFieldPanel);

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(175, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setForeground(Color.WHITE);
		panel.add(createButton);
	}

	public JTextField getUsernameTextField() {
		return usernameTextField;
	}

	public JPasswordField getPasswordField() {
		return passwordFieldPanel.getPasswordField();
	}

	public JPasswordField getConfirmPasswordField() {
		return confirmPasswordFieldPanel.getPasswordField();
	}

	public JButton getCreateButton() {
		return createButton;
	}

	public boolean isCreatedAccount() {
		return isCreatedAccount;
	}

	public void setCreatedAccount(boolean createdAccount) {
		isCreatedAccount = createdAccount;
	}
}
