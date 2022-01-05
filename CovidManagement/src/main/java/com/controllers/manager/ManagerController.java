package com.controllers.manager;

import com.utilities.Constants;
import com.utilities.UtilityFunctions;
import com.views.login.LoginView;
import com.views.manager.ManagerView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerController implements ActionListener {
	final private ManagerView managerView;
	final private LoginView loginView;
	final private ManageUserController manageUserController;
	final private StatisticController statisticController;

	public ManagerController(ManagerView managerView, LoginView loginView, String username) {
		this.managerView = managerView;
		this.loginView = loginView;
		this.manageUserController = new ManageUserController(
				loginView.getMainFrame(),
				managerView.getManageUserPanel(),
				username
		);
		this.statisticController = new StatisticController(
				loginView.getMainFrame(),
				managerView.getStatisticPanel()
		);

		this.managerView.getNameLabel().setText(username);

		this.managerView.getManageUserButton().addActionListener(this);
		this.managerView.getManageNecessariesButton().addActionListener(this);
		this.managerView.getStatisticButton().addActionListener(this);
		this.managerView.getLogoutButton().addActionListener(this);
		this.managerView.getQuitButton().addActionListener(this);

		manageUserController.preprocessAndDisplayUI();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == managerView.getManageUserButton()) {
			manageUserNavigateAction();
		} else if (event.getSource() == managerView.getManageNecessariesButton()) {
			manageNecessariesNavigateAction();
		} else if (event.getSource() == managerView.getStatisticButton()) {
			manageStatisticNavigateAction();
		} else if (event.getSource() == managerView.getLogoutButton()) {
			loginView.display();
		} else if (event.getSource() == managerView.getQuitButton()) {
			UtilityFunctions.quitApp(loginView.getMainFrame());
		}
	}

	private void manageUserNavigateAction() {
		managerView.getManageNecessariesPanel().setVisible(false);
		managerView.getStatisticPanel().setVisible(false);

		managerView.getManageNecessariesButton().setBackground(Constants.LIGHT_BLUE);
		managerView.getManageNecessariesButton().setIcon(null);
		managerView.getStatisticButton().setBackground(Constants.LIGHT_BLUE);
		managerView.getStatisticButton().setIcon(null);
		managerView.getManageUserButton().setBackground(Constants.DARK_BLUE);
		managerView.getManageUserButton().setIcon(Constants.RIGHT_CHEVRON_ICON);
		managerView.setSelectingFeature(ManagerView.MANAGE_USER);

		manageUserController.preprocessAndDisplayUI();
	}

	private void manageNecessariesNavigateAction() {
		managerView.getManageUserPanel().setVisible(false);
		managerView.getStatisticPanel().setVisible(false);

		managerView.getManageUserButton().setBackground(Constants.LIGHT_BLUE);
		managerView.getManageUserButton().setIcon(null);
		managerView.getStatisticButton().setBackground(Constants.LIGHT_BLUE);
		managerView.getStatisticButton().setIcon(null);
		managerView.getManageNecessariesButton().setBackground(Constants.DARK_BLUE);
		managerView.getManageNecessariesButton().setIcon(Constants.RIGHT_CHEVRON_ICON);
		managerView.setSelectingFeature(ManagerView.MANAGE_NECESSARIES);

		managerView.getManageNecessariesPanel().setVisible(true);
	}

	private void manageStatisticNavigateAction() {
		managerView.getManageUserPanel().setVisible(false);
		managerView.getManageNecessariesPanel().setVisible(false);

		managerView.getManageUserButton().setBackground(Constants.LIGHT_BLUE);
		managerView.getManageUserButton().setIcon(null);
		managerView.getManageNecessariesButton().setBackground(Constants.LIGHT_BLUE);
		managerView.getManageNecessariesButton().setIcon(null);
		managerView.getStatisticButton().setBackground(Constants.DARK_BLUE);
		managerView.getStatisticButton().setIcon(Constants.RIGHT_CHEVRON_ICON);
		managerView.setSelectingFeature(ManagerView.STATISTIC);

		statisticController.preprocessAndDisplayUI();
	}
}
