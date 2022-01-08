package covid_management.views.manager;

import covid_management.views.manager.panels.ManageNecessariesPanel;
import covid_management.views.manager.panels.ManageUserPanel;
import covid_management.views.manager.panels.StatisticPanel;
import covid_management.views.shared.panels.ImagePanel;
import shared.utilities.Constants;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ManagerView extends JPanel {
	// Main frame.
	final private JFrame mainFrame;

	// Components at the left pane.
	private JLabel nameLabel;
	private JButton manageUserButton;
	private JButton manageNecessariesButton;
	private JButton statisticButton;
	private JButton logoutButton;
	private JButton quitButton;

	// Components at the right pane.
	private ManageUserPanel manageUserPanel;
	private ManageNecessariesPanel manageNecessariesPanel;
	private StatisticPanel statisticPanel;

	public ManagerView(JFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;

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
		nameLabel = new JLabel("", SwingConstants.CENTER);
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
		manageNecessariesButton = new JButton();
		manageNecessariesButton.setBounds(0, 230, Constants.LEFT_PANEL_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		manageNecessariesButton.setBackground(Constants.LIGHT_BLUE);
		manageNecessariesButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		manageNecessariesButton.setHorizontalTextPosition(JButton.LEFT);
		manageNecessariesButton.setHorizontalAlignment(JButton.RIGHT);
		manageNecessariesButton.setText("<html><div align=left width=200px>Manage Necessaries</div></html>");
		manageNecessariesButton.setForeground(Color.WHITE);
		leftPane.add(manageNecessariesButton);

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
		mainFrame.setVisible(false);
		mainFrame.setResizable(true);

		mainFrame.setTitle("Manager");
		mainFrame.setResizable(false);
		mainFrame.setContentPane(this);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public JLabel getNameLabel() {
		return nameLabel;
	}

	public JButton getManageUserButton() {
		return manageUserButton;
	}

	public JButton getManageNecessariesButton() {
		return manageNecessariesButton;
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
