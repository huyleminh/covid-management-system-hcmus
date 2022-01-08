package payment_system;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import payment_system.controllers.PaymentSystemController;
import shared.utilities.Constants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
	private static final Logger logger = LogManager.getLogger(Server.class);

	private final String secretKey;
	private ServerSocket server;
	private final int portNumber;
	private final PaymentSystemController paymentSystemController;
	public static final Vector<Socket> clientList = new Vector<>();

	public Server(
			int portNumber,
			String secretKey,
			PaymentSystemController paymentSystemController
	) {
		this.secretKey = secretKey;
		this.server = null;
		this.portNumber = portNumber;
		this.paymentSystemController = paymentSystemController;
	}

	public void run() {
		try {
			if (server != null && !server.isClosed())
				server.close();

			server = new ServerSocket(portNumber);
			logger.info("ServerSocket is created " + server);

			while (!server.isClosed()) {
				try {
					Socket client = server.accept();
					clientList.add(client);

					String host = client.getInetAddress().getHostAddress();
					int port = client.getPort();
					String threadName = "Client-%s-%d".formatted(host, port);

					ServerThread threadSendMessage = new ServerThread(
							threadName,
							client,
							secretKey,
							paymentSystemController
					);

					threadSendMessage.start();
				} catch (IOException e) {
					if (!server.isClosed()) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace());
					} else {
						logger.info("Server socket is closed.");
					}
				}
			}

			logger.info("Server socket is closed.");
		} catch (IOException e) {
			logger.info("Can not open socket");
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			System.exit(1);
		}
	}

	public void close() {
		try {
			if (server != null && !server.isClosed()) {
				for (Socket client : clientList) {
					if (!client.isClosed()) {
						PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
						printWriter.println(Constants.SERVER_CLOSING_RESPONSE);

						printWriter.close();
						client.close();
					}
				}

				synchronized (server) {
					logger.info("Server socket is closing");
					server.close();
				}

				clientList.clear();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
		}
	}
}

class ServerThread extends Thread {
	private static final Logger logger = LogManager.getLogger(ServerThread.class);

	private final Socket client;
	private final String secretKey;
	private final PaymentSystemController paymentSystemController;

	ServerThread(
			String name,
			Socket client,
			String secretKey,
			PaymentSystemController paymentSystemController
	) {
		super(name);
		this.client = client;
		this.secretKey = secretKey;
		this.paymentSystemController = paymentSystemController;
	}

	@Override
	public void run() {
		String host = client.getInetAddress().getHostAddress();
		int port = client.getPort();
		logger.info(">>> Client(host: " + host + ", port: " + port + ") is connected");

		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		OutputStream outputStream = null;
		PrintWriter printWriter = null;
		try {
			inputStream = client.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			outputStream = client.getOutputStream();
			printWriter = new PrintWriter(outputStream, true);

			String messageRequest = bufferedReader.readLine();
			if (messageRequest != null) {
				JSONObject parsedRequest = new JSONObject(messageRequest);
				String response = Constants.FORBIDDEN_RESPONSE;

				if (secretKey.equals(parsedRequest.getString("secretKey"))) {
					JSONObject content = parsedRequest.getJSONObject("content");

					switch (parsedRequest.getInt("operation")) {
						case Constants.CREATE_NEW_PAYMENT_ACCOUNT -> response = paymentSystemController.addNewAccount(content);
						case Constants.PAY_DEBT -> response = paymentSystemController.addNewTransaction(content);
					}
				}

				printWriter.println(response);
			}
		} catch (JSONException jsonException) {
			logger.error(jsonException.getMessage());
			logger.error(jsonException.getStackTrace());
			if (printWriter != null)
				printWriter.println(Constants.FORBIDDEN_RESPONSE);
		} catch (IOException ioException) {
			logger.error(ioException.getMessage());
			logger.error(ioException.getStackTrace());
		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
				if (inputStream != null)
					inputStream.close();
				if (printWriter != null)
					printWriter.close();
				if (outputStream != null)
					outputStream.close();

				Server.clientList.remove(client);
				client.close();
				logger.info(">>> Client(host: " + host + ", port: " + port + ") is disconnected");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
