package covid_management.controllers.manager;

import covid_management.Client;
import covid_management.controllers.ValidationHandler;
import covid_management.dao.*;
import covid_management.models.*;
import covid_management.views.manager.dialogs.CreateUserDialog;
import covid_management.views.manager.dialogs.FindUserDialog;
import org.json.JSONObject;
import shared.components.NonEditableTableModel;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.components.dialogs.ProcessingDialog;
import shared.exceptions.DBConnectionException;
import shared.utilities.Constants;
import shared.utilities.Pair;
import shared.utilities.UtilityFunctions;

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
	private final ProcessingDialog processingDialog;
	final private String managerUsername;

	private Vector<Location> locationList;
	private Vector<String> provinceNameList;
	private HashMap<String, Vector<String>> districtNameHashMap;
	private HashMap<Pair<String, String>, Vector<String>> wardNameHashMap;
	private int selectedUserId;
	private Location selectedLocation;
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
		this.processingDialog = new ProcessingDialog(mainFrame);
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
		this.selectedLocation = null;
		this.isSuccess = false;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void preprocessAndDisplayUI() {
		createUserDialog.clearDataShowing();

		try {
			// Load all location with currentSlots < capacity
			LocationDAO locationDAOModel = new LocationDAO();
			ArrayList<Location> locationList = locationDAOModel.getAllAvailable(true);

			this.locationList.clear();
			this.locationList.addAll(locationList);

			// Load all provinces
			ProvinceDAO provinceDAOModel = new ProvinceDAO();
			ArrayList<Province> provinceList = provinceDAOModel.getAll();

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

				selectedLocation = null;
				this.isSuccess = false;
				createUserDialog.setVisible(true);
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == createUserDialog.getCreateButton()) {
			createAction();
		} else if (event.getSource() == createUserDialog.getCancelButton()) {
			createUserDialog.setVisible(false);
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
			findUserDialog.setVisible(false);
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

			if (option == JOptionPane.YES_OPTION) {
				selectedLocation = location;

				// Create request
				JSONObject request = new JSONObject();
				JSONObject content = new JSONObject();

				content.put("identifierNumber",identifierNumber);
				content.put("fullname", fullname);
				request.put("secretKey", Constants.DOTENV.get("SOCKET_SECRET_KEY"));
				request.put("operation", Constants.CREATE_NEW_PAYMENT_ACCOUNT);
				request.put("content", content);

				Client client = new Client(
						"Client " + managerUsername,
						Integer.parseInt(Constants.DOTENV.get("SOCKET_PORT")),
						request,
						this
				);
				client.start();

				processingDialog.setExitOnCloseButton(false);
				processingDialog.setVisible(true);
			}
		}
	}

	private void findAction() {
		try {
			byte newStatusAsByte = User.byteValueOfStatus(
					String.valueOf(createUserDialog.getStatusOptions().getSelectedItem())
			);
			newStatusAsByte -= 1;

			UserDAO daoModel = new UserDAO();
			ArrayList<User> userList = daoModel.getAllByStatus(newStatusAsByte);

			findUserDialog.getSearchOptions().setSelectedIndex(0);
			findUserDialog.getSearchValueTextField().setText("");
			addUserListIntoTableOfFindUserDialog(userList);

			findUserDialog.setVisible(true);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void actionOfStatusOptions() {
		final String status = String.valueOf(createUserDialog.getStatusOptions().getSelectedItem());

		switch (status) {
			case "Bình phục", "F0", "Không rõ" -> {
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
		try {
			String searchOption = String.valueOf(findUserDialog.getSearchOptions().getSelectedItem());
			String searchValue = UtilityFunctions.removeRedundantWhiteSpace(
					findUserDialog.getSearchValueTextField().getText()
			);

			byte newStatusAsByte = User.byteValueOfStatus(
					String.valueOf(createUserDialog.getStatusOptions().getSelectedItem())
			);
			newStatusAsByte -= 1;

			UserDAO daoModel = new UserDAO();
			ArrayList<User> userList;
			if (searchValue.isEmpty()) {
				userList = daoModel.getAllByStatus(newStatusAsByte);
			} else {
				String field = searchOption.equals("ID Card") ? "identifierNumber" : "fullname";
				userList = daoModel.searchByAndFilterByStatus(field, searchValue, newStatusAsByte);
			}

			addUserListIntoTableOfFindUserDialog(userList);
		} catch (DBConnectionException e) {
			findUserDialog.setVisible(false);
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
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
		try {
			// Load all districts
			DistrictDAO districtDAOModel = new DistrictDAO();
			ArrayList<District> districtList = districtDAOModel.getAll();
			// Load all wards
			WardDAO wardDAOModel = new WardDAO();
			ArrayList<Ward> wardList = wardDAOModel.getAll();

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
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
			return false;
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

		// if user is not recovery, the user must select a non-none quarantine location (not "Không có" option)
		if (!statusAsString.equals("Bình phục") && location.getLocationName().equals("Không có")) {
			showErrorMessage(createUserDialog, "Create User", "Please select another location");
			return false;
		}

		try {
			// check existing id card
			UserDAO userDAOModel = new UserDAO();
			boolean isExisting = userDAOModel.isExistingIdentifierNumber(identifierNumber);

			if (isExisting) {
				showErrorMessage(createUserDialog, "Create User", "ID card is existing");
				return false;
			}

			// check capacity of selected quarantine
			if (!location.getLocationName().equals("Không có")) {
				LocationDAO locationDAOModel = new LocationDAO();
				boolean isEnough = locationDAOModel.hasEnoughOneSlot(location.getLocationId());
				if (!isEnough) {
					showErrorMessage(
							createUserDialog,
							"Create User",
							"\""+ location.getLocationName() + "\" location is full"
					);
					return false;
				}
			}
		} catch (DBConnectionException e) {
			createUserDialog.setVisible(false);
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void insertNewUser(JSONObject request, String response) {
		try {
			switch (response) {
				case Constants.SUCCESS_RESPONSE -> {
					JSONObject content = request.getJSONObject("content");
					final String statusAsString = String.valueOf(createUserDialog.getStatusOptions().getSelectedItem());
					final String address = "%s, %s, %s".formatted(
							createUserDialog.getWardOptions().getSelectedItem(),
							createUserDialog.getDistrictOptions().getSelectedItem(),
							createUserDialog.getProvinceOptions().getSelectedItem()
					);
					User newUser = new User(
							-1,
							content.getString("identifierNumber"),
							content.getString("fullname"),
							(short) createUserDialog.getYearBirthOptions().getSelectedItem(),
							selectedLocation.getLocationId(),
							User.byteValueOfStatus(statusAsString),
							selectedUserId,
							address
					);
					final String description = UserHistory.generateDescriptionWithoutFormatting(UserHistory.ADD_NEW_USER)
														  .formatted(content.getString("identifierNumber"));

					UserDAO daoModel = new UserDAO();
					short newCurrentSlots = (short) (selectedLocation.getLocationName().equals("Không có") ? 0 : (selectedLocation.getCurrentSlots() + 1));
					daoModel.create(
							newUser,
							managerUsername,
							description,
							newCurrentSlots
					);

					processingDialog.setExitOnCloseButton(true);
					processingDialog.setVisible(false);
					JOptionPane.showMessageDialog(
							createUserDialog,
							"Created successfully",
							"Create User",
							JOptionPane.INFORMATION_MESSAGE
					);

					isSuccess = true;
					createUserDialog.setVisible(false);
				}

				case Constants.FORBIDDEN_RESPONSE, Constants.SERVER_CLOSING_RESPONSE -> {
					processingDialog.setExitOnCloseButton(true);
					processingDialog.setVisible(false);
					showErrorMessage(createUserDialog, "Create Account", "Can not connect to the server");
				}
				case Constants.DB_CONNECTION_ERROR_RESPONSE -> {
					processingDialog.setExitOnCloseButton(true);
					processingDialog.setVisible(false);
					showErrorMessage(createUserDialog, "Create Account", "Created unsuccessfully");
				}
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
			showErrorMessage(createUserDialog, "Create Account", "Can not connect to the server");
		});
	}

	private boolean hasInfectiousPeron(String status) {
		return !(status.equals("Bình phục") || status.equals("F0") || status.equals("Không rõ"));
	}

	private Location findLocationByLocationName(String locationName) {
		for (Location location : locationList)
			if (location.getLocationName().equals(locationName))
				return location;

		return null;
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
