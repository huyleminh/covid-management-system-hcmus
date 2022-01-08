package covid_management.controllers.manager;

import covid_management.views.login.LoginView;
import covid_management.views.manager.ManagerView;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerController implements ActionListener {
	final private ManagerView managerView;
	final private LoginView loginView;
	final private ManageUserController manageUserController;
	final private ManageNecessariesController manageNecessariesController;
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
		this.manageNecessariesController = new ManageNecessariesController(
				loginView.getMainFrame(),
				managerView.getManageNecessariesPanel(),
				username
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

		manageNecessariesController.preprocessAndDisplayUI();
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

		statisticController.preprocessAndDisplayUI();
	}
}
