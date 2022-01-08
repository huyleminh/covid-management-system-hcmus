package covid_management.controllers.user;

import covid_management.controllers.ValidationHandler;
import covid_management.dao.AccountDAO;
import covid_management.models.Account;
import covid_management.views.login.LoginView;
import covid_management.views.user.UserView;
import covid_management.views.user.dialogs.ChangePasswordDialog;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class UserController implements ActionListener {
	final private UserView userView;
	final private LoginView loginView;
	final private ChangePasswordDialog changePasswordDialog;
	final private PersonalInfoController personalInfoController;
	final private PurchaseNecessariesController purchaseNecessariesController;
	final private DebtPaymentController debtPaymentController;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private String username;

	public UserController(UserView userView, LoginView loginView, int userId, String username) {
		this.userView = userView;
		this.loginView = loginView;
		this.changePasswordDialog = new ChangePasswordDialog(loginView.getMainFrame());
		this.personalInfoController = new PersonalInfoController(
				this.userView.getMainFrame(),
				this.userView.getPersonalInfoTabbed(),
				userId
		);
		this.purchaseNecessariesController = new PurchaseNecessariesController(
				this.userView.getMainFrame(),
				this.userView.getPurchaseNecessariesTabbed(),
				userId
		);
		this.debtPaymentController = new DebtPaymentController(
				this.userView.getMainFrame(),
				this.userView.getDebtPaymentPanel(),
				userId
		);
		this.connectionErrorDialog = new ConnectionErrorDialog(loginView.getMainFrame());
		this.username = username;

		this.userView.getNameLabel().setText(username);

		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
		this.userView.getPersonalInfoButton().addActionListener(this);
		this.userView.getPurchaseNecessariesButton().addActionListener(this);
		this.userView.getDebtPaymentButton().addActionListener(this);
		this.userView.getChangePasswordButton().addActionListener(this);
		this.userView.getLogoutButton().addActionListener(this);
		this.userView.getQuitButton().addActionListener(this);
		this.changePasswordDialog.getSaveButton().addActionListener(this);
		this.changePasswordDialog.getCancelButton().addActionListener(this);

		personalInfoController.preprocessAndDisplayUI();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectAction();
		} else if (event.getSource() == userView.getPersonalInfoButton()) {
			personalInfoNavigateAction();
		} else if (event.getSource() == userView.getPurchaseNecessariesButton()) {
			purchaseNecessariesNavigateAction();
		} else if (event.getSource() == userView.getDebtPaymentButton()) {
			debtPaymentNavigateAction();
		} else if (event.getSource() == userView.getChangePasswordButton()) {
			changePasswordAction();
		} else if (event.getSource() == userView.getLogoutButton()) {
			loginView.display();
		} else if (event.getSource() == userView.getQuitButton()) {
			UtilityFunctions.quitApp(userView.getMainFrame());
		} else if (event.getSource() == changePasswordDialog.getSaveButton()) {
			saveActionOfChangePasswordDialog();
		} else if (event.getSource() == changePasswordDialog.getCancelButton()) {
			changePasswordDialog.setVisible(false);
		}
	}

	private void reconnectAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();

		if (userView.getPersonalInfoTabbed().isVisible()) {
			personalInfoController.preprocessAndDisplayUI();
		} else if (userView.getPurchaseNecessariesTabbed().isVisible()) {
			purchaseNecessariesController.preprocessAndDisplayUI();
		} else if (userView.getDebtPaymentPanel().isVisible()) {
			debtPaymentController.preprocessAndDisplayUI();
		}
	}

	private void personalInfoNavigateAction() {
		userView.getPurchaseNecessariesTabbed().setVisible(false);
		userView.getDebtPaymentPanel().setVisible(false);

		userView.getPurchaseNecessariesButton().setBackground(Constants.LIGHT_BLUE);
		userView.getPurchaseNecessariesButton().setIcon(null);
		userView.getDebtPaymentButton().setBackground(Constants.LIGHT_BLUE);
		userView.getDebtPaymentButton().setIcon(null);
		userView.getPersonalInfoButton().setBackground(Constants.DARK_BLUE);
		userView.getPersonalInfoButton().setIcon(Constants.RIGHT_CHEVRON_ICON);

		personalInfoController.preprocessAndDisplayUI();
	}

	private void purchaseNecessariesNavigateAction() {
		userView.getPersonalInfoTabbed().setVisible(false);
		userView.getDebtPaymentPanel().setVisible(false);

		userView.getPersonalInfoButton().setBackground(Constants.LIGHT_BLUE);
		userView.getPersonalInfoButton().setIcon(null);
		userView.getDebtPaymentButton().setBackground(Constants.LIGHT_BLUE);
		userView.getDebtPaymentButton().setIcon(null);
		userView.getPurchaseNecessariesButton().setBackground(Constants.DARK_BLUE);
		userView.getPurchaseNecessariesButton().setIcon(Constants.RIGHT_CHEVRON_ICON);

		purchaseNecessariesController.preprocessAndDisplayUI();
	}

	private void debtPaymentNavigateAction() {
		userView.getPersonalInfoTabbed().setVisible(false);
		userView.getPurchaseNecessariesTabbed().setVisible(false);

		userView.getPersonalInfoButton().setBackground(Constants.LIGHT_BLUE);
		userView.getPersonalInfoButton().setIcon(null);
		userView.getPurchaseNecessariesButton().setBackground(Constants.LIGHT_BLUE);
		userView.getPurchaseNecessariesButton().setIcon(null);
		userView.getDebtPaymentButton().setBackground(Constants.DARK_BLUE);
		userView.getDebtPaymentButton().setIcon(Constants.RIGHT_CHEVRON_ICON);

		debtPaymentController.preprocessAndDisplayUI();
	}

	private void changePasswordAction() {
		changePasswordDialog.getCurrentPasswordFieldPanel().getPasswordField().setText("");
		changePasswordDialog.getNewPasswordFieldPanel().getPasswordField().setText("");
		changePasswordDialog.getConfirmNewPasswordFieldPanel().getPasswordField().setText("");

		changePasswordDialog.getCurrentPasswordFieldPanel().setPasswordVisible(false);
		changePasswordDialog.getNewPasswordFieldPanel().setPasswordVisible(false);
		changePasswordDialog.getConfirmNewPasswordFieldPanel().setPasswordVisible(false);

		changePasswordDialog.setVisible(true);
	}

	private void saveActionOfChangePasswordDialog() {
		String oldPassword = changePasswordDialog.getCurrentPasswordFieldPanel().getPasswordValue();
		String newPassword = changePasswordDialog.getNewPasswordFieldPanel().getPasswordValue();
		String confirmNewPassword = changePasswordDialog.getConfirmNewPasswordFieldPanel().getPasswordValue();

		if (!ValidationHandler.validatePassword(oldPassword)) {
			showErrorMessage(changePasswordDialog, "Change Password", "Invalid current password");
		} else if (
				!ValidationHandler.validatePassword(newPassword) ||
				!ValidationHandler.validatePassword(confirmNewPassword)
		) {
			showErrorMessage(
					changePasswordDialog,
					"Change Password",
					"Invalid new password or confirm new password"
			);
		} else if (!newPassword.equals(confirmNewPassword)) {
			showErrorMessage(
					changePasswordDialog,
					"Create Account Manager",
					"Confirm new password does not match with new password"
			);
		} else {
			int option = JOptionPane.showConfirmDialog(
					changePasswordDialog,
					"Are you sure to change password",
					"Change Password",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				try {
					AccountDAO daoModel = new AccountDAO();
					Optional<Account> optionalAccount = daoModel.get(username);

					if (optionalAccount.isPresent()) {
						Account account = optionalAccount.get();
						String oldPasswordEncoded = UtilityFunctions.hashPassword(oldPassword);

						if (!oldPasswordEncoded.equals(account.getPassword())) {
							showErrorMessage(
									changePasswordDialog,
									"Change Password",
									"Incorrect current password"
							);
						} else {
							Account dummyAccount = Account.createEmpty();
							dummyAccount.setUsername(username);
							dummyAccount.setPassword(UtilityFunctions.hashPassword(newPassword));

							daoModel.updateOneFieldByUsername(dummyAccount, "password");
							JOptionPane.showMessageDialog(
									changePasswordDialog,
									"Change password successfully, you must login again.",
									"Change Password",
									JOptionPane.INFORMATION_MESSAGE
							);

							changePasswordDialog.setVisible(false);
							loginView.display();
						}
					}
				} catch (DBConnectionException e) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					e.printStackTrace();
				}
			}
		}
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
