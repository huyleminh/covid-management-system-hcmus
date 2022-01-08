package covid_management.controllers.user;

import covid_management.Client;
import covid_management.dao.DebtDAO;
import covid_management.dao.PaymentHistoryDAO;
import covid_management.dao.UserDAO;
import covid_management.models.Debt;
import covid_management.models.PaymentHistory;
import covid_management.models.User;
import covid_management.views.user.dialogs.InputNumberDialog;
import covid_management.views.user.panels.DebtPaymentPanel;
import org.json.JSONObject;
import shared.components.NonEditableTableModel;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.components.dialogs.ProcessingDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

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

		this.inputMoneyDialog.setVisibleIncreaseAnDecreaseButtons(false);
		this.inputMoneyDialog.setAlwaysOnTop(false);
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
		int totalDebt = debtPaymentPanel.getTotalDebtValue();

		if (money == 0) {
			JOptionPane.showMessageDialog(
					inputMoneyDialog,
					"Please enter a number which is greater than zero",
					"Input Money",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else if (totalDebt < money) {
			JOptionPane.showMessageDialog(
					inputMoneyDialog,
					"You can not pay to exceed your current debt",
					"Input Money",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			try {
				// Create request
				UserDAO daoModel = new UserDAO();
				Optional<User> optionalUser = daoModel.get(userId);

				if (optionalUser.isPresent()) {
					User user = optionalUser.get();
					JSONObject request = new JSONObject();
					JSONObject content = new JSONObject();

					Timestamp current = new Timestamp(System.currentTimeMillis());
					content.put("identifierNumber", user.getIdentifierNumber());
					content.put("debtAmount", money);
					content.put("paidAt", UtilityFunctions.formatTimestamp(Constants.TIMESTAMP_WITHOUT_NANOSECOND, current));
					request.put("secretKey", Constants.DOTENV.get("SOCKET_SECRET_KEY"));
					request.put("operation", Constants.PAY_DEBT);
					request.put("content", content);

					Client client = new Client(
							"Client " + userId,
							Integer.parseInt(Constants.DOTENV.get("SOCKET_PORT")),
							request,
							this
					);
					client.start();

					processingDialog.setExitOnCloseButton(false);
					processingDialog.setVisible(true);
				}
			} catch (DBConnectionException e) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				e.printStackTrace();
			}
		}
	}

	private void loadDebtList() {
		try {
			DebtDAO debtDAOModel = new DebtDAO();
			PaymentHistoryDAO paymentHistoryDAOModel = new PaymentHistoryDAO();
			ArrayList<Debt> debtList = debtDAOModel.getAllByUserId(userId);
			int totalPaidDebt = paymentHistoryDAOModel.getTotalAmountByUserId(userId);

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
					UtilityFunctions.formatMoneyVND(totalDebt - totalPaidDebt) + " VND"
			);

			if (!debtPaymentPanel.isVisible())
				debtPaymentPanel.setVisible(true);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	public void payDebt(JSONObject request, String response) {
		try {
			switch (response) {
				case Constants.SUCCESS_RESPONSE -> {
					PaymentHistoryDAO daoModel = new PaymentHistoryDAO();
					JSONObject content = request.getJSONObject("content");
					PaymentHistory paymentHistory = new PaymentHistory(
							-1,
							userId,
							Timestamp.valueOf(content.getString("paidAt")),
							content.getInt("debtAmount")
					);
					daoModel.create(paymentHistory);

					// update total debt
					int currentTotalDebt = debtPaymentPanel.getTotalDebtValue();
					debtPaymentPanel.getTotalDebtValueLabel().setText(
							UtilityFunctions.formatMoneyVND(currentTotalDebt - paymentHistory.getPaymentAmount()) + " VND"
					);

					processingDialog.setExitOnCloseButton(true);
					processingDialog.setVisible(false);
					JOptionPane.showMessageDialog(
							mainFrame,
							"Paid successfully",
							"Pay Debt",
							JOptionPane.INFORMATION_MESSAGE
					);
				}

				case Constants.FORBIDDEN_RESPONSE, Constants.SERVER_CLOSING_RESPONSE -> {
					processingDialog.setExitOnCloseButton(true);
					processingDialog.setVisible(false);
					showErrorMessage(mainFrame, "Pay Debt", "Can not connect to the server");
				}
				case Constants.DB_CONNECTION_ERROR_RESPONSE -> {
					processingDialog.setExitOnCloseButton(true);
					processingDialog.setVisible(false);
					showErrorMessage(mainFrame, "Pay Debt", "Pay unsuccessfully");
				}
//				case Constants.NOT_ENOUGH_BALANCE_RESPONSE -> {
//					processingDialog.setExitOnCloseButton(true);
//					processingDialog.setVisible(false);
//					showErrorMessage(mainFrame, "Pay Debt", "Your account have not enough balance");
//				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	public void handleConnectionRefused() {
		SwingUtilities.invokeLater(() -> {
			processingDialog.setExitOnCloseButton(true);
			processingDialog.setVisible(false);
			showErrorMessage(debtPaymentPanel, "Debt Payment", "Can not connect to the server");
		});
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
