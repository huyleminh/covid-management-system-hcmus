package com.controllers.login;

import com.controllers.admin.AdminController;
import com.controllers.manager.ManagerController;
import com.controllers.user.UserController;
import com.dao.AccountDAO;
import com.models.Account;
import com.utilities.SingletonDBConnection;
import com.utilities.UtilityFunctions;
import com.controllers.ValidationHandler;
import com.views.admin.AdminView;
import com.views.login.CreatePasswordDialog;
import com.views.login.LoginView;
import com.views.manager.ManagerView;
import com.views.shared.dialogs.ConnectionErrorDialog;
import com.views.user.UserView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Optional;

public class LoginController implements ActionListener {
	// Constants
	private static final byte NEXT_ACTION_OF_LOGIN_VIEW = 0;
	private static final byte LOGIN_ACTION_OF_LOGIN_VIEW = 1;

	private LoginDocumentListener documentListener;
	private LoginView loginView;
	private CreatePasswordDialog createPasswordDialog;
	private AccountDAO accountDAOModel;
	private ConnectionErrorDialog connectionErrorDialog;
	private byte currentTask;

	// The value of this attribute always has a non-null value when the Next button is clicked successfully.
	private Account account;

	public LoginController(LoginView loginView) {
		this.documentListener = new LoginDocumentListener(loginView);
		this.loginView = loginView;
		this.createPasswordDialog = new CreatePasswordDialog(loginView.getMainFrame());
		this.connectionErrorDialog = new ConnectionErrorDialog(loginView.getMainFrame());
		this.currentTask = NEXT_ACTION_OF_LOGIN_VIEW;
		this.accountDAOModel = new AccountDAO();

		// Add action listeners
		this.loginView.getLoginButton().addActionListener(this);
		this.createPasswordDialog.getCancelButton().addActionListener(this);
		this.createPasswordDialog.getCreateButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
			connectionErrorDialog.setExitOnCloseButton(false);
			connectionErrorDialog.setVisible(false);

			SingletonDBConnection.getInstance().connect();
			switch (currentTask) {
				case NEXT_ACTION_OF_LOGIN_VIEW -> createPasswordDialog.setVisible(false);
				case LOGIN_ACTION_OF_LOGIN_VIEW -> loginView.displayBeforeValidatingUsername();
			}
		});

		// Add component listener
		this.createPasswordDialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				createPasswordDialog.getPasswordFieldPanel().getPasswordField().setText("");
				createPasswordDialog.getConfirmPasswordFieldPanel().getPasswordField().setText("");

				createPasswordDialog.getPasswordFieldPanel().setPasswordVisible(false);
				createPasswordDialog.getConfirmPasswordFieldPanel().setPasswordVisible(false);
			}
		});
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
		this.currentTask = NEXT_ACTION_OF_LOGIN_VIEW;

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

			if (account.equals(Account.emptyInstance)) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			} else if (account.getIsActive() == Account.INACTIVE) {
				showErrorMessage(loginView, "Login", "This account has been locked!");
			} else if (account.getRole() == Account.USER && account.getPassword() == null) {
				// If the user has not logged in yet, the app will display "create new password" dialog from loginView
				int option = JOptionPane.showConfirmDialog(
						loginView,
						"This account has not logged in yet. Do you want to create password?",
						"Login",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
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
		this.currentTask = LOGIN_ACTION_OF_LOGIN_VIEW;

		// Password from input user
		final String password = String.valueOf(loginView.getPasswordField().getPassword());

		// Validates password
		if (!ValidationHandler.validatePassword(password)) {
			showErrorMessage(loginView, "Login", "Invalid password");
			return;
		}

		final String username = loginView.getUsernameField().getText();
		final String passwordEncoded = UtilityFunctions.hashPassword(password);
		account = accountDAOModel.get(username).get();

		// Testing
		account.logToScreen();

		if (account.equals(Account.emptyInstance)) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		} else if (account.getIsActive() == Account.INACTIVE) {
			showErrorMessage(loginView, "Login", "This account has been locked!");
		} else if (!username.equals(account.getUsername()) || !passwordEncoded.equals(account.getPassword())) {
			showErrorMessage(loginView, "Login", "Incorrect username or password");
		} else {
			// Switches to dashboard with the corresponding role
			byte role = account.getRole();

			switch (role) {
				case Account.ADMIN -> {
					AdminView adminView = new AdminView(loginView.getMainFrame());
					AdminController adminController = new AdminController(adminView, loginView, username);

					adminView.display();
				}
				case Account.MANAGER -> {
					ManagerView managerView = new ManagerView(loginView.getMainFrame());
					ManagerController managerController = new ManagerController(managerView, loginView, username);

					managerView.display();
				}
				case Account.USER -> {
					UserView userView = new UserView(loginView.getMainFrame());
					UserController userController = new UserController(
							userView,
							loginView,
							account.getUserId(),
							username
					);

					userView.display();
				}
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
		final String password = String.valueOf(createPasswordDialog.getPasswordFieldPanel().getPasswordField().getPassword());
		final String confirmPassword = String.valueOf(createPasswordDialog.getConfirmPasswordFieldPanel().getPasswordField().getPassword());

		// Validate password and confirm password
		if (!ValidationHandler.validatePassword(password) || !ValidationHandler.validatePassword(confirmPassword)) {
			showErrorMessage(createPasswordDialog, "Login", "Invalid password or confirm password");
		} else if (!password.equals(confirmPassword)) {
			showErrorMessage(createPasswordDialog, "Login", "Confirm password does not match with password");
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
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				else
					createPasswordDialog.setVisible(false);
			}
		}
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
