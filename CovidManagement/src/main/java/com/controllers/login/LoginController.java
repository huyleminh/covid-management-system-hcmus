package com.controllers.login;

import com.dao.AccountDAO;
import com.models.Account;
import com.utilities.UtilityFunctions;
import com.utilities.ValidationHandler;
import com.views.admin.AdminView;
import com.views.login.CreatePasswordDialog;
import com.views.login.LoginView;
import com.views.manager.ManagerView;
import com.views.user.UserView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class LoginController implements ActionListener {
	private LoginDocumentListener documentListener;
	private LoginView loginView;
	private CreatePasswordDialog createPasswordDialog;
	private AccountDAO accountDAOModel;

	// The value of this attribute always has a non-null value when the Next button is clicked successfully.
	private Account account;

	public LoginController(LoginView loginView) {
		this.documentListener = new LoginDocumentListener(loginView);
		this.loginView = loginView;
		this.createPasswordDialog = new CreatePasswordDialog(loginView.getMainFrame());
		this.accountDAOModel = new AccountDAO();

		this.loginView.getLoginButton().addActionListener(this);
		this.createPasswordDialog.getCancelButton().addActionListener(this);
		this.createPasswordDialog.getCreateButton().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == loginView.getLoginButton()) {
			if (event.getActionCommand().equals("Login"))
				loginActionOfLoginView();
			else
				nextActionOfLoginView();
		} else if (event.getSource() == createPasswordDialog.getCancelButton()) {
			cancelActionOfCreatePasswordDialog();
		} else if (event.getSource() == createPasswordDialog.getCreateButton()) {
			createActionOfCreatePasswordDialog();
		}
	}

	private void nextActionOfLoginView() {
		final String username = loginView.getUsernameField().getText();
		if (!ValidationHandler.validateUsername(username)) {
			showErrorMessage(loginView, "Login", "Invalid username");
			return;
		}

		// Loads account data from the database
		Optional<Account> optionalAccount = accountDAOModel.get(username);

		if (optionalAccount.isEmpty()) {
			showErrorMessage(loginView, "Login", "Incorrect username");
		} else {
			account = optionalAccount.get();

			// Testing
			account.logToScreen();

			if (account.getPassword() == null) {
				// If the user has not password yet, the app will display "create new password" dialog from loginView
				int option = JOptionPane.showConfirmDialog(
						loginView,
						"This is the first time you sign in. You need to create a password",
						"Login",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE
				);

				if (option == JOptionPane.YES_OPTION)
					createPasswordDialog.setVisible(true);
			} else {
				// Otherwise, calls displayAfterValidatingUsername() method from loginView
				loginView.displayAfterValidatingUsername();
			}
		}
	}

	private void loginActionOfLoginView() {
		// Password from input user
		final String password = String.valueOf(loginView.getPasswordField().getPassword());

		// Validates password
		if (!ValidationHandler.validatePassword(password)) {
			JOptionPane.showMessageDialog(
					loginView, "Invalid password", "Login", JOptionPane.ERROR_MESSAGE
			);
			return;
		}

		final String username = loginView.getUsernameField().getText();
		final String passwordEncoded = UtilityFunctions.hashPassword(password);

		// Authenticate
		if (!username.equals(account.getUsername()) || !passwordEncoded.equals(account.getPassword())) {
			showErrorMessage(loginView, "Login", "Incorrect username or password");
		} else {
			// Switches to dashboard with the corresponding role
			byte role = account.getRole();

			if (role == Account.ADMIN) {
				AdminView adminView = new AdminView(loginView.getMainFrame());
				adminView.display();
			} else if (role == Account.MANAGER) {
				ManagerView managerView = new ManagerView(loginView.getMainFrame());
				managerView.display();
			} else {  // User
				UserView userView = new UserView(loginView.getMainFrame());
				userView.display();
			}
		}
	}

	private void cancelActionOfCreatePasswordDialog() {
		int option = JOptionPane.showConfirmDialog(
				createPasswordDialog,
				"Are you sure to close?",
				"Create Password",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION)
			createPasswordDialog.setVisible(false);
	}

	private void createActionOfCreatePasswordDialog() {
		final String password = String.valueOf(createPasswordDialog.getPasswordField().getPassword());
		final String confirmPassword = String.valueOf(createPasswordDialog.getConfirmPasswordField().getPassword());

		// Validate password and confirm password
		if (!ValidationHandler.validatePassword(password) || !ValidationHandler.validatePassword(confirmPassword)) {
			showErrorMessage(createPasswordDialog, "Login", "Invalid password or confirm password");
		} else if (!password.equals(confirmPassword)) {
			showErrorMessage(createPasswordDialog, "Login", "Incorrect confirm password");
		} else {
			int option = JOptionPane.showConfirmDialog(
					createPasswordDialog,
					"Are you sure to create this password?",
					"Create Password",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				final String passwordEncoded = UtilityFunctions.hashPassword(password);
				final String username = loginView.getUsernameField().getText();

				// Save password encoded into the database
				boolean isUpdated = accountDAOModel.updateOneField(
						new Account(username, passwordEncoded, account.getRole(), account.getIsActive(), account.getUserId()),
						"password"
				);

				if (!isUpdated)
					showErrorMessage(createPasswordDialog, "Create Password", "Can not create this password!");
				else
					createPasswordDialog.setVisible(false);
			}
		}
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
