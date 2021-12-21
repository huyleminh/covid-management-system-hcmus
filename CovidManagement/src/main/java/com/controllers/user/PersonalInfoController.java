package com.controllers.user;

import com.dao.*;
import com.models.User;
import com.models.UserHistory;
import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.utilities.SingletonDBConnection;
import com.utilities.UtilityFunctions;
import com.views.shared.dialogs.ConnectionErrorDialog;
import com.views.user.panels.BasicInfoPanel;
import com.views.user.tabbed_panes.PersonalInfoTabbed;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Optional;

public class PersonalInfoController implements ChangeListener {
	private PersonalInfoTabbed personalInfoTabbed;
	private int userId;
	private UserDAO userDAOModel;
	private int currentTabIndex;
	private ConnectionErrorDialog connectionErrorDialog;

	public PersonalInfoController(JFrame mainFrame, PersonalInfoTabbed personalInfoTabbed, int userId) {
		this.personalInfoTabbed = personalInfoTabbed;
		this.userId = userId;
		this.userDAOModel = new UserDAO();
		this.currentTabIndex = PersonalInfoTabbed.BASIC_INFORMATION_INDEX;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);

		this.connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
			connectionErrorDialog.setExitOnCloseButton(false);
			connectionErrorDialog.setVisible(false);

			SingletonDBConnection.getInstance().connect();
			preprocessOf(currentTabIndex);
		});

		basicInfoAction();

		this.personalInfoTabbed.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		final int index = personalInfoTabbed.getSelectedIndex();
		this.currentTabIndex = index;

		preprocessOf(index);
	}

	private void basicInfoAction() {
		Optional<User> userOptional = userDAOModel.get(userId);
		if (userOptional.isEmpty()) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			return;
		}

		// User
		User user = userOptional.get();
		user.logToScreen();  // Testing

		// Province name
		ProvinceDAO provinceDAOModel = new ProvinceDAO();
		Optional<String> provinceName = provinceDAOModel.getProvinceName(user.getProvinceId());

		// District name
		DistrictDAO districtDAOModel = new DistrictDAO();
		Optional<Object> districtName = districtDAOModel.getOneField(user.getDistrictId(), "districtName");

		// Ward name
		WardDAO wardDAOModel = new WardDAO();
		Optional<Object> wardName = wardDAOModel.getOneField(user.getWardId(), "wardName");

		// Quarantine location name
		LocationDAO locationDAOModel = new LocationDAO();
		Optional<Object> locationName = locationDAOModel.getOneField(user.getLocationId(), "locationName");

		// Infectious person full name
		Optional<Object> infectiousPersonFullName = Optional.of("Không xác định");
		if (user.getUserInvolvedId() != 0) {  // when using jdbc's getInt() method, if null then value is 0.
			infectiousPersonFullName = userDAOModel.getOneField(user.getUserInvolvedId(), "fullname");
		}

		if (
				provinceName.isEmpty() ||
						districtName.isEmpty() ||
						wardName.isEmpty() ||
						locationName.isEmpty() ||
						(infectiousPersonFullName.isEmpty() && user.getUserInvolvedId() != 0)
		) { // when the connection of the database is unavailable
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		} else {
			// Create full address.
			final String addressWithoutStreet = "%s, %s, %s".formatted(wardName.get(), districtName.get(), provinceName.get());
			final String fullAddress = ((user.getStreet() == null) ? addressWithoutStreet : (user.getStreet() + ", " + addressWithoutStreet));

			// Display values
			BasicInfoPanel basicInfoPanel = personalInfoTabbed.getBasicInfoPanel();
			basicInfoPanel.getFullnameTextField()
					.setText(" " + user.getFullname());
			basicInfoPanel.getIdCardTextField()
					.setText(" " + user.getIdentifierNumber());
			basicInfoPanel.getYearBirthTextField()
					.setText(" " + user.getYearOfBirth());
			basicInfoPanel.getAddressTextField()
					.setText(" " + fullAddress);
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
		System.out.println(userHistoryList.isEmpty());
		// Check connection.
		if (userHistoryList.size() == 1 && userHistoryList.get(0).isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else if (!userHistoryList.isEmpty()) {  // Add those data into the table.
			NonEditableTableModel tableModel = (NonEditableTableModel) personalInfoTabbed.getManagedHistoryPanel()
					.getScrollableTable()
					.getTableModel();

			for (UserHistory userHistory: userHistoryList) {
				String description = userHistory.getDescription();
				int index = description.indexOf("của người dùng");
				// Because the suffix always is "của người dùng " + id card.
				// And all descriptions are filtered, therefor index is always not equal to -1.

				tableModel.addRow(new Object[] {
						description.substring(0, index - 1),  // 1 is space.
						UtilityFunctions.formatTimestamp(Constants.TIMESTAMP_WITHOUT_NANOSECOND, userHistory.getDate())
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
		}
	}

	public void runFirstTab() {
		basicInfoAction();
	}
}
