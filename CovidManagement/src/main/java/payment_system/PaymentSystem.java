package payment_system;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import payment_system.controllers.PaymentSystemController;
import payment_system.views.PaymentSystemView;
import shared.db.SingletonDBConnection;
import shared.utilities.Constants;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class PaymentSystem {
	private static final Logger logger = LogManager.getLogger(PaymentSystem.class);

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
		}

		SwingUtilities.invokeLater(() -> {
			JFrame mainFrame = new JFrame("Payment System");
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			PaymentSystemView paymentSystemView = new PaymentSystemView();
			PaymentSystemController paymentSystemController = new PaymentSystemController(
					mainFrame, paymentSystemView
			);

			Server server = new Server(
					Integer.parseInt(Constants.DOTENV.get("SOCKET_PORT")),
					Constants.DOTENV.get("SOCKET_SECRET_KEY"),
					paymentSystemController
			);

			mainFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						SingletonDBConnection.getInstance().closeConnection();
						server.close();
					} catch (SQLException sqlException) {
						logger.error(sqlException.getMessage());
						logger.error(sqlException.getStackTrace());
					}
				}
			});

			paymentSystemController.preprocessAndDisplayUI(server);
		});
	}
}
