package covid_management;

import covid_management.controllers.login.CreateAdminFirstLoginController;
import covid_management.controllers.login.LoginController;
import covid_management.dao.SystemInfoDAO;
import covid_management.models.SystemInfo;
import covid_management.views.login.CreateAdminFirstLoginDialog;
import covid_management.views.login.LoginView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Optional;

public class CovidManagement {
	private static final Logger logger = LogManager.getLogger(CovidManagement.class);

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception exception) {
			logger.error(exception.getMessage());
			logger.error(exception.getStackTrace());
		}

		SwingUtilities.invokeLater(() -> {
			JFrame mainFrame = new JFrame();
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						logger.info("The CovidManagement app is closing");
						SingletonDBConnection.getInstance().closeConnection();
						logger.info("The CovidManagement app is closed");
					} catch (SQLException sqlException) {
						logger.error(sqlException.getMessage());
						logger.trace(sqlException.getStackTrace());
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

		try {
			SystemInfoDAO systemInfoDAOModel = new SystemInfoDAO();
			Optional<SystemInfo> systemInfoOptional = systemInfoDAOModel.get(1);

			if (systemInfoOptional.isPresent()) {
				if (systemInfoOptional.get().getFirstLoggedIn() == SystemInfo.NOT_INITIALIZE_YET)
					adminFirstLoginDialog.setVisible(true);

				loginView.display();
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
		}
	}
}
