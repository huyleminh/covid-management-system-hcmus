package covid_management.controllers.admin;

import covid_management.views.admin.AdminView;
import covid_management.views.login.LoginView;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminController implements ActionListener {
	final private AdminView adminView;
	final private LoginView loginView;
	final private ManagerManagementController managerManagementController;
	final private QuarantineManagementController quarantineManagementController;

	public AdminController(AdminView adminView, LoginView loginView, String username) {
		this.adminView = adminView;
		this.loginView = loginView;
		this.managerManagementController = new ManagerManagementController(
				this.loginView.getMainFrame(),
				this.adminView.getManagerManagementPanel()
		);
		this.quarantineManagementController = new QuarantineManagementController(
				this.loginView.getMainFrame(),
				this.adminView.getQuarantineManagementPanel()
		);

		this.adminView.getNameLabel().setText(username);

		this.adminView.getManageManagerButton().addActionListener(this);
		this.adminView.getManageQuarantineButton().addActionListener(this);
		this.adminView.getLogoutButton().addActionListener(this);
		this.adminView.getQuitButton().addActionListener(this);

		managerManagementController.preprocessAndDisplayUI();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == adminView.getManageManagerButton()) {
			manageManagerNavigateAction();
		} else if (event.getSource() == adminView.getManageQuarantineButton()) {
			manageQuarantineNavigateAction();
		} else if (event.getSource() == adminView.getLogoutButton()) {
			loginView.display();
		} else if (event.getSource() == adminView.getQuitButton()) {
			UtilityFunctions.quitApp(adminView.getMainFrame());
		}
	}

	private void manageManagerNavigateAction() {
		adminView.getQuarantineManagementPanel().setVisible(false);

		adminView.getManageQuarantineButton().setBackground(Constants.LIGHT_BLUE);
		adminView.getManageQuarantineButton().setIcon(null);
		adminView.getManageManagerButton().setBackground(Constants.DARK_BLUE);
		adminView.getManageManagerButton().setIcon(Constants.RIGHT_CHEVRON_ICON);

		managerManagementController.preprocessAndDisplayUI();
	}

	private void manageQuarantineNavigateAction() {
		adminView.getManagerManagementPanel().setVisible(false);

		adminView.getManageManagerButton().setBackground(Constants.LIGHT_BLUE);
		adminView.getManageManagerButton().setIcon(null);
		adminView.getManageQuarantineButton().setBackground(Constants.DARK_BLUE);
		adminView.getManageQuarantineButton().setIcon(Constants.RIGHT_CHEVRON_ICON);

		quarantineManagementController.preprocessAndDisplayUI();
	}
}
