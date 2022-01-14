package payment_system.controllers;

import org.json.JSONObject;
import payment_system.Server;
import payment_system.dao.PaymentAccountDAO;
import payment_system.dao.SystemInfoDAO;
import payment_system.dao.TransactionDAO;
import payment_system.models.PaymentAccount;
import payment_system.models.SystemInfo;
import payment_system.models.Transaction;
import payment_system.views.PaymentSystemView;
import shared.components.NonEditableTableModel;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Vector;

public class PaymentSystemController implements ActionListener {
	private final JFrame mainFrame;
	private final PaymentSystemView paymentSystemView;
	private final ConnectionErrorDialog connectionErrorDialog;
	private Server server;

	public PaymentSystemController(JFrame mainFrame, PaymentSystemView paymentSystemView) {
		this.mainFrame = mainFrame;
		this.paymentSystemView = paymentSystemView;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);

		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	public void preprocessAndDisplayUI(Server server) {
		this.server = server;

		if (loadBankAccountInfo()) {
			if (loadTransactionHistoryList()) {
				Thread serverThread = new Thread("ServerThread") {
					@Override
					public void run() {
						server.run();
					}
				};

				if (!mainFrame.isVisible()) {
					paymentSystemView.displayOn(mainFrame);
				}
				serverThread.start();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		server.close();

		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();

		if (loadBankAccountInfo()) {
			if (loadTransactionHistoryList()) {
				Thread serverThread = new Thread("ServerThread") {
					@Override
					public void run() {
						server.run();
					}
				};

				if (!mainFrame.isVisible()) {
					paymentSystemView.displayOn(mainFrame);
				}
				serverThread.start();
			}
		}
	}

	private boolean loadBankAccountInfo() {
		try {
			SystemInfoDAO daoModel = new SystemInfoDAO();
			Optional<SystemInfo> systemInfoOptional = daoModel.get(1);

			if (systemInfoOptional.isPresent()) {
				SystemInfo systemInfo = systemInfoOptional.get();

				StringBuilder bankIdFormatted = new StringBuilder(systemInfo.getBankAccountNumber());
				bankIdFormatted.insert(4, " ");
				bankIdFormatted.insert(9, " ");

				paymentSystemView.getBankIdValueLabel().setText(bankIdFormatted.toString());
				paymentSystemView.getBalanceValueLabel().setText(
						UtilityFunctions.formatMoneyVND(systemInfo.getBalance())
				);

				return true;
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}

		return false;
	}

	private boolean loadTransactionHistoryList() {
		try {
			TransactionDAO daoModel = new TransactionDAO();
			Vector<Vector<Object>> transactionHistoryList = daoModel.getAllTransactionHistories();
			NonEditableTableModel tableModel = (NonEditableTableModel) paymentSystemView.getTable().getModel();

			tableModel.removeAllRows();
			transactionHistoryList.forEach(tableModel::addRow);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public String addNewAccount(JSONObject data) {
		String response = Constants.DB_CONNECTION_ERROR_RESPONSE;

		try {
			SystemInfoDAO systemInfoDAOModel = new SystemInfoDAO();
			Optional<SystemInfo> optionalSystemInfo = systemInfoDAOModel.get(1);

			if (optionalSystemInfo.isPresent()) {
				PaymentAccountDAO paymentAccountDAOModel = new PaymentAccountDAO();
				paymentAccountDAOModel.create(
						new PaymentAccount(
								-1,
								optionalSystemInfo.get().getDefaultBalanceOfNewAccount(),
								data.getString("fullname"),
								data.getString("identifierNumber")
						)
				);

				response = Constants.SUCCESS_RESPONSE;
			}
		} catch (DBConnectionException e) {
			response = Constants.DB_CONNECTION_ERROR_RESPONSE;
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		}

		return response;
	}

//	public String addNewTransaction(JSONObject data) {
//		String response = Constants.DB_CONNECTION_ERROR_RESPONSE;
//
//		try {
//			PaymentAccountDAO paymentAccountDAOModel = new PaymentAccountDAO();
//			Optional<PaymentAccount> optionalPaymentAccount = paymentAccountDAOModel.getByUserIdentifierNumber(
//					data.getString("identifierNumber")
//			);
//
//			if (optionalPaymentAccount.isPresent()) {
//				PaymentAccount paymentAccount = optionalPaymentAccount.get();
//				TransactionDAO transactionDAOModel = new TransactionDAO();
//				transactionDAOModel.create(
//						new Transaction(
//								-1,
//								paymentAccount.getPaymentId(),
//								Timestamp.valueOf(data.getString("paidAt")),
//								data.getInt("debtAmount")
//						)
//				);
//
//				SwingUtilities.invokeLater(() -> {
//					int currentBalance = paymentSystemView.getBalanceValue();
//					paymentSystemView.setBalanceValue(currentBalance + data.getInt("debtAmount"));
//
//					NonEditableTableModel tableModel = (NonEditableTableModel) paymentSystemView.getTable().getModel();
//					tableModel.insertRow(0, new Object[] {
//							paymentAccount.getUserIdentifierNumber(),
//							paymentAccount.getFullname(),
//							data.getInt("debtAmount"),
//							data.getString("paidAt")
//					});
//				});
//
//				response = Constants.SUCCESS_RESPONSE;
//			}
//		} catch (DBConnectionException e) {
//			response = Constants.DB_CONNECTION_ERROR_RESPONSE;
//			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
//		}
//
//		return response;
//	}

	public String addNewTransaction(JSONObject data) {
		String response = Constants.DB_CONNECTION_ERROR_RESPONSE;

		try {
			PaymentAccountDAO paymentAccountDAOModel = new PaymentAccountDAO();
			Optional<PaymentAccount> optionalPaymentAccount = paymentAccountDAOModel.getByUserIdentifierNumber(
					data.getString("identifierNumber")
			);

			if (optionalPaymentAccount.isPresent()) {
				PaymentAccount paymentAccount = optionalPaymentAccount.get();

				if (paymentAccount.getBalance() < data.getInt("debtAmount")) {
					response = Constants.NOT_ENOUGH_BALANCE_RESPONSE;
				} else {
					TransactionDAO transactionDAOModel = new TransactionDAO();
					transactionDAOModel.create(
							new Transaction(
									-1,
									paymentAccount.getPaymentId(),
									Timestamp.valueOf(data.getString("paidAt")),
									data.getInt("debtAmount")
							)
					);

					SwingUtilities.invokeLater(() -> {
						int currentBalance = paymentSystemView.getBalanceValue();
						paymentSystemView.setBalanceValue(currentBalance + data.getInt("debtAmount"));

						NonEditableTableModel tableModel = (NonEditableTableModel) paymentSystemView.getTable()
																									.getModel();
						tableModel.insertRow(0, new Object[]{
								paymentAccount.getUserIdentifierNumber(),
								paymentAccount.getFullname(),
								data.getInt("debtAmount"),
								data.getString("paidAt")
						});
					});

					response = Constants.SUCCESS_RESPONSE;
				}
			}
		} catch (DBConnectionException e) {
			response = Constants.DB_CONNECTION_ERROR_RESPONSE;
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		}

		return response;
	}
}
