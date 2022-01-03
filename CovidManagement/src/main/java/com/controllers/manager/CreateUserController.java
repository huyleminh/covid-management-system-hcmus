package com.controllers.manager;

import com.controllers.ValidationHandler;
import com.dao.*;
import com.models.*;
import com.models.table.NonEditableTableModel;
import com.utilities.Pair;
import com.utilities.UtilityFunctions;
import com.views.manager.dialogs.CreateUserDialog;
import com.views.manager.dialogs.FindUserDialog;
import com.views.shared.dialogs.ConnectionErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class CreateUserController implements ActionListener {
	final private CreateUserDialog createUserDialog;
	final private FindUserDialog findUserDialog;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private String managerUsername;

	private Vector<Location> locationList;
	private Vector<String> provinceNameList;
	private HashMap<String, Vector<String>> districtNameHashMap;
	private HashMap<Pair<String, String>, Vector<String>> wardNameHashMap;
	private int selectedUserId;
	private boolean isSuccess;

	public CreateUserController(
			JFrame mainFrame,
			CreateUserDialog createUserDialog,
			ConnectionErrorDialog connectionErrorDialog,
			String managerUsername
	) {
		this.createUserDialog = createUserDialog;
		this.findUserDialog = new FindUserDialog(mainFrame);
		this.connectionErrorDialog = connectionErrorDialog;
		this.managerUsername = managerUsername;

		initNecessaryAttributes();

		this.createUserDialog.getCreateButton().addActionListener(this);
		this.createUserDialog.getCancelButton().addActionListener(this);
		this.createUserDialog.getFindButton().addActionListener(this);
		this.createUserDialog.getRemoveButton().addActionListener(this);
		this.createUserDialog.getStatusOptions().addActionListener(this);
		this.createUserDialog.getProvinceOptions().addActionListener(this);
		this.createUserDialog.getDistrictOptions().addActionListener(this);
		this.findUserDialog.getSearchButton().addActionListener(this);
		this.findUserDialog.getSelectButton().addActionListener(this);
		this.findUserDialog.getCancelButton().addActionListener(this);
	}

	private void initNecessaryAttributes() {
		this.locationList = new Vector<>();
		this.provinceNameList = new Vector<>();
		this.districtNameHashMap = new HashMap<>();
		this.wardNameHashMap = new HashMap<>();
		this.selectedUserId = -1;
		this.isSuccess = false;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == createUserDialog.getCreateButton()) {
			createAction();
		} else if (event.getSource() == createUserDialog.getCancelButton()) {
			cancelActionOfCreateUserDialog();
		} else if (event.getSource() == createUserDialog.getFindButton()) {
			findAction();
		} else if (event.getSource() == createUserDialog.getRemoveButton()) {
			clearInfectiousUserInfo();
		} else if (event.getSource() == createUserDialog.getStatusOptions()) {
			actionOfStatusOptions();
		} else if (event.getSource() == createUserDialog.getProvinceOptions()) {
			actionOfProvinceOptions();
		} else if (event.getSource() == createUserDialog.getDistrictOptions()) {
			actionOfDistrictOptions();
		} else if (event.getSource() == findUserDialog.getSearchButton()) {
			searchActionOfFindUserDialog();
		} else if (event.getSource() == findUserDialog.getSelectButton()) {
			selectActionOfFindUserDialog();
		} else if (event.getSource() == findUserDialog.getCancelButton()) {
			cancelActionOfFindUserDialog();
		}
	}

	public void preprocessAndDisplayUI() {
		createUserDialog.clearDataShowing();

		// Load all location with currentSlots < capacity
		LocationDAO locationDAOModel = new LocationDAO();
		ArrayList<Location> locationList = (ArrayList<Location>) locationDAOModel.getAllAvailable(true);
		this.locationList.clear();
		this.locationList.addAll(locationList);

		// Load all provinces
		ProvinceDAO provinceDAOModel = new ProvinceDAO();
		ArrayList<Province> provinceList = (ArrayList<Province>) provinceDAOModel.getAll();

		if (
				(locationList.size() == 1 && locationList.get(0).isEmpty()) ||
				provinceList.isEmpty()
		) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			return;
		}

		// Add list of province names
		provinceNameList.clear();
		provinceList.forEach(province -> provinceNameList.add(province.getProvinceName()));

		// Mapping list of districts and wards
		districtNameHashMap.clear();
		wardNameHashMap.clear();

		boolean isGot = getAllDistrictsAndWardsWithHashMap(provinceList, districtNameHashMap, wardNameHashMap);
		if (isGot) {
			Vector<String> districtNameList = districtNameHashMap.get(provinceNameList.get(0));
			Vector<String> wardNameList = wardNameHashMap.get(new Pair<>(
					provinceNameList.get(0),
					districtNameList.get(0)
			));
			Vector<String> locationNameList = new Vector<>();
			locationList.forEach(location -> locationNameList.add(location.getLocationName()));

			// Add all province, district, ward and quarantine options
			createUserDialog.getProvinceOptions().setModel(new DefaultComboBoxModel<>(provinceNameList));
			createUserDialog.getDistrictOptions().setModel(new DefaultComboBoxModel<>(districtNameList));
			createUserDialog.getWardOptions().setModel(new DefaultComboBoxModel<>(wardNameList));
			createUserDialog.getQuarantineOptions().setModel(new DefaultComboBoxModel<>(locationNameList));

			this.isSuccess = false;
			createUserDialog.setVisible(true);
		}
	}

	private void createAction() {
		final String fullname = UtilityFunctions.removeRedundantWhiteSpace(createUserDialog.getFullnameTextField().getText());
		final String identifierNumber = UtilityFunctions.removeRedundantWhiteSpace(createUserDialog.getIdCardTextField().getText());
		final String statusAsString = String.valueOf(createUserDialog.getStatusOptions().getSelectedItem());
		final String locationName = String.valueOf(createUserDialog.getQuarantineOptions().getSelectedItem());
		Location location = findLocationByLocationName(locationName);

		if (validateUserInfoBeforeCreating(fullname, identifierNumber, statusAsString, location)) {
			int option = JOptionPane.showConfirmDialog(
					createUserDialog,
					"Are you sure to create this user?",
					"Create User",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION)
				insertNewUser(fullname, identifierNumber, statusAsString, location);
		}
	}

	private void cancelActionOfCreateUserDialog() {
		int option = JOptionPane.showConfirmDialog(
				createUserDialog,
				"Are you sure to close?",
				"Create User",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION)
			createUserDialog.setVisible(false);
	}

	private void findAction() {
		byte newStatusAsByte = User.byteValueOfStatus(String.valueOf(createUserDialog.getStatusOptions().getSelectedItem()));
		newStatusAsByte -= 1;

		UserDAO userDAOModel = new UserDAO();
		ArrayList<User> userList = (ArrayList<User>) userDAOModel.getAllByStatus(newStatusAsByte);

		if (userList.size() == 1 && userList.get(0).isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else {
			findUserDialog.getSearchOptions().setSelectedIndex(0);
			findUserDialog.getSearchValueTextField().setText("");
			addUserListIntoTableOfFindUserDialog(userList);

			findUserDialog.setVisible(true);
		}
	}

	private void actionOfStatusOptions() {
		final String status = String.valueOf(createUserDialog.getStatusOptions().getSelectedItem());

		switch (status) {
			case "Recovery", "F0", "Unknown" -> {
				createUserDialog.getFindButton().setEnabled(false);
				createUserDialog.getRemoveButton().setEnabled(false);
			}
			case "F1", "F2", "F3" ->  {
				createUserDialog.getFindButton().setEnabled(true);
				createUserDialog.getRemoveButton().setEnabled(true);
			}
		}

		clearInfectiousUserInfo();
	}

	private void actionOfProvinceOptions() {
		final String provinceName = String.valueOf(createUserDialog.getProvinceOptions().getSelectedItem());
		Vector<String> districtNameList = districtNameHashMap.get(provinceName);

		createUserDialog.getDistrictOptions().setModel(new DefaultComboBoxModel<>(districtNameList));
		createUserDialog.getWardOptions().setModel(new DefaultComboBoxModel<>(
				wardNameHashMap.get(new Pair<>(provinceName, districtNameList.get(0))))
		);
	}

	private void actionOfDistrictOptions() {
		final String provinceName = String.valueOf(createUserDialog.getProvinceOptions().getSelectedItem());
		final String districtName = String.valueOf(createUserDialog.getDistrictOptions().getSelectedItem());

		createUserDialog.getWardOptions().setModel(new DefaultComboBoxModel<>(
				wardNameHashMap.get(new Pair<>(provinceName, districtName)))
		);
	}

	private void searchActionOfFindUserDialog() {
		String searchOption = String.valueOf(findUserDialog.getSearchOptions().getSelectedItem());
		String searchValue = UtilityFunctions.removeRedundantWhiteSpace(findUserDialog.getSearchValueTextField().getText());
		UserDAO userDAOModel = new UserDAO();
		ArrayList<User> userList;

		byte newStatusAsByte = User.byteValueOfStatus(String.valueOf(createUserDialog.getStatusOptions().getSelectedItem()));
		newStatusAsByte -= 1;
		if (searchValue.isEmpty())
			userList = (ArrayList<User>) userDAOModel.getAllByStatus(newStatusAsByte);
		else {
			String field = searchOption.equals("ID Card") ? "identifierNumber" : "fullname";
			userList = (ArrayList<User>) userDAOModel.searchByAndFilterByStatus(field, searchValue, newStatusAsByte);
		}

		if (userList.size() == 1 && userList.get(0).isEmpty()) {
			findUserDialog.setVisible(false);
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		} else {
			addUserListIntoTableOfFindUserDialog(userList);
		}
	}

	private void selectActionOfFindUserDialog() {
		JTable table = findUserDialog.getScrollableTable().getTable();
		int selectedIndex = table.getSelectedRow();

		if (selectedIndex == -1) {
			JOptionPane.showMessageDialog(
					findUserDialog,
					"Please select a row!",
					"Find user",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			int userId = (int) table.getValueAt(selectedIndex, 0);
			int option = JOptionPane.showConfirmDialog(
					findUserDialog,
					"Are you sure to select this user",
					"Find User",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				this.selectedUserId = userId;
				final String fullname = String.valueOf(table.getValueAt(selectedIndex, 2));
				final String identifierNumber = String.valueOf(table.getValueAt(selectedIndex, 1));
				final String status = String.valueOf(table.getValueAt(selectedIndex, 4));
				createUserDialog.getInfectiousPersonFullnameTextField().setText(" " + fullname);
				createUserDialog.getInfectiousPersonIdCardTextField().setText(" " + identifierNumber);
				createUserDialog.getInfectiousPersonCurrentStatusTextField().setText(" " + status);

				findUserDialog.setVisible(false);
			}
		}
	}

	private void cancelActionOfFindUserDialog() {
		int option = JOptionPane.showConfirmDialog(
				findUserDialog,
				"Are you sure to close?",
				"Find User",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION)
			findUserDialog.setVisible(false);
	}

	private void clearInfectiousUserInfo() {
		selectedUserId = -1;
		createUserDialog.getInfectiousPersonFullnameTextField().setText("");
		createUserDialog.getInfectiousPersonIdCardTextField().setText("");
		createUserDialog.getInfectiousPersonCurrentStatusTextField().setText("");
	}

	private void addUserListIntoTableOfFindUserDialog(ArrayList<User> userList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) findUserDialog.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		for (User user : userList) {
			tableModel.addRow(new Object[]{
					user.getUserId(),
					user.getIdentifierNumber(),
					user.getFullname(),
					user.getYearOfBirth(),
					User.STATUS_NAMES[user.getStatus()]
			});
		}
	}

	private boolean getAllDistrictsAndWardsWithHashMap(
			ArrayList<Province> provinceList,
			HashMap<String, Vector<String>> districtNameHashMap,
			HashMap<Pair<String, String>, Vector<String>> wardNameHashMap
	) {
		// Load all districts
		DistrictDAO districtDAOModel = new DistrictDAO();
		ArrayList<District> districtList = (ArrayList<District>) districtDAOModel.getAll();

		// Load all wards
		WardDAO wardDAOModel = new WardDAO();
		ArrayList<Ward> wardList = (ArrayList<Ward>) wardDAOModel.getAll();

		if (districtList.isEmpty() || wardList.isEmpty()) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			return false;
		}

		for (Province province : provinceList) {
			Vector<String> districtNameList = new Vector<>();

			for (District district : districtList) {
				if (province.getProvinceId() == district.getProvinceId()) {
					districtNameList.add(district.getDistrictName());
					Vector<String> wardNameList = new Vector<>();

					for (Ward ward : wardList)
						if (district.getDistrictId() == ward.getDistrictId())
							wardNameList.add(ward.getWardName());

					wardNameHashMap.put(new Pair<>(province.getProvinceName(), district.getDistrictName()), wardNameList);
				}
			}

			districtNameHashMap.put(province.getProvinceName(), districtNameList);
		}

		return true;
	}

	private boolean validateUserInfoBeforeCreating(
			String fullname, String identifierNumber, String statusAsString, Location location
	) {
		// validate full name
		if (!ValidationHandler.validateFullName(fullname)) {
			showErrorMessage(createUserDialog, "Create User", "Full name is invalid");
			return false;
		}

		// validate id card
		if (!ValidationHandler.validateIdentifierNumber(identifierNumber)) {
			showErrorMessage(createUserDialog, "Create User", "ID card is invalid");
			return false;
		}

		// validate status combined with infectious person
		if (hasInfectiousPeron(statusAsString) && selectedUserId == -1) {
			showErrorMessage(createUserDialog, "Create User", "Please select an infectious person");
			return false;
		}

		// if user is not recovery, the user must select a non-none quarantine location (not "None" option)
		if (!statusAsString.equals("Recovery") && location.getLocationName().equals("None")) {
			showErrorMessage(createUserDialog, "Create User", "Please select another location");
			return false;
		}

		// check existing id card
		UserDAO userDAOModel = new UserDAO();
		byte isExisting = userDAOModel.isExistingIdentifierNumber(identifierNumber);
		if (isExisting == UserDAO.CONNECTION_ERROR) {
			createUserDialog.setVisible(false);
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			return false;
		} else if (isExisting == UserDAO.EXISTING) {
			showErrorMessage(createUserDialog, "Create User", "ID card is existing");
			return false;
		}

		// check capacity of selected quarantine
		LocationDAO locationDAOModel = new LocationDAO();
		byte isEnough = locationDAOModel.hasEnoughOneSlot(location.getLocationId());
		if (isEnough == LocationDAO.CONNECTION_ERROR) {
			createUserDialog.setVisible(false);
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			return false;
		} else if (isEnough == UserDAO.EXISTING) {
			showErrorMessage(
					createUserDialog,
					"Create User",
					"The " + location.getLocationName() + " location is full"
			);
			return false;
		}

		return true;
	}

	private void insertNewUser(
			String fullname, String identifierNumber, String statusAsString, Location location
	) {
		final String address = "%s, %s, %s".formatted(
				createUserDialog.getWardOptions().getSelectedItem(),
				createUserDialog.getDistrictOptions().getSelectedItem(),
				createUserDialog.getProvinceOptions().getSelectedItem()
		);
		User newUser = new User(
				-1,
				identifierNumber,
				fullname,
				(short) createUserDialog.getYearBirthOptions().getSelectedItem(),
				location.getLocationId(),
				User.byteValueOfStatus(statusAsString),
				selectedUserId,
				address
		);
		newUser.logToScreen();
		final String description = UserHistory.generateDescriptionWithoutFormattedString(UserHistory.ADD_NEW_USER)
											  .formatted(identifierNumber);

		// TODO insert: PAYMENT_SYSTEM: paymentAccount
		UserDAO userDAOModel = new UserDAO();
		boolean isCreated = userDAOModel.create(
				newUser,
				managerUsername,
				description,
				(short) (location.getCurrentSlots() + 1)
		);
		if (!isCreated) {
			createUserDialog.setVisible(false);
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		} else {
			JOptionPane.showMessageDialog(
					createUserDialog,
					"Created successfully",
					"Create User",
					JOptionPane.INFORMATION_MESSAGE
			);

			isSuccess = true;
			createUserDialog.setVisible(false);
		}
	}

	private boolean hasInfectiousPeron(String status) {
		return !(status.equals("Recovery") || status.equals("F0") || status.equals("Unknown"));
	}

	private Location findLocationByLocationName(String locationName) {
		for (Location location : locationList)
			if (location.getLocationName().equals(locationName))
				return location;

		return Location.emptyInstance;
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
