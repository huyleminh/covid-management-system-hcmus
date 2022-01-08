package covid_management.controllers.user;

import covid_management.dao.*;
import covid_management.models.*;
import covid_management.views.user.panels.BasicInfoPanel;
import covid_management.views.user.tabbed_panes.PersonalInfoTabbed;
import shared.components.NonEditableTableModel;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Optional;

public class PersonalInfoController implements ChangeListener {
	private PersonalInfoTabbed personalInfoTabbed;
	private int userId;
	private int currentTabIndex;
	private ConnectionErrorDialog connectionErrorDialog;

	public PersonalInfoController(JFrame mainFrame, PersonalInfoTabbed personalInfoTabbed, int userId) {
		this.personalInfoTabbed = personalInfoTabbed;
		this.userId = userId;
		this.currentTabIndex = PersonalInfoTabbed.BASIC_INFORMATION_INDEX;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);

		this.connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
			connectionErrorDialog.setExitOnCloseButton(false);
			connectionErrorDialog.setVisible(false);

			SingletonDBConnection.getInstance().connect();
			preprocessOf(currentTabIndex);
		});

		this.personalInfoTabbed.addChangeListener(this);
	}

	public void preprocessAndDisplayUI() {
		if (personalInfoTabbed.getSelectedIndex() == PersonalInfoTabbed.BASIC_INFORMATION_INDEX)
			preprocessOf(PersonalInfoTabbed.BASIC_INFORMATION_INDEX);
		else
			personalInfoTabbed.setSelectedIndex(PersonalInfoTabbed.BASIC_INFORMATION_INDEX);

		personalInfoTabbed.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		final int index = personalInfoTabbed.getSelectedIndex();
		this.currentTabIndex = index;

		preprocessOf(index);
	}

	private void basicInfoAction() {
		try {
			UserDAO daoModel = new UserDAO();

			// Load User entity
			Optional<User> optionalUser = daoModel.get(userId);
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();

				// Quarantine location name
				LocationDAO locationDAOModel = new LocationDAO();
				Optional<Object> locationName = locationDAOModel.getOneField(
						user.getLocationId(),
						"locationName"
				);

				if (locationName.isEmpty())
					locationName = Optional.of("Không có");

				// Load infectious person's full name
				Optional<Object> infectiousPersonFullName = Optional.of("Không rõ");
				if (user.getInfectiousUserId() != 0)  // when using jdbc's getInt() method, if null then value is 0.
					infectiousPersonFullName = daoModel.getOneField(user.getInfectiousUserId(), "fullname");

				// Display values
				BasicInfoPanel basicInfoPanel = personalInfoTabbed.getBasicInfoPanel();
				basicInfoPanel.getFullnameTextField()
							  .setText(" " + user.getFullname());
				basicInfoPanel.getIdCardTextField()
							  .setText(" " + user.getIdentifierNumber());
				basicInfoPanel.getYearBirthTextField()
							  .setText(" " + user.getYearOfBirth());
				basicInfoPanel.getAddressTextField()
							  .setText(" " + user.getAddress());
				basicInfoPanel.getCurrentStatusTextField()
							  .setText(" " + User.STATUS_NAMES[user.getStatus()]);
				basicInfoPanel.getQuarantineTextField()
							  .setText(" " + locationName.get());
				basicInfoPanel.getInfectiousPersonTextField()
							  .setText(" " + infectiousPersonFullName.get());
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void managedHistoryAction() {
		try {
			// Load all rows belong to this user.
			UserHistoryDAO daoModel = new UserHistoryDAO();
			ArrayList<UserHistory> userHistoryList = daoModel.getAllManagedHistoryByUserId(userId);

			if (!userHistoryList.isEmpty()) {
				NonEditableTableModel tableModel = (NonEditableTableModel) personalInfoTabbed.getManagedHistoryPanel()
																							 .getScrollableTable()
																							 .getTableModel();
				tableModel.removeAllRows();
				for (UserHistory userHistory : userHistoryList) {
					String description = userHistory.getDescription()
													.replaceFirst(
															" của người dùng \\d{9}|\\d{12}",
															""
													);

					if (userHistory.getOperationType() == UserHistory.INDIRECTLY_CHANGE_STATUS) {
						description = description.replaceFirst(
								"lây nhiễm \\d{9}|\\d{12}",
								"lây nhiễm cho bạn"
						);
					}

					tableModel.addRow(
							new Object[]{
									description,
									UtilityFunctions.formatTimestamp(
											Constants.TIMESTAMP_WITHOUT_NANOSECOND,
											userHistory.getDate()
									)
							}
					);
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void purchasedNecessariesAction() {
		try {
			// Load all necessaries belong to this user, which contains necessaries name, price, quantity and created date.
			OrderDetailDAO daoModel = new OrderDetailDAO();
			ArrayList<OrderDetail> orderDetailList = daoModel.getAllByUserId(userId);

			if (!orderDetailList.isEmpty()) {
				NonEditableTableModel tableModel = (NonEditableTableModel) personalInfoTabbed.getPurchasedNecessariesPanel()
																							 .getScrollableTable()
																							 .getTableModel();

				for (OrderDetail orderDetail : orderDetailList) {
					tableModel.addRow(new Object[]{
							orderDetail.getNecessariesName(),
							orderDetail.getQuantity(),
							UtilityFunctions.formatMoneyVND(orderDetail.getPrice()),
							UtilityFunctions.formatTimestamp(
									Constants.TIMESTAMP_WITHOUT_NANOSECOND,
									orderDetail.getPurchasedAt()
							)
					});
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void debtAction() {
		try {
			// Load all Debt entities by userId from the database.
			DebtDAO daoModel = new DebtDAO();
			ArrayList<Debt> debtList = daoModel.getAllByUserId(userId);

			if (!debtList.isEmpty()) {  // Add those data into the table.
				NonEditableTableModel tableModel = (NonEditableTableModel) personalInfoTabbed.getDebtPanel()
																							 .getScrollableTable()
																							 .getTableModel();

				for (Debt debt : debtList) {
					tableModel.addRow(new Object[] {
							UtilityFunctions.formatMoneyVND(debt.getTotalDebt()),
							UtilityFunctions.formatTimestamp(Constants.TIMESTAMP_WITHOUT_NANOSECOND, debt.getDebtDate())
					});
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void paymentHistoryAction() {
		try {
			// Load all PaymentHistory entities by userId from the database.
			PaymentHistoryDAO daoModel = new PaymentHistoryDAO();
			ArrayList<PaymentHistory> paymentHistoryList = daoModel.getAllByUserId(userId);

			if (!paymentHistoryList.isEmpty()) {  // Add those data into the table.
				NonEditableTableModel tableModel = (NonEditableTableModel) personalInfoTabbed.getPaymentHistoryPanel()
																							 .getScrollableTable()
																							 .getTableModel();

				for (PaymentHistory paymentHistory : paymentHistoryList) {
					tableModel.addRow(new Object[]{
							UtilityFunctions.formatMoneyVND(paymentHistory.getPaymentAmount()),
							UtilityFunctions.formatTimestamp(
									Constants.TIMESTAMP_WITHOUT_NANOSECOND,
									paymentHistory.getDate()
							)
					});
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	// Preprocessing of a tab of the PersonalInfoTabbed.
	private void preprocessOf(int tabIndex) {
		switch (tabIndex) {
			case PersonalInfoTabbed.BASIC_INFORMATION_INDEX -> {
				personalInfoTabbed.getBasicInfoPanel().clearDataShowing();
				basicInfoAction();
			}
			case PersonalInfoTabbed.MANAGED_HISTORY_INDEX -> {
				personalInfoTabbed.getManagedHistoryPanel().clearDataShowing();
				managedHistoryAction();
			}
			case PersonalInfoTabbed.PURCHASED_NECESSARIES_INDEX -> {
				personalInfoTabbed.getPurchasedNecessariesPanel().clearDataShowing();
				purchasedNecessariesAction();
			}
			case PersonalInfoTabbed.DEBT_INDEX -> {
				personalInfoTabbed.getDebtPanel().clearDataShowing();
				debtAction();
			}
			case PersonalInfoTabbed.PAYMENT_HISTORY_INDEX -> {
				personalInfoTabbed.getPaymentHistoryPanel().clearDataShowing();
				paymentHistoryAction();
			}
		}
	}
}
