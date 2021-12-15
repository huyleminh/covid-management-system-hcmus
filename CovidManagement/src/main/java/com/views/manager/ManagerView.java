package com.views.manager;

import com.utilities.Constants;
import com.views.manager.panels.ManageNecessariesPanel;
import com.views.manager.panels.ManageUserPanel;
import com.views.manager.panels.StatisticPanel;
import com.views.shared.panels.ImagePanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class ManagerView extends JPanel {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			ManagerView managerView = new ManagerView();
			managerView.display();
		});
	}

	// Constants for selecting feature.
	public static final int MANAGE_USER = 0;
	public static final int MANAGE_NECESSARIES = 1;
	public static final int STATISTIC = 2;

	// Status
	private int selectingFeature = MANAGE_USER;

	// Main frame.
	private JFrame frame;

	// Components at the left pane.
	private JLabel nameLabel;
	private JButton manageUserButton;
	private JButton managerNecessariesButton;
	private JButton statisticButton;
	private JButton logoutButton;
	private JButton quitButton;

	// Components at the right pane.
	private ManageUserPanel manageUserPanel;
	private ManageNecessariesPanel manageNecessariesPanel;
	private StatisticPanel statisticPanel;

	public ManagerView() {
		super();

		setLayout(null);
		initLeftComponents();
		initRightComponents();
		setPreferredSize(new Dimension(Constants.APP_WIDTH, Constants.APP_HEIGHT));
	}

	private void initLeftComponents() {
		// Left panel
		JPanel leftPane = new JPanel();
		leftPane.setLayout(null);
		leftPane.setBounds(0, 0, Constants.LEFT_PANEL_WIDTH, Constants.APP_HEIGHT);
		add(leftPane);

		Border lineBorderRightEdge = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK);
		leftPane.setBorder(lineBorderRightEdge);

		// Profile image
		ImagePanel profileImage = new ImagePanel(Constants.PROFILE_ICON_FILE_PATH, 128, 128);
		profileImage.setBounds(36, Constants.TOP_PADDING, 128, 128);
		leftPane.add(profileImage);

		// User name label
		nameLabel = new JLabel("Lê Hoàng Anh", SwingConstants.CENTER);
		nameLabel.setBounds(30, 148, 140, 25);
		leftPane.add(nameLabel);

		// Personal information button
		manageUserButton = new JButton();
		manageUserButton.setBounds(0, 190, Constants.LEFT_PANEL_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		manageUserButton.setBackground(Constants.DARK_BLUE);
		manageUserButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		manageUserButton.setHorizontalTextPosition(JButton.LEFT);
		manageUserButton.setHorizontalAlignment(JButton.RIGHT);
		manageUserButton.setText("<html><div align=left width=200px>Manage User</div></html>");
		manageUserButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
		manageUserButton.setForeground(Color.WHITE);
		leftPane.add(manageUserButton);

		// Purchase necessaries button
		managerNecessariesButton = new JButton();
		managerNecessariesButton.setBounds(0, 230, Constants.LEFT_PANEL_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		managerNecessariesButton.setBackground(Constants.LIGHT_BLUE);
		managerNecessariesButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		managerNecessariesButton.setHorizontalTextPosition(JButton.LEFT);
		managerNecessariesButton.setHorizontalAlignment(JButton.RIGHT);
		managerNecessariesButton.setText("<html><div align=left width=200px>Manage Necessaries</div></html>");
		managerNecessariesButton.setForeground(Color.WHITE);
		leftPane.add(managerNecessariesButton);

		// Debt payment button
		statisticButton = new JButton();
		statisticButton.setBounds(0, 270, Constants.LEFT_PANEL_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		statisticButton.setBackground(Constants.LIGHT_BLUE);
		statisticButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		statisticButton.setHorizontalTextPosition(JButton.LEFT);
		statisticButton.setHorizontalAlignment(JButton.RIGHT);
		statisticButton.setText("<html><div align=left width=200px>Statistic</div></html>");
		statisticButton.setForeground(Color.WHITE);
		leftPane.add(statisticButton);

		// Logout button
		logoutButton = new JButton("Log Out");
		logoutButton.setBounds(45, 520, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		logoutButton.setBackground(Constants.LIGHT_BLUE);
		logoutButton.setHorizontalTextPosition(JButton.CENTER);
		logoutButton.setForeground(Color.WHITE);
		leftPane.add(logoutButton);

		// Quit button
		quitButton = new JButton("Quit");
		quitButton.setBounds(45, 560, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		quitButton.setBackground(Constants.RED);
		quitButton.setHorizontalTextPosition(JButton.CENTER);
		quitButton.setForeground(Color.WHITE);
		leftPane.add(quitButton);

		manageUserButton.addActionListener((event) -> {
			manageNecessariesPanel.setVisible(false);
			statisticPanel.setVisible(false);

			managerNecessariesButton.setBackground(Constants.LIGHT_BLUE);
			managerNecessariesButton.setIcon(null);
			statisticButton.setBackground(Constants.LIGHT_BLUE);
			statisticButton.setIcon(null);
			manageUserButton.setBackground(Constants.DARK_BLUE);
			manageUserButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
			selectingFeature = MANAGE_USER;

			manageUserPanel.setVisible(true);
		});

		managerNecessariesButton.addActionListener((event) -> {
			manageUserPanel.setVisible(false);
			statisticPanel.setVisible(false);

			manageUserButton.setBackground(Constants.LIGHT_BLUE);
			manageUserButton.setIcon(null);
			statisticButton.setBackground(Constants.LIGHT_BLUE);
			statisticButton.setIcon(null);
			managerNecessariesButton.setBackground(Constants.DARK_BLUE);
			managerNecessariesButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
			selectingFeature = MANAGE_NECESSARIES;

			manageNecessariesPanel.setVisible(true);
		});

		statisticButton.addActionListener((event) -> {
			manageUserPanel.setVisible(false);
			manageNecessariesPanel.setVisible(false);

			manageUserButton.setBackground(Constants.LIGHT_BLUE);
			manageUserButton.setIcon(null);
			managerNecessariesButton.setBackground(Constants.LIGHT_BLUE);
			managerNecessariesButton.setIcon(null);
			statisticButton.setBackground(Constants.DARK_BLUE);
			statisticButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
			selectingFeature = STATISTIC;

			statisticPanel.setVisible(true);
		});
	}

	private void initRightComponents() {
		manageUserPanel = new ManageUserPanel();
		manageUserPanel.setBounds(210, 10, 780, 580);
		add(manageUserPanel);

		manageNecessariesPanel = new ManageNecessariesPanel();
		manageNecessariesPanel.setBounds(210, 10, 780, 580);
		manageNecessariesPanel.setVisible(false);
		add(manageNecessariesPanel);

		statisticPanel = new StatisticPanel();
		statisticPanel.setBounds(210, 10, 780, 580);
		statisticPanel.setVisible(false);
		add(statisticPanel);
	}

	public void display() {
		frame = new JFrame("Manager");
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

	public JButton getManageUserButton() {
		return manageUserButton;
	}

	public JButton getManagerNecessariesButton() {
		return managerNecessariesButton;
	}

	public JButton getStatisticButton() {
		return statisticButton;
	}

	public JButton getLogoutButton() {
		return logoutButton;
	}

	public JButton getQuitButton() {
		return quitButton;
	}

	public ManageUserPanel getManageUserPanel() {
		return manageUserPanel;
	}

	public ManageNecessariesPanel getManageNecessariesPanel() {
		return manageNecessariesPanel;
	}

	public StatisticPanel getStatisticPanel() {
		return statisticPanel;
	}
}
