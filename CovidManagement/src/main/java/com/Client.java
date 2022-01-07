package com;

import com.controllers.user.DebtPaymentController;
import com.utilities.Constants;
import org.json.JSONObject;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
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
			System.out.println("Client socket is created" + client);

			inputStream = client.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			outputStream = client.getOutputStream();
			printWriter = new PrintWriter(outputStream, true);

			printWriter.println(request.toString());
			String response = bufferedReader.readLine();

			switch (request.getInt("operation")) {
				case Constants.CREATE_NEW_PAYMENT_ACCOUNT -> {}
				case Constants.PAY_DEBT -> ((DebtPaymentController) controller).payDebt(request, response);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(">>> Client.java - run() - catch block");
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
				e.printStackTrace();
				System.out.println(">>> Client.java - run() - finally block");
			}
		}
	}
}
