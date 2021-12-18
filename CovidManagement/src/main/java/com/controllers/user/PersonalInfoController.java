package com.controllers.user;

import com.dao.*;
import com.models.User;
import com.utilities.SingletonDBConnection;
import com.views.shared.dialogs.ConnectionErrorDialog;
import com.views.user.panels.BasicInfoPanel;
import com.views.user.tabbed_panes.PersonalInfoTabbed;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Optional;

public class PersonalInfoController implements ChangeListener {
	private PersonalInfoTabbed personalInfoTabbed;
	private int userId;
	private UserDAO userDAOModel;
	private String currentTab = PersonalInfoTabbed.BASIC_INFORMATION_TITLE;
	private ConnectionErrorDialog connectionErrorDialog;

	public PersonalInfoController(JFrame mainFrame, PersonalInfoTabbed personalInfoTabbed, int userId) {
		this.personalInfoTabbed = personalInfoTabbed;
		this.userId = userId;
		this.userDAOModel = new UserDAO();
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);

		this.connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
			connectionErrorDialog.setExitOnCloseButton(false);
			connectionErrorDialog.setVisible(false);

			SingletonDBConnection.getInstance().connect();
			preprocessOf(currentTab);
		});

		basicInfoAction();

		this.personalInfoTabbed.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		final int index = personalInfoTabbed.getSelectedIndex();
		final String title = personalInfoTabbed.getTitleAt(index);
		this.currentTab = title;

		preprocessOf(title);
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
			final String address = " %s, %s, %s, %s".formatted(
					user.getStreet(),
					wardName.get(),
					districtName.get(),
					provinceName.get()
			);

			// Display values
			BasicInfoPanel basicInfoPanel = personalInfoTabbed.getBasicInfoPanel();
			basicInfoPanel.getFullnameTextField()
						  .setText(" " + user.getFullname());
			basicInfoPanel.getIdCardTextField()
						  .setText(" " + user.getIdentifierNumber());
			basicInfoPanel.getYearBirthTextField()
						  .setText(" " + user.getYearOfBirth());
			basicInfoPanel.getAddressTextField()
						  .setText(address);
			basicInfoPanel.getCurrentStatusTextField()
						  .setText(" " + User.STATUS_NAMES[user.getStatus()]);  // Because a user always has a non-null value of status field.
			basicInfoPanel.getQuarantineTextField()
						  .setText(" " + locationName.get());
			basicInfoPanel.getInfectiousPersonTextField()
						  .setText(" " + infectiousPersonFullName.get());
		}
	}

	// Preprocessing of a tab of the PersonalInfoTabbed.
	private void preprocessOf(String tabTitle) {
		switch (tabTitle) {
			case PersonalInfoTabbed.BASIC_INFORMATION_TITLE -> {
				personalInfoTabbed.getBasicInfoPanel().clearDataShowing();
				basicInfoAction();
			}
		}
	}
}
