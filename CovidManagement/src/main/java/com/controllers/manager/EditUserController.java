package com.controllers.manager;

import com.dao.LocationDAO;
import com.dao.UserDAO;
import com.models.Location;
import com.models.User;
import com.models.UserHistory;
import com.models.table.NonEditableTableModel;
import com.utilities.Pair;
import com.utilities.UtilityFunctions;
import com.views.manager.dialogs.EditUserDialog;
import com.views.manager.dialogs.FindUserDialog;
import com.views.shared.dialogs.ConnectionErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;

public class EditUserController implements ActionListener {
	final private EditUserDialog editUserDialog;
	final private FindUserDialog findUserDialog;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private String managerUsername;
	final private JTable table;

	private ArrayList<Location> locationList;
	private Location originalLocation;
	private Pair<Integer, String> originInfectiousPerson = new Pair<>(-1, "");
	private int selectedUserId;
	private byte oldStatusAsByte;
	private boolean isSuccess;

	public EditUserController(
			JFrame mainFrame,
			EditUserDialog editUserDialog,
			ConnectionErrorDialog connectionErrorDialog,
			String managerUsername,
			JTable table
	) {
		this.editUserDialog = editUserDialog;
		this.findUserDialog = new FindUserDialog(mainFrame);
		this.connectionErrorDialog = connectionErrorDialog;
		this.managerUsername = managerUsername;
		this.table = table;
		this.isSuccess = false;

		this.editUserDialog.getSaveButton().addActionListener(this);
		this.editUserDialog.getCancelButton().addActionListener(this);
		this.editUserDialog.getStatusOptions().addActionListener(this);
		this.editUserDialog.getFindButton().addActionListener(this);
		this.editUserDialog.getQuarantineOptions().addActionListener(this);
		this.findUserDialog.getSearchButton().addActionListener(this);
		this.findUserDialog.getSelectButton().addActionListener(this);
		this.findUserDialog.getCancelButton().addActionListener(this);

		// Add item listeners
		this.editUserDialog.getStatusEditableButton().addItemListener((event) -> {
			boolean isSelected = this.editUserDialog.getStatusEditableButton().isSelected();
			final String currentStatus = String.valueOf(table.getValueAt(table.getSelectedRow(), 3));

			this.editUserDialog.getStatusOptions().setEnabled(isSelected);
			this.editUserDialog.getStatusOptions().setSelectedItem(currentStatus);

		});
		this.editUserDialog.getQuarantineEditableButton().addItemListener((event) -> {
			boolean isSelected = this.editUserDialog.getQuarantineEditableButton().isSelected();
			final String currentQuarantine = String.valueOf(table.getValueAt(table.getSelectedRow(), 4));

			this.editUserDialog.getQuarantineOptions().setEnabled(isSelected);
			this.editUserDialog.getQuarantineOptions().setSelectedItem(currentQuarantine);
		});
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == editUserDialog.getSaveButton()) {
			saveActionOfEditUserDialog();
		} else if (event.getSource() == editUserDialog.getCancelButton()) {
			cancelActionOfEditUserDialog();
		} else if (event.getSource() == editUserDialog.getStatusOptions()) {
			actionOfStatusOptions();
		} else if (event.getSource() == editUserDialog.getFindButton()) {
			findAction();
		} else if (event.getSource() == editUserDialog.getQuarantineOptions()) {
			actionOfQuarantineOptions();
		} else if (event.getSource() == findUserDialog.getSearchButton()) {
			searchActionOfFindUserDialog();
		} else if (event.getSource() == findUserDialog.getSelectButton()) {
			selectActionOfFindUserDialog();
		} else if (event.getSource() == findUserDialog.getCancelButton()) {
			cancelActionOfFindUserDialog();
		}
	}

	public void preprocessAndDisplayUI(int infectiousUserId) {
		// Load infectious user
		originInfectiousPerson.setLeftValue(infectiousUserId);
		originInfectiousPerson.setRightValue("");
		if (infectiousUserId != -1) {
			UserDAO userDaoModel = new UserDAO();
			Optional<User> infectiousUser = userDaoModel.get(infectiousUserId);  // always has a value

			if (infectiousUser.get().isEmpty()) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				return;
			} else {
				originInfectiousPerson.setRightValue(infectiousUser.get().getFullname());
			}
		}


		// Load list of locations
		LocationDAO locationDAOModel = new LocationDAO();
		locationList = (ArrayList<Location>) locationDAOModel.getAll();

		if (locationList.size() == 1 && locationList.get(0).isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else {
			int selectedRow = table.getSelectedRow();

			// Load status options.
			final String currentStatus = String.valueOf(table.getValueAt(selectedRow, 3));
			int startIndex = 0;
			int endIndex = currentStatus.equals("Recovery") ? (User.STATUS_NAMES.length - 1) : User.byteValueOfStatus(currentStatus);

//			editUserDialog.getStatusOptions().removeAllItems();
//			while (startIndex <= endIndex) {
//				editUserDialog.getStatusOptions().addItem(User.STATUS_NAMES[startIndex]);
//				++startIndex;
//			}


			// Load quarantine options.
//			final String currentQuarantine = String.valueOf(table.getValueAt(selectedRow, 4));
//			editUserDialog.getQuarantineOptions().removeAllItems();
//			for (Location location : locationList) {
//				if (isValidAddedQuarantine(location, currentQuarantine, currentStatus))
//					editUserDialog.getQuarantineOptions().addItem(location.getLocationName());
//			}

			Vector<String> statusNameList = new Vector<>();
			while (startIndex <= endIndex) {
				statusNameList.add(User.STATUS_NAMES[startIndex]);
				++startIndex;
			}

			final String currentQuarantine = String.valueOf(table.getValueAt(selectedRow, 4));
			Vector<String> locationNameList = new Vector<>();
			locationList.forEach(location -> {
				if (isValidAddedQuarantine(location, currentQuarantine, currentStatus))
					locationNameList.add(location.getLocationName());
			});

			int availableSlots = locationList.get(0).getCapacity() - locationList.get(0).getCurrentSlots();

			// Set values for some necessary attributes
			originalLocation = findLocationByLocationName(currentQuarantine);
			isSuccess = false;
			selectedUserId = -1;
			oldStatusAsByte = User.byteValueOfStatus(currentStatus);

			// Set current options.
			editUserDialog.getStatusOptions().setModel(new DefaultComboBoxModel<>(statusNameList));
			editUserDialog.getInfectiousPersonFullnameTextField().setText(" " + originInfectiousPerson.getRightValue());
			editUserDialog.getQuarantineOptions().setModel(new DefaultComboBoxModel<>(locationNameList));
			editUserDialog.getAvailableSlotsTextField().setText(String.valueOf(availableSlots));
			editUserDialog.getQuarantineOptions().setSelectedItem(currentQuarantine);
			if (editUserDialog.getStatusEditableButton().isSelected())
				editUserDialog.getStatusOptions().setSelectedItem(currentStatus);
			else
				editUserDialog.getStatusEditableButton().setSelected(true);

			editUserDialog.setVisible(true);
		}
	}

	private void saveActionOfEditUserDialog() {
		if (editUserDialog.getStatusEditableButton().isSelected()) {
			editStatus();
		} else {
			editQuarantine();
		}
	}

	private void cancelActionOfEditUserDialog() {
		int option = JOptionPane.showConfirmDialog(
				editUserDialog,
				"Are you sure to close?",
				"Edit User",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION) {
			table.clearSelection();
			editUserDialog.setVisible(false);
		}
	}

	private void actionOfStatusOptions() {
		final String selectedStatus = String.valueOf(editUserDialog.getStatusOptions().getSelectedItem());

		if (User.STATUS_NAMES[oldStatusAsByte].equals(selectedStatus)) {
			editUserDialog.getFindButton().setEnabled(false);
			editUserDialog.getInfectiousPersonFullnameTextField().setText(" " + originInfectiousPerson.getRightValue());
		} else {
			boolean isEnabled = isCaseTwo(User.STATUS_NAMES[oldStatusAsByte], selectedStatus);

			editUserDialog.getFindButton().setEnabled(isEnabled);
			editUserDialog.getInfectiousPersonFullnameTextField().setText("");
		}
	}

	private void findAction() {
		byte newStatusAsByte = User.byteValueOfStatus(String.valueOf(editUserDialog.getStatusOptions().getSelectedItem()));
		newStatusAsByte -= 1;

		UserDAO userDAOModel = new UserDAO();
		ArrayList<User> userList = (ArrayList<User>) userDAOModel.getAllByStatus(newStatusAsByte);

		if (userList.size() == 1 && userList.get(0).isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else {
			addUserListIntoTableOfFindUserDialog(userList);
			findUserDialog.setVisible(true);
		}
	}

	private void actionOfQuarantineOptions() {
		final String locationName = String.valueOf(editUserDialog.getQuarantineOptions().getSelectedItem());
		Location selectedLocation = findLocationByLocationName(locationName);
		final int availableSlots = selectedLocation.getCapacity() - selectedLocation.getCurrentSlots();

		editUserDialog.getAvailableSlotsTextField().setText(String.valueOf(availableSlots));
	}

	private void searchActionOfFindUserDialog() {
		String searchOption = String.valueOf(findUserDialog.getSearchOptions().getSelectedItem());
		String searchValue = UtilityFunctions.removeRedundantWhiteSpace(findUserDialog.getSearchValueTextField().getText());
		UserDAO userDAOModel = new UserDAO();
		ArrayList<User> userList;

		byte newStatusAsByte = User.byteValueOfStatus(String.valueOf(editUserDialog.getStatusOptions().getSelectedItem()));
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
		} else if (userList.isEmpty()) {
			JOptionPane.showMessageDialog(
					findUserDialog,
					"No users found!",
					"Find User",
					JOptionPane.INFORMATION_MESSAGE
			);
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
				editUserDialog.getInfectiousPersonFullnameTextField().setText(" " + fullname);

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

	/* Case 1
	 * 1. F0 -> recovery
	 * 		==> F1 -> F0, F2 -> F1, F3 -> F2
	 * 2. F1 -> F0/recovery
	 * 		==> remove infectious person
	 * 		==> F2 -> F1/F0, F3 -> F2/F1
	 * 3. F2 -> F0/recovery
	 * 		==> remove infectious person
	 * 		==> F3 -> F1/F0
	 * 6. F3 -> F0/recovery
	 * 		==> remove infectious person
	 *
	 * Case 2
	 * 3. F2 -> F1
	 * 		==> choose new F0
	 * 		==> F3 -> F2
	 * 5. F3 -> F2/F1
	 * 		==> choose new F1/F0
	 * 7. recovery/unknown -> F3/F2/F1
	 * 		==> choose new F2/F1/F0
	 *
	 * Case 3: do nothing
	 * 9. recovery -> F0
	 * 10. unknown -> F0/recovery
	 */
	private void editStatus() {
		final int selectedIndex = table.getSelectedRow();

		// Get selected option.
		final String currentStatus = String.valueOf(table.getValueAt(selectedIndex, 3));
		byte currentStatusAsByte = User.byteValueOfStatus(currentStatus);
		this.oldStatusAsByte = currentStatusAsByte;

		final String selectedStatus = String.valueOf(editUserDialog.getStatusOptions().getSelectedItem());
		byte selectedStatusAsByte = User.byteValueOfStatus(selectedStatus);

		// Validate that option.
		if (currentStatusAsByte == selectedStatusAsByte) {
			int option = JOptionPane.showConfirmDialog(
					editUserDialog,
					"Information does not change. Are you sure to close?",
					"Edit User",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION)
				editUserDialog.setVisible(false);
		} else {
			if (isCaseTwo(currentStatus, selectedStatus) && this.selectedUserId == -1) {
				showErrorMessage(editUserDialog, "Edit User", "You must select a new infectious person");
				return;
			}

			final int userId = (int) table.getValueAt(selectedIndex, 0);
			final String identifierNumber = String.valueOf(table.getValueAt(selectedIndex, 1));

			ArrayList<ArrayList<User>> involvedUserList = new ArrayList<>();
			ArrayList<Byte> newStatusList = new ArrayList<>();
			ArrayList<Integer> infectiousUserIdList = new ArrayList<>();
			ArrayList<ArrayList<String>> descriptionList = new ArrayList<>();

			boolean isLoaded = loadAllUserInvolvedListForEditing(
					userId, currentStatusAsByte, selectedStatusAsByte, identifierNumber,
					involvedUserList, newStatusList, infectiousUserIdList, descriptionList
			);

			if (!isLoaded) {
				editUserDialog.setVisible(false);
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			} else {
				boolean removeInfectiousUser = !currentStatus.equals("F0");
				Integer newInfectiousUserId = this.selectedUserId;
				String description = UserHistory.generateDescriptionWithoutFormatting(UserHistory.DIRECTLY_CHANGE_STATUS)
												.formatted(currentStatus, selectedStatus, identifierNumber);

				UserDAO userDAOModel = new UserDAO();
				boolean isUpdated = userDAOModel.updateStatus(
						userId, managerUsername, selectedStatusAsByte, removeInfectiousUser, newInfectiousUserId, description,
						involvedUserList, newStatusList, infectiousUserIdList, descriptionList
				);

				if (!isUpdated) {
					editUserDialog.setVisible(false);
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				} else {
					JOptionPane.showMessageDialog(
							editUserDialog,
							"Updated successfully",
							"Edit User",
							JOptionPane.INFORMATION_MESSAGE
					);

					this.isSuccess = true;
					editUserDialog.setVisible(false);
				}
			}
		}
	}

	private boolean isCaseTwo(String currentStatus, String newStatus) {
		if (currentStatus.equals("F2"))
			return newStatus.equals("F1");
		if (currentStatus.equals("F3"))
			return newStatus.equals("F1") || newStatus.equals("F2");
		if (currentStatus.equals("Recovery") || currentStatus.equals("Unknown"))
			return newStatus.equals("F1") || newStatus.equals("F2") || newStatus.equals("F3");
		return false;
	}

	private boolean loadAllUserInvolvedListForEditing(
			int userId, byte currentStatus, byte newStatus, String identifierNumberOfInfectiousUser,
			ArrayList<ArrayList<User>> involvedUserList,
			ArrayList<Byte> newStatusList,
			ArrayList<Integer> infectiousUserIdList,
			ArrayList<ArrayList<String>> descriptionList
	) {
		final String currentStatusAsString = User.STATUS_NAMES[currentStatus];
		if (
				currentStatusAsString.equals("F3") ||
				currentStatusAsString.equals("Recovery") ||
				currentStatusAsString.equals("Unknown")
		) {
			return true;
		}

		UserDAO userDAOModel = new UserDAO();
		ArrayList<User> userList = (ArrayList<User>) userDAOModel.getAllByInfectiousUserId(userId);
		if (userList.size() == 1 && userList.get(0).isEmpty())
			return false;
		if (!userList.isEmpty()) {
			involvedUserList.add(userList);
			newStatusList.add((byte) (newStatus + 1));
			infectiousUserIdList.add(userId);
			descriptionList.add(new ArrayList<>());

			// Create a list of descriptions for list of current involved user
			int lastIndex = descriptionList.size() - 1;
			for (User involvedUser : userList) {
				String description = UserHistory.generateDescriptionWithoutFormatting(UserHistory.INDIRECTLY_CHANGE_STATUS)
												.formatted(
														User.STATUS_NAMES[involvedUser.getStatus()],
														User.STATUS_NAMES[newStatus + 1],
														involvedUser.getIdentifierNumber(),
														identifierNumberOfInfectiousUser,
														User.STATUS_NAMES[currentStatus],
														User.STATUS_NAMES[newStatus]
												);
				descriptionList.get(lastIndex).add(description);
			}

			// Load list of involved users
			for (User involvedUser : userList) {
				boolean isLoaded = loadAllUserInvolvedListForEditing(
						involvedUser.getUserId(),
						involvedUser.getStatus(),
						(byte) (newStatus + 1),
						involvedUser.getIdentifierNumber(),
						involvedUserList,
						newStatusList,
						infectiousUserIdList,
						descriptionList
				);

				if (!isLoaded)
					return false;
			}
		}

		return true;
	}

	private void editQuarantine() {
		// Get selected option.
		final String currentQuarantine = originalLocation.getLocationName();
		final String selectedQuarantine = String.valueOf(editUserDialog.getQuarantineOptions().getSelectedItem());

		// Validate that option.
		if (currentQuarantine.equals(selectedQuarantine)) {
			int option = JOptionPane.showConfirmDialog(
					editUserDialog,
					"Information does not change. Are you sure to close?",
					"Edit User",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION)
				editUserDialog.setVisible(false);
		} else if (currentQuarantine.equals("None")) {
			showErrorMessage(editUserDialog, "Edit User", "The user's status must be 'Recovery'");
		} else {
			final Location selectedLocation = findLocationByLocationName(selectedQuarantine);
			LocationDAO locationDAOModel = new LocationDAO();
			Optional<Location> optionalLocation = locationDAOModel.get(selectedLocation.getLocationId());

			if (optionalLocation.isEmpty())  {
				editUserDialog.setVisible(false);
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			} else if (optionalLocation.get().getCapacity() > optionalLocation.get().getCurrentSlots()) {

				final int selectedRow = table.getSelectedRow();
				final int userId = (int) table.getValueAt(selectedRow, 0);
				final String identifierNumber = String.valueOf(table.getValueAt(selectedRow, 1));
				String description = UserHistory.generateDescriptionWithoutFormatting(UserHistory.CHANGE_QUARANTINE)
												.formatted(currentQuarantine, selectedQuarantine, identifierNumber);

				// Manipulates database
				UserDAO userDAOModel = new UserDAO();
				boolean isUpdated = userDAOModel.updateLocation(
						userId,
						optionalLocation.get().getLocationId(),
						(short) (optionalLocation.get().getCurrentSlots() + 1),
						originalLocation.getLocationId(),
						(short) (originalLocation.getCurrentSlots() - 1),
						managerUsername,
						description
				);

				if (!isUpdated) {  // Database connection is unavailable
					editUserDialog.setVisible(false);
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				} else {  // If update successfully
					JOptionPane.showMessageDialog(
							editUserDialog,
							"Updated successfully",
							"Edit User",
							JOptionPane.INFORMATION_MESSAGE
					);

					this.isSuccess = true;
					editUserDialog.setVisible(false);
				}
			} else {
				showErrorMessage(editUserDialog, "Edit User", "This quarantine location is full");
			}
		}
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

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}

	private Location findLocationByLocationName(String locationName) {
		for (Location location : locationList)
			if (location.getLocationName().equals(locationName))
				return location;

		return Location.emptyInstance;
	}

	// This function will check whether a quarantine location is added into quarantine options or not.
	private boolean isValidAddedQuarantine(Location location, String currentLocationName, String currentStatus) {
		return location.getLocationName().equals(currentLocationName) ||
				location.getCapacity() > location.getCurrentSlots() ||
				(currentStatus.equals("Recovery") && location.getLocationName().equals("None"));
	}
}
