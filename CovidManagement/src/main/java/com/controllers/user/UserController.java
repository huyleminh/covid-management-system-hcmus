package com.controllers.user;

import com.utilities.Constants;
import com.utilities.UtilityFunctions;
import com.views.login.LoginView;
import com.views.user.UserView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserController implements ActionListener {

	private UserView userView;
	private LoginView loginView;
	private int userId;
	private PersonalInfoController personalInfoController;

	public UserController(UserView userView, LoginView loginView, int userId, String username) {
		this.userView = userView;
		this.loginView = loginView;
		this.userId = userId;
		this.personalInfoController = new PersonalInfoController(
				this.userView.getMainFrame(),
				this.userView.getPersonalInfoTabbed(),
				userId
		);

		this.userView.getNameLabel().setText(username);

		this.userView.getPersonalInfoButton().addActionListener(this);
		this.userView.getPurchaseNecessariesButton().addActionListener(this);
		this.userView.getDebtPaymentButton().addActionListener(this);
		this.userView.getLogoutButton().addActionListener(this);
		this.userView.getQuitButton().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == userView.getPersonalInfoButton()) {
			personalInfoNavigateAction();
		} else if (event.getSource() == userView.getPurchaseNecessariesButton()) {
			purchaseNecessariesNavigateAction();
		} else if (event.getSource() == userView.getDebtPaymentButton()) {
			debtPaymentNavigateAction();
		} else if (event.getSource() == userView.getLogoutButton()) {
			loginView.display();
		} else if (event.getSource() == userView.getQuitButton()) {
			UtilityFunctions.quitApp(userView.getMainFrame());
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
		userView.setSelectingFeature(UserView.PERSONAL_INFO);

		userView.getPersonalInfoTabbed().setVisible(true);
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
		userView.setSelectingFeature(UserView.PURCHASE_NECESSARIES);

		userView.getPurchaseNecessariesTabbed().setVisible(true);
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
		userView.setSelectingFeature(UserView.DEBT_PAYMENT);

		userView.getDebtPaymentPanel().setVisible(true);
	}
}
