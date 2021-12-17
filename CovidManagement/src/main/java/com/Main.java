package com;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.controllers.login.CreateAdminFirstLoginController;
import com.controllers.login.LoginController;
import com.dao.SystemInfoDAO;
import com.models.SystemInfo;
import com.utilities.UtilityFunctions;
import com.views.login.CreateAdminFirstLoginDialog;
import com.views.login.LoginView;

import javax.swing.*;
import java.util.Optional;

public class Main {

//	private static final Logger logger = LogManager.getLogger(com.Main.class);

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			JFrame mainFrame = new JFrame();
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			LoginView loginView = new LoginView(mainFrame);
			LoginController loginController = new LoginController(loginView);

			CreateAdminFirstLoginDialog adminFirstLoginDialog = new CreateAdminFirstLoginDialog(mainFrame);
			CreateAdminFirstLoginController adminFirstLoginController = new CreateAdminFirstLoginController(
					loginView, adminFirstLoginDialog
			);

			SystemInfoDAO systemInfoDAOModel = new SystemInfoDAO();
			Optional<SystemInfo> systemInfoOptional = systemInfoDAOModel.get(1);

			if (systemInfoOptional.isEmpty()) {
				UtilityFunctions.quitApp(mainFrame);
			} else {
				// Testing
				systemInfoOptional.get().logToScreen();

				if (systemInfoOptional.get().getFirstLoggedIn() == SystemInfo.INITIALIZE_FIRST_TIME)
					adminFirstLoginDialog.setVisible(true);

				loginView.display();
			}
		});
	}
}
