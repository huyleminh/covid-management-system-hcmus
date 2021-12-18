package com;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.controllers.login.CreateAdminFirstLoginController;
import com.controllers.login.LoginController;
import com.dao.SystemInfoDAO;
import com.models.SystemInfo;
import com.utilities.SingletonDBConnection;
import com.views.login.CreateAdminFirstLoginDialog;
import com.views.login.LoginView;
import com.views.shared.dialogs.ConnectionErrorDialog;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Optional;

public class Main {

//	private static final Logger logger = LogManager.getLogger(com.Main.class);

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception exception) {
			exception.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			JFrame mainFrame = new JFrame();
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						SingletonDBConnection.getInstance().closeConnection();
					} catch (SQLException sqlException) {
						System.out.println(">>> Main.java - line 41");
						sqlException.printStackTrace();
					}
				}
			});

			ConnectionErrorDialog connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
			connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
				connectionErrorDialog.setExitOnCloseButton(false);
				connectionErrorDialog.setVisible(false);

				SingletonDBConnection.getInstance().connect();
				runApp(mainFrame, connectionErrorDialog);
			});

			runApp(mainFrame, connectionErrorDialog);
		});
	}

	private static void runApp(JFrame mainFrame, ConnectionErrorDialog connectionErrorDialog) {
		LoginView loginView = new LoginView(mainFrame);
		LoginController loginController = new LoginController(loginView);

		CreateAdminFirstLoginDialog adminFirstLoginDialog = new CreateAdminFirstLoginDialog(mainFrame);
		CreateAdminFirstLoginController adminFirstLoginController = new CreateAdminFirstLoginController(
				loginView, adminFirstLoginDialog
		);

		SystemInfoDAO systemInfoDAOModel = new SystemInfoDAO();
		Optional<SystemInfo> systemInfoOptional = systemInfoDAOModel.get(1);

		if (systemInfoOptional.isEmpty()) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		} else {
			// Testing
			systemInfoOptional.get().logToScreen();

			if (systemInfoOptional.get().getFirstLoggedIn() == SystemInfo.INITIALIZE_FIRST_TIME)
				adminFirstLoginDialog.setVisible(true);

			loginView.display();
		}
	}
}
