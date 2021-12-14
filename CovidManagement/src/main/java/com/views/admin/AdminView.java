package com.views.admin;

import com.utilities.Constants;
import com.views.admin.dialogs.*;
import com.views.admin.panels.ManagerManagementPanel;
import com.views.admin.panels.QuarantineManagementPanel;
import com.views.shared.panels.ImagePanel;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class AdminView extends JPanel {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			AdminView adminView = new AdminView();

			CreateAccountDialog createAccountDialog = new CreateAccountDialog(adminView.getFrame());
			CreateQuarantineDialog createQuarantineDialog = new CreateQuarantineDialog(adminView.getFrame());
			EditQuarantineDialog editQuarantineDialog = new EditQuarantineDialog(adminView.getFrame());
			ViewActivityDialog viewActivityDialog = new ViewActivityDialog(adminView.getFrame());

			adminView.getManagerManagementPanel()
					.getViewActivityButton()
					.addActionListener((event) -> viewActivityDialog.setVisible(true));

			adminView.getManagerManagementPanel()
					.getCreateButton()
					.addActionListener((event) ->	createAccountDialog.setVisible(true));

			adminView.getQuarantineManagementPanel()
					.getEditButton()
					.addActionListener((event) -> editQuarantineDialog.setVisible(true));

			adminView.getQuarantineManagementPanel()
					.getCreateButton()
					.addActionListener((event) -> createQuarantineDialog.setVisible(true));

			adminView.display();
		});
	}

	// Constants for selecting feature.
	public static final int MANAGE_MANAGER = 0;
	public static final int MANAGE_QUARANTINE = 1;

	// Status
	private int selectingFeature = MANAGE_MANAGER;

	// Main frame.
	private JFrame frame;

	// Components at the left pane.
	private JLabel nameLabel;
	private JButton manageManagerButton;
	private JButton manageQuarantineButton;
	private JButton logoutButton;
	private JButton quitButton;

	// Components at the right pane.
	private ManagerManagementPanel managerManagementPanel;
	private QuarantineManagementPanel quarantineManagementPanel;

	public AdminView() {
		super();

		setLayout(null);
		initLeftPaneComponents();
		initRightPaneComponents();
		setPreferredSize(new Dimension(Constants.APP_WIDTH, Constants.APP_HEIGHT));
	}

	private void initLeftPaneComponents() {
		// Left panel
		JPanel leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 200, 600);
		leftPanel.setLayout(null);
		leftPanel.setPreferredSize(new Dimension(Constants.LEFT_PANEL_WIDTH, Constants.APP_HEIGHT));
		add(leftPanel);

		Border lineBorderRightEdge = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK);
		leftPanel.setBorder(lineBorderRightEdge);

		// Profile image
		ImagePanel profileImage = new ImagePanel(Constants.PROFILE_ICON_FILE_PATH, 128, 128);
		profileImage.setBounds(36, Constants.TOP_PADDING, 128, 128);
		leftPanel.add(profileImage);

		// User name label
		nameLabel = new JLabel("Lê Hoàng Anh", SwingConstants.CENTER);
		nameLabel.setBounds(30, 148, 140, 25);
		leftPanel.add(nameLabel);

		// Manage manager button
		manageManagerButton = new JButton();
		manageManagerButton.setBounds(0, 190, Constants.LEFT_PANEL_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		manageManagerButton.setBackground(Constants.DARK_BLUE);
		manageManagerButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		manageManagerButton.setHorizontalTextPosition(JButton.LEFT);
		manageManagerButton.setHorizontalAlignment(JButton.RIGHT);
		manageManagerButton.setText("<html><div align=left width=200px>Manage Manager</div></html>");
		manageManagerButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
		manageManagerButton.setForeground(Color.WHITE);
		leftPanel.add(manageManagerButton);

		// Manage quarantine location button
		manageQuarantineButton = new JButton();
		manageQuarantineButton.setBounds(0, 230, Constants.LEFT_PANEL_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		manageQuarantineButton.setBackground(Constants.LIGHT_BLUE);
		manageQuarantineButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		manageQuarantineButton.setHorizontalTextPosition(JButton.LEFT);
		manageQuarantineButton.setHorizontalAlignment(JButton.RIGHT);
		manageQuarantineButton.setText("<html><div align=left width=200px>Manage Quarantine</div></html>");
		manageQuarantineButton.setForeground(Color.WHITE);
		leftPanel.add(manageQuarantineButton);

		// Logout button
		logoutButton = new JButton("Log Out");
		logoutButton.setBounds(45, 520, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		logoutButton.setBackground(Constants.LIGHT_BLUE);
		logoutButton.setHorizontalTextPosition(JButton.CENTER);
		logoutButton.setForeground(Color.WHITE);
		leftPanel.add(logoutButton);

		// Quit button
		quitButton = new JButton("Quit");
		quitButton.setBounds(45, 560, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		quitButton.setBackground(Constants.RED);
		quitButton.setHorizontalTextPosition(JButton.CENTER);
		quitButton.setForeground(Color.WHITE);
		leftPanel.add(quitButton);

		manageManagerButton.addActionListener((event) -> {
			quarantineManagementPanel.setVisible(false);

			manageQuarantineButton.setBackground(Constants.LIGHT_BLUE);
			manageQuarantineButton.setIcon(null);
			manageManagerButton.setBackground(Constants.DARK_BLUE);
			manageManagerButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
			selectingFeature = MANAGE_MANAGER;

			managerManagementPanel.setVisible(true);
		});

		manageQuarantineButton.addActionListener((event) -> {
			managerManagementPanel.setVisible(false);

			manageManagerButton.setBackground(Constants.LIGHT_BLUE);
			manageManagerButton.setIcon(null);
			manageQuarantineButton.setBackground(Constants.DARK_BLUE);
			manageQuarantineButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
			selectingFeature = MANAGE_QUARANTINE;

			quarantineManagementPanel.setVisible(true);
		});
	}

	private void initRightPaneComponents() {
		// Manager management panel
		managerManagementPanel = new ManagerManagementPanel();
		managerManagementPanel.setBounds(210, 10, 780, 580);
		managerManagementPanel.setVisible(true);
		add(managerManagementPanel);

		// Quarantine management panel
		quarantineManagementPanel = new QuarantineManagementPanel();
		quarantineManagementPanel.setBounds(210, 10, 780, 580);
		quarantineManagementPanel.setVisible(false);
		add(quarantineManagementPanel);
	}

	public void display() {
		frame = new JFrame("Admin");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setContentPane(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void setSelectingFeature(int selectingFeature) {
		this.selectingFeature = selectingFeature;
	}

	public int getSelectingFeature() {
		return selectingFeature;
	}

	public JFrame getFrame() {
		return frame;
	}

	public JLabel getNameLabel() {
		return nameLabel;
	}

	public JButton getManageManagerButton() {
		return manageManagerButton;
	}

	public JButton getManageQuarantineButton() {
		return manageQuarantineButton;
	}

	public JButton getLogoutButton() {
		return logoutButton;
	}

	public JButton getQuitButton() {
		return quitButton;
	}

	public ManagerManagementPanel getManagerManagementPanel() {
		return managerManagementPanel;
	}

	public QuarantineManagementPanel getQuarantineManagementPanel() {
		return quarantineManagementPanel;
	}
}