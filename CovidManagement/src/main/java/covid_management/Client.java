package covid_management;

import covid_management.controllers.manager.CreateUserController;
import covid_management.controllers.user.DebtPaymentController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import shared.utilities.Constants;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
	private static final Logger logger = LogManager.getLogger(Client.class);

	private final int portNumber;
	private final JSONObject request;
	private final ActionListener controller;

	public Client(
			String threadName,
			int portNumber,
			JSONObject request,
			ActionListener controller
	) {
		super(threadName);

		this.portNumber = portNumber;
		this.request = request;
		this.controller = controller;
	}

	@Override
	public void run() {
		Socket client = null;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		OutputStream outputStream = null;
		PrintWriter printWriter = null;

		try {
			client = new Socket(InetAddress.getLocalHost(), portNumber);
			logger.info("Client socket is created " + client);

			inputStream = client.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			outputStream = client.getOutputStream();
			printWriter = new PrintWriter(outputStream, true);

			printWriter.println(request.toString());
			String response = bufferedReader.readLine();

			switch (request.getInt("operation")) {
				case Constants.CREATE_NEW_PAYMENT_ACCOUNT -> {
					CreateUserController createUserController = (CreateUserController) controller;
					createUserController.insertNewUser(request, response);
				}
				case Constants.PAY_DEBT -> {
					DebtPaymentController debtPaymentController = (DebtPaymentController) controller;
					debtPaymentController.payDebt(request, response);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());

			switch (request.getInt("operation")) {
				case Constants.CREATE_NEW_PAYMENT_ACCOUNT -> {
					CreateUserController createUserController = (CreateUserController) controller;
					createUserController.handleConnectionRefused();
				}
				case Constants.PAY_DEBT -> {
					DebtPaymentController debtPaymentController = (DebtPaymentController) controller;
					debtPaymentController.handleConnectionRefused();
				}
			}
		} finally {
			try {
				if (client != null)
					client.close();
				if (inputStream != null)
					inputStream.close();
				if (bufferedReader != null)
					bufferedReader.close();
				if (outputStream != null)
					outputStream.close();
				if (printWriter != null)
					printWriter.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}
}
