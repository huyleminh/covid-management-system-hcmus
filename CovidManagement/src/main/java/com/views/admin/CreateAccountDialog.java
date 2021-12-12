package com.views.admin;

import com.utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class CreateAccountDialog extends JDialog {
//	public static void main(String[] args) {
//		try {
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		SwingUtilities.invokeLater(() -> {
//			ManagerManagementView mainView = new ManagerManagementView();
//			CreateAccountDialog createAccountDialog = new CreateAccountDialog(mainView.getFrame());
//			ViewActivityDialog viewActivityDialog = new ViewActivityDialog(mainView.getFrame());
//
//			mainView.getCreateButton().addActionListener((event) -> {
//				createAccountDialog.setVisible(true);
//			});
//			mainView.getViewActivityButton().addActionListener((event) -> {
//				viewActivityDialog.setVisible(true);
//			});
//			mainView.display();
//		});
//	}

	private static final int LEFT_PADDING = 45;
	private static final int MIN_WIDTH = 80;
	private static final int MAX_WIDTH = 255;

	JTextField usernameField;
	JTextField passwordField;
	JComboBox<String> roleComboBox;
	JButton cancelButton;
	JButton createButton;

	CreateAccountDialog(JFrame frame) {
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
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(LEFT_PADDING, 20, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(usernameLabel);

		// username text field
		usernameField = new JTextField();
		usernameField.setBounds(135, 20, MAX_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(usernameField);

		// password label
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(LEFT_PADDING, 60, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(passwordLabel);

		// password text field
		passwordField = new JTextField();
		passwordField.setBounds(135, 60, MAX_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(passwordField);

		JLabel roleLabel = new JLabel("Role");
		roleLabel.setBounds(LEFT_PADDING, 100, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(roleLabel);

		roleComboBox = new JComboBox<>(new String[] {"Manager"});
		roleComboBox.setBounds(135, 100, MAX_WIDTH / 2, Constants.TEXT_HEIGHT);
		panel.add(roleComboBox);

		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(135, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);
		cancelButton.addActionListener((event) -> {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure to close?", null, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				System.out.println("Cancel: Yes");
				this.setVisible(false);
			}
			else
				System.out.println("Cancel: No");
		});

		createButton = new JButton("Create");
		createButton.setBounds(225, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setForeground(Color.WHITE);
		panel.add(createButton);
		createButton.addActionListener((event) -> {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure to create this user?", null, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				System.out.println("Create: Yes");
				this.setVisible(false);
			}
			else
				System.out.println("Create: No");
		});
	}

	public JTextField getUsernameField() {
		return usernameField;
	}

	public JTextField getPasswordField() {
		return passwordField;
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
