package com.controllers.user;

import com.Client;
import com.dao.DebtDAO;
import com.dao.PaymentHistoryDAO;
import com.dao.UserDAO;
import com.models.Debt;
import com.models.PaymentHistory;
import com.models.User;
import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.utilities.SingletonDBConnection;
import com.utilities.UtilityFunctions;
import com.views.shared.dialogs.ConnectionErrorDialog;
import com.views.shared.dialogs.ProcessingDialog;
import com.views.user.dialogs.InputNumberDialog;
import com.views.user.panels.DebtPaymentPanel;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

public class DebtPaymentController implements ActionListener {
	private final JFrame mainFrame;
	private final DebtPaymentPanel debtPaymentPanel;
	private final InputNumberDialog inputMoneyDialog;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final ProcessingDialog processingDialog;
	private final int userId;

	public DebtPaymentController(JFrame mainFrame, DebtPaymentPanel debtPaymentPanel, int userId) {
		this.mainFrame = mainFrame;
		this.debtPaymentPanel = debtPaymentPanel;
		this.inputMoneyDialog = new InputNumberDialog(
				mainFrame, "Input Money", "Money", 0, Constants.MAX_DEBT
		);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.processingDialog = new ProcessingDialog(mainFrame);
		this.userId = userId;

		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
		this.debtPaymentPanel.getPayButton().addActionListener(this);
		this.inputMoneyDialog.getOkButton().addActionListener(this);
		this.inputMoneyDialog.getCancelButton().addActionListener(this);
	}

	public void preprocessAndDisplayUI() {
		loadDebtList();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectAction();
		} else if (event.getSource() == debtPaymentPanel.getPayButton()) {
			inputMoneyDialog.setNumber(0);
			inputMoneyDialog.setVisible(true);
		} else if (event.getSource() == inputMoneyDialog.getOkButton()) {
			okActionOfInputMoneyDialog();
		} else if (event.getSource() == inputMoneyDialog.getCancelButton()) {
			inputMoneyDialog.setVisible(false);
		}
	}

	private void reconnectAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadDebtList();
	}

	private void okActionOfInputMoneyDialog() {
		int money = inputMoneyDialog.getNumber();
		if (money == 0) {
			JOptionPane.showMessageDialog(
					inputMoneyDialog,
					"Please enter a number which is greater than zero",
					"Input Money",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			// Create request
			UserDAO daoModel = new UserDAO();
			Optional<User> optionalUser = daoModel.get(userId);
			if (optionalUser.get().isEmpty()) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			} else {
				Dotenv dotenv = Dotenv.load();
				User user = optionalUser.get();
				JSONObject request = new JSONObject();
				JSONObject content = new JSONObject();

				Timestamp current = new Timestamp(System.currentTimeMillis());
				content.put("identifierNumber", user.getIdentifierNumber());
				content.put("debtAmount", 0);
				content.put("paidAt", UtilityFunctions.formatTimestamp(Constants.TIMESTAMP_WITHOUT_NANOSECOND, current));
				request.put("secretKey", dotenv.get("SOCKET_SECRET_KEY"));
				request.put("operation", Constants.PAY_DEBT);
				request.put("content", content);

				Client client = new Client(
						"Client " + userId,
						Integer.parseInt(dotenv.get("SOCKET_PORT")),
						request,
						this
				);
				client.start();

				processingDialog.setExitOnCloseButton(false);
				processingDialog.setVisible(true);
			}
		}
	}

	private void loadDebtList() {
		DebtDAO daoModel = new DebtDAO();
		ArrayList<Debt> debtList = (ArrayList<Debt>) daoModel.getAllByUserId(userId);

		if (debtList.size() == 1 && debtList.get(0).isEmpty()) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		} else {
			NonEditableTableModel tableModel = (NonEditableTableModel) debtPaymentPanel.getScrollableTable()
																					   .getTableModel();

			tableModel.removeAllRows();

			int totalDebt = 0;
			for (Debt debt : debtList) {
				totalDebt += debt.getTotalDebt();
				tableModel.addRow(
						new Object[]{
								debt.getTotalDebt(),
								UtilityFunctions.formatTimestamp(
										Constants.TIMESTAMP_WITHOUT_NANOSECOND,
										debt.getDebtDate()
								)
						}
				);
			}

			debtPaymentPanel.getTotalDebtValueLabel().setText(
					UtilityFunctions.formatMoneyVND(totalDebt) + " VND"
			);

			if (!debtPaymentPanel.isVisible())
				debtPaymentPanel.setVisible(true);
		}
	}

	public void payDebt(JSONObject request, String response) {
		int operation = request.getInt("operation");
		String dialogTitle = (operation == Constants.CREATE_NEW_PAYMENT_ACCOUNT) ? "Create Account" : "Pay Debt";
		String operationName = (operation == Constants.CREATE_NEW_PAYMENT_ACCOUNT) ? "Create" : "Pay";

		processingDialog.setExitOnCloseButton(true);
		processingDialog.setVisible(false);

		switch (response) {
			case Constants.SUCCESS_RESPONSE -> {
				PaymentHistoryDAO daoModel = new PaymentHistoryDAO();
				JSONObject content = request.getJSONObject("content");
				boolean isCreated = daoModel.create(
						new PaymentHistory(
								-1,
								userId,
								Timestamp.valueOf(content.getString("paidAt")),
								content.getInt("debtAmount")
						)
				);

				JOptionPane.showMessageDialog(
						mainFrame,
						operationName + " successfully",
						dialogTitle,
						JOptionPane.INFORMATION_MESSAGE
				);
			}

			case Constants.FORBIDDEN_RESPONSE, Constants.SERVER_CLOSING_RESPONSE -> {
				showErrorMessage(mainFrame, dialogTitle, "Can not connect to the server");
			}
			case Constants.DB_CONNECTION_ERROR_RESPONSE -> {
				showErrorMessage(mainFrame, dialogTitle, operationName + " failed");
			}
			case Constants.NOT_ENOUGH_BALANCE_RESPONSE -> {
				showErrorMessage(mainFrame, dialogTitle, "Your account have not enough balance");
			}
		}
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
