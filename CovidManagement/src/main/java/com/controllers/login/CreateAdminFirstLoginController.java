package com.controllers.login;

import com.dao.AccountDAO;
import com.dao.SystemInfoDAO;
import com.models.Account;
import com.models.SystemInfo;
import com.utilities.UtilityFunctions;
import com.utilities.ValidationHandler;
import com.views.login.CreateAdminFirstLoginDialog;
import com.views.login.LoginView;

import javax.swing.*;
import java.awt.event.*;

public class CreateAdminFirstLoginController implements ActionListener {
	private LoginView loginView;
	private CreateAdminFirstLoginDialog adminFirstLoginDialog;
	private AccountDAO accountDAOModel;

	public CreateAdminFirstLoginController(
			LoginView loginView,
			CreateAdminFirstLoginDialog adminFirstLoginDialog
	) {
		this.loginView = loginView;
		this.adminFirstLoginDialog = adminFirstLoginDialog;
		this.accountDAOModel = new AccountDAO();

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
		if (event.getSource() == adminFirstLoginDialog.getCreateButton()) {
			createActionOfAdminFirstLoginDialog();
		}
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
				final String passwordEncoded = UtilityFunctions.hashPassword(password);
				final String username = adminFirstLoginDialog.getUsernameTextField().getText();

				// Save password encoded into the database
				boolean isUpdated = accountDAOModel.updateOneField(
						new Account(username, passwordEncoded, Account.ADMIN, Account.ACTIVE, 1),
						"password"
				);

				// Save status of first login into the database
				if (isUpdated) {
					SystemInfoDAO systemInfoDAO = new SystemInfoDAO();
					isUpdated = systemInfoDAO.updateOneField(
							new SystemInfo(1, SystemInfo.HAS_INITIALIZED_FIRST_TIME, "", 0),
							"firstLoggedIn"
					);
				}
				if (!isUpdated) {
					showErrorMessage("Create Admin Account", "Can not create this account!");
				} else {
					// Switches to part of login screen
					adminFirstLoginDialog.setCreatedAccount(true);
					adminFirstLoginDialog.setVisible(false);
					loginView.displayBeforeValidatingUsername();
				}
			}
		}
	}

	private void showErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(adminFirstLoginDialog, message, title, JOptionPane.ERROR_MESSAGE);
	}
}