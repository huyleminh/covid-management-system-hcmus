package covid_management.controllers.login;

import covid_management.views.login.LoginView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LoginDocumentListener implements DocumentListener {
	private LoginView loginView;

	public LoginDocumentListener(LoginView loginView) {
		this.loginView = loginView;

		this.loginView.getUsernameField().getDocument().addDocumentListener(this);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if (loginView.getLoginButton().getActionCommand().equals("Login"))
			SwingUtilities.invokeLater(() -> loginView.displayBeforeValidatingUsername(false));
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if (loginView.getLoginButton().getActionCommand().equals("Login"))
			SwingUtilities.invokeLater(() -> loginView.displayBeforeValidatingUsername(false));
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// do nothing
	}
}
