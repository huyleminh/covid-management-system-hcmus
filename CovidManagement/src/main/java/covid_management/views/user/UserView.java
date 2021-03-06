package covid_management.views.user;

import covid_management.views.shared.panels.ImagePanel;
import covid_management.views.user.panels.DebtPaymentPanel;
import covid_management.views.user.tabbed_panes.PersonalInfoTabbed;
import covid_management.views.user.tabbed_panes.PurchaseNecessariesTabbed;
import shared.utilities.Constants;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UserView extends JPanel {
	// Main frame.
	final private JFrame mainFrame;

	// Components at the left pane.
	private JLabel nameLabel;
	private JButton personalInfoButton;
	private JButton purchaseNecessariesButton;
	private JButton debtPaymentButton;
	private JButton changePasswordButton;
	private JButton logoutButton;
	private JButton quitButton;

	// Components at the right pane.
	private PersonalInfoTabbed personalInfoTabbed;
	private PurchaseNecessariesTabbed purchaseNecessariesTabbed;
	private DebtPaymentPanel debtPaymentPanel;

	public UserView(JFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;

		setLayout(null);
		initLeftPaneComponents();
		initRightPaneComponents();
		setPreferredSize(new Dimension(Constants.APP_WIDTH, Constants.APP_HEIGHT));
	}

	private void initLeftPaneComponents() {
		// Left panel
		JPanel leftPane = new JPanel();
		leftPane.setBounds(0, 0, 200, 600);
		leftPane.setLayout(null);
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
		personalInfoButton = new JButton();
		personalInfoButton.setBounds(0, 190, Constants.LEFT_PANEL_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		personalInfoButton.setBackground(Constants.DARK_BLUE);
		personalInfoButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		personalInfoButton.setHorizontalTextPosition(JButton.LEFT);
		personalInfoButton.setHorizontalAlignment(JButton.RIGHT);
		personalInfoButton.setText("<html><div align=left width=200px>Personal Information</div></html>");
		personalInfoButton.setIcon(Constants.RIGHT_CHEVRON_ICON);
		personalInfoButton.setForeground(Color.WHITE);
		leftPane.add(personalInfoButton);

		// Purchase necessaries button
		purchaseNecessariesButton = new JButton();
		purchaseNecessariesButton.setBounds(0, 230, Constants.LEFT_PANEL_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		purchaseNecessariesButton.setBackground(Constants.LIGHT_BLUE);
		purchaseNecessariesButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		purchaseNecessariesButton.setHorizontalTextPosition(JButton.LEFT);
		purchaseNecessariesButton.setHorizontalAlignment(JButton.RIGHT);
		purchaseNecessariesButton.setText("<html><div align=left width=200px>Purchase Necessaries</div></html>");
		purchaseNecessariesButton.setForeground(Color.WHITE);
		leftPane.add(purchaseNecessariesButton);

		// Debt payment button
		debtPaymentButton = new JButton();
		debtPaymentButton.setBounds(0, 270, Constants.LEFT_PANEL_WIDTH - 2, Constants.BUTTON_LARGE_HEIGHT);
		debtPaymentButton.setBackground(Constants.LIGHT_BLUE);
		debtPaymentButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		debtPaymentButton.setHorizontalTextPosition(JButton.LEFT);
		debtPaymentButton.setHorizontalAlignment(JButton.RIGHT);
		debtPaymentButton.setText("<html><div align=left width=200px>Debt Payment</div></html>");
		debtPaymentButton.setForeground(Color.WHITE);
		leftPane.add(debtPaymentButton);

		// Change password button
		changePasswordButton = new JButton("Change Password");
		changePasswordButton.setBounds(30, 480, Constants.BUTTON_LARGE_WIDTH + 10, Constants.BUTTON_HEIGHT);
		changePasswordButton.setBackground(Constants.LIGHT_BLUE);
		changePasswordButton.setHorizontalTextPosition(JButton.CENTER);
		changePasswordButton.setForeground(Color.WHITE);
		leftPane.add(changePasswordButton);

		// Logout button
		logoutButton = new JButton("Log Out");
		logoutButton.setBounds(30, 520, Constants.BUTTON_LARGE_WIDTH + 10, Constants.BUTTON_HEIGHT);
		logoutButton.setBackground(Constants.LIGHT_BLUE);
		logoutButton.setHorizontalTextPosition(JButton.CENTER);
		logoutButton.setForeground(Color.WHITE);
		leftPane.add(logoutButton);

		// Quit button
		quitButton = new JButton("Quit");
		quitButton.setBounds(30, 560, Constants.BUTTON_LARGE_WIDTH + 10, Constants.BUTTON_HEIGHT);
		quitButton.setBackground(Constants.RED);
		quitButton.setHorizontalTextPosition(JButton.CENTER);
		quitButton.setForeground(Color.WHITE);
		leftPane.add(quitButton);
	}

	private void initRightPaneComponents() {
		initPersonalInfoTabbed();
		initPurchaseNecessariesTabbed();
		initDebtPaymentPanel();
	}

	private void initPersonalInfoTabbed() {
		// tabbed pane: top (23), left (2), bottom (2), right(2)
		personalInfoTabbed = new PersonalInfoTabbed();
		personalInfoTabbed.setBounds(208, 10, 784, 580);
		add(personalInfoTabbed);
	}

	private void initPurchaseNecessariesTabbed() {
		purchaseNecessariesTabbed = new PurchaseNecessariesTabbed();
		purchaseNecessariesTabbed.setBounds(208, 10, 784, 580);
		purchaseNecessariesTabbed.setVisible(false);
		add(purchaseNecessariesTabbed);
	}

	private void initDebtPaymentPanel() {
		debtPaymentPanel = new DebtPaymentPanel();
		debtPaymentPanel.setBounds(210, 10, 780, 580);
		debtPaymentPanel.setVisible(false);
		add(debtPaymentPanel);
	}

	public void display() {
		mainFrame.setVisible(false);
		mainFrame.setResizable(true);

		mainFrame.setTitle("User");
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

	public JButton getPersonalInfoButton() {
		return personalInfoButton;
	}

	public JButton getPurchaseNecessariesButton() {
		return purchaseNecessariesButton;
	}

	public JButton getDebtPaymentButton() {
		return debtPaymentButton;
	}

	public JButton getChangePasswordButton() {
		return changePasswordButton;
	}

	public JButton getLogoutButton() {
		return logoutButton;
	}

	public JButton getQuitButton() {
		return quitButton;
	}

	public PersonalInfoTabbed getPersonalInfoTabbed() {
		return personalInfoTabbed;
	}

	public PurchaseNecessariesTabbed getPurchaseNecessariesTabbed() {
		return purchaseNecessariesTabbed;
	}

	public DebtPaymentPanel getDebtPaymentPanel() {
		return debtPaymentPanel;
	}
}
