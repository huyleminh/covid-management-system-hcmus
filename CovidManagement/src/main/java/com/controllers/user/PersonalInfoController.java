package com.controllers.user;

import com.dao.*;
import com.models.*;
import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.utilities.Pair;
import com.utilities.SingletonDBConnection;
import com.utilities.UtilityFunctions;
import com.views.shared.dialogs.ConnectionErrorDialog;
import com.views.user.panels.BasicInfoPanel;
import com.views.user.tabbed_panes.PersonalInfoTabbed;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Timestamp;
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

		// Add component listener
		this.personalInfoTabbed.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				personalInfoTabbed.setSelectedIndex(PersonalInfoTabbed.BASIC_INFORMATION_INDEX);
			}
		});

		basicInfoAction();
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		final int index = personalInfoTabbed.getSelectedIndex();
		this.currentTabIndex = index;

		preprocessOf(index);
	}

	private void basicInfoAction() {
		UserDAO daoModel = new UserDAO();
		Optional<User> userOptional = daoModel.get(userId);
		User user = userOptional.get();
		user.logToScreen();  // Testing
		if (user.isEmpty()) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			return;
		}

		// Quarantine location name
		LocationDAO locationDAOModel = new LocationDAO();
		Optional<Object> locationName = locationDAOModel.getOneField(user.getLocationId(), "locationName");

		// Infectious person full name
		Optional<Object> infectiousPersonFullName = Optional.of("Unknown");
		if (user.getInfectiousUserId() != 0) {  // when using jdbc's getInt() method, if null then value is 0.
			infectiousPersonFullName = daoModel.getOneField(user.getInfectiousUserId(), "fullname");
		}

		// connection is unavailable
		if (locationName.isEmpty() || (infectiousPersonFullName.isEmpty() && user.getInfectiousUserId() != 0)) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		} else {
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
					.setText(" " + User.STATUS_NAMES[user.getStatus()]);  // Because a user always has a non-null value of status field.
			basicInfoPanel.getQuarantineTextField()
					.setText(" " + locationName.get());
			basicInfoPanel.getInfectiousPersonTextField()
					.setText(" " + infectiousPersonFullName.get());
		}
	}

	private void managedHistoryAction() {
		// Load all rows belong to this user.
		UserHistoryDAO daoModel = new UserHistoryDAO();
		ArrayList<UserHistory> userHistoryList = (ArrayList<UserHistory>) daoModel.getAllManagedHistoryByUserId(userId);

		// Check connection.
		if (userHistoryList.size() == 1 && userHistoryList.get(0).isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else if (!userHistoryList.isEmpty()) {  // Add those data into the table.
			NonEditableTableModel tableModel = (NonEditableTableModel) personalInfoTabbed.getManagedHistoryPanel()
					.getScrollableTable()
					.getTableModel();

			for (UserHistory userHistory: userHistoryList) {
				String description = userHistory.getDescription();
				// And all descriptions are filtered (remove "Thêm mới người dùng").
				// Because the string always contains " của người dùng <ID Card>".

				tableModel.addRow(new Object[] {
						description.replaceFirst(" của người dùng \\d{9}|\\d{12}", ""),
						UtilityFunctions.formatTimestamp(Constants.TIMESTAMP_WITHOUT_NANOSECOND, userHistory.getDate())
				});
			}
		}
	}

	private void purchasedNecessariesAction() {
		// Load all necessaries belong to this user, which contains necessaries name, price, quantity and created date.
		OrderDetailDAO daoModel = new OrderDetailDAO();
		ArrayList<OrderDetail> orderDetailList = (ArrayList<OrderDetail>) daoModel.getAllByUserId(userId);

		// Check connection
		if (orderDetailList.size() == 1 && orderDetailList.get(0).isEmpty()) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		} else if (!orderDetailList.isEmpty()) {  // Add those data into the table.
			NonEditableTableModel tableModel = (NonEditableTableModel) personalInfoTabbed.getPurchasedNecessariesPanel()
					.getScrollableTable()
					.getTableModel();

			for (OrderDetail orderDetail : orderDetailList) {
				tableModel.addRow(new Object[] {
						orderDetail.getNecessariesName(),
						orderDetail.getQuantity(),
						UtilityFunctions.formatMoneyVND(orderDetail.getPrice()),
						UtilityFunctions.formatTimestamp(Constants.TIMESTAMP_WITHOUT_NANOSECOND, orderDetail.getPurchasedAt())
				});
			}
		}
	}

	private void debtAction() {
		// Load all Debt entities by userId from the database.
		DebtDAO daoModel = new DebtDAO();
		ArrayList<Debt> debtList = (ArrayList<Debt>) daoModel.getAllByUserId(userId);

		// Check connection.
		if (debtList.size() == 1 && debtList.get(0).isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else if (!debtList.isEmpty()) {  // Add those data into the table.
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
	}

	private void paymentHistoryAction() {
		// Load all PaymentHistory entities by userId from the database.
		PaymentHistoryDAO daoModel = new PaymentHistoryDAO();
		ArrayList<PaymentHistory> paymentHistoryList = (ArrayList<PaymentHistory>) daoModel.getAllByUserId(userId);

		// Check connection.
		if (paymentHistoryList.size() == 1 && paymentHistoryList.get(0).isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else if (!paymentHistoryList.isEmpty()) {  // Add those data into the table.
			NonEditableTableModel tableModel = (NonEditableTableModel) personalInfoTabbed.getPaymentHistoryPanel()
					.getScrollableTable()
					.getTableModel();

			for (PaymentHistory paymentHistory : paymentHistoryList) {
				tableModel.addRow(new Object[] {
						UtilityFunctions.formatMoneyVND(paymentHistory.getPaymentAmount()),
						UtilityFunctions.formatTimestamp(Constants.TIMESTAMP_WITHOUT_NANOSECOND, paymentHistory.getDate())
				});
			}
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
