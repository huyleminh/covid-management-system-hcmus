package covid_management.controllers.login;

import covid_management.controllers.ValidationHandler;
import covid_management.dao.AccountDAO;
import covid_management.dao.SystemInfoDAO;
import covid_management.models.Account;
import covid_management.models.SystemInfo;
import covid_management.views.login.CreateAdminFirstLoginDialog;
import covid_management.views.login.LoginView;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CreateAdminFirstLoginController implements ActionListener {
	final private LoginView loginView;
	final private CreateAdminFirstLoginDialog adminFirstLoginDialog;
	final private ConnectionErrorDialog connectionErrorDialog;

	public CreateAdminFirstLoginController(
			LoginView loginView,
			CreateAdminFirstLoginDialog adminFirstLoginDialog
	) {
		this.loginView = loginView;
		this.adminFirstLoginDialog = adminFirstLoginDialog;
		this.connectionErrorDialog = new ConnectionErrorDialog(loginView.getMainFrame());

		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
		this.adminFirstLoginDialog.getCreateButton().addActionListener(this);

		this.adminFirstLoginDialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (!adminFirstLoginDialog.isCreatedAccount())
					UtilityFunctions.quitApp(loginView.getMainFrame());
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectAction();
		} else if (event.getSource() == adminFirstLoginDialog.getCreateButton()) {
			createActionOfAdminFirstLoginDialog();
		}
	}

	private void reconnectAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		createActionOfAdminFirstLoginDialog();
	}

	private void createActionOfAdminFirstLoginDialog() {
		final String password = String.valueOf(adminFirstLoginDialog.getPasswordField().getPassword());
		final String confirmPassword = String.valueOf(adminFirstLoginDialog.getConfirmPasswordField().getPassword());

		// Validate password and confirm password
		if (!ValidationHandler.validatePassword(password) || !ValidationHandler.validatePassword(confirmPassword)) {
			showErrorMessage("Login", "Invalid password or confirm password");
		} else if (!password.equals(confirmPassword)) {
			showErrorMessage("Login", "Incorrect confirm password");
		} else {
			int option = JOptionPane.showConfirmDialog(
					adminFirstLoginDialog,
					"Are you sure to create this account?",
					"Create Admin Account",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				try {
					final String passwordEncoded = UtilityFunctions.hashPassword(password);
					final String username = adminFirstLoginDialog.getUsernameTextField().getText();

					// Save password encoded into the database
					AccountDAO accountDAOModel = new AccountDAO();
					accountDAOModel.updateOneFieldByUsername(
							new Account(username, passwordEncoded, Account.ADMIN, Account.ACTIVE, 0),  // ignore userId
							"password"
					);

					// Save status of first login into the database
					SystemInfoDAO systemInfoDAO = new SystemInfoDAO();
					systemInfoDAO.updateOneField(
							new SystemInfo(1, SystemInfo.INITIALIZED),
							"firstLoggedIn"
					);

					// Switches to part of login screen
					adminFirstLoginDialog.setCreatedAccount(true);
					adminFirstLoginDialog.setVisible(false);
					loginView.displayBeforeValidatingUsername(true);
				} catch (DBConnectionException e) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					e.printStackTrace();
				}
			}
		}
	}

	private void showErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(adminFirstLoginDialog, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
