package com.controllers.manager;

import com.dao.*;
import com.models.User;
import com.models.table.NonEditableTableModel;
import com.utilities.Pair;
import com.utilities.SingletonDBConnection;
import com.utilities.UtilityFunctions;
import com.views.manager.dialogs.CreateUserDialog;
import com.views.manager.dialogs.EditUserDialog;
import com.views.manager.dialogs.ViewUserDetailDialog;
import com.views.manager.panels.ManageUserPanel;
import com.views.shared.dialogs.ConnectionErrorDialog;
import com.views.shared.dialogs.SortDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Optional;

public class ManageUserController implements ActionListener {
	final private JFrame mainFrame;
	final private ManageUserPanel manageUserPanel;
	final private EditUserDialog editUserDialog;
	final private CreateUserDialog createUserDialog;
	final private SortDialog sortDialog;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private String managerUsername;
	final private EditUserController editUserController;
	final private CreateUserController createUserController;

	public ManageUserController(JFrame mainFrame, ManageUserPanel manageUserPanel, String username) {
		this.mainFrame = mainFrame;
		this.manageUserPanel = manageUserPanel;
		this.editUserDialog = new EditUserDialog(mainFrame);
		this.createUserDialog = new CreateUserDialog(mainFrame);
		this.sortDialog = new SortDialog(
				mainFrame,
				"Sort User",
				new String[] { "ID Card", "Full Name", "Status" }
		);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.managerUsername = username;
		this.editUserController = new EditUserController(
				mainFrame,
				editUserDialog,
				connectionErrorDialog,
				managerUsername,
				manageUserPanel.getScrollableTable().getTable()
		);
		this.createUserController = new CreateUserController(
				mainFrame,
				createUserDialog,
				connectionErrorDialog,
				managerUsername
		);

		this.connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
			connectionErrorDialog.setExitOnCloseButton(false);
			connectionErrorDialog.setVisible(false);

			SingletonDBConnection.getInstance().connect();
			loadUserList();
		});

		this.manageUserPanel.getSearchButton().addActionListener(this);
		this.manageUserPanel.getEditButton().addActionListener(this);
		this.manageUserPanel.getViewDetailButton().addActionListener(this);
		this.manageUserPanel.getSortButton().addActionListener(this);
		this.manageUserPanel.getCreateButton().addActionListener(this);
		this.sortDialog.getSortButton().addActionListener(this);
		this.sortDialog.getCancelButton().addActionListener(this);

		// Add component listener
		editUserDialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (editUserController.isSuccess())
					loadUserList();
			}
		});
		createUserDialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (createUserController.isSuccess())
					loadUserList();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == manageUserPanel.getSearchButton()) {
			searchAction();
		} else if (event.getSource() == manageUserPanel.getEditButton()) {
			editAction();
		} else if (event.getSource() == manageUserPanel.getViewDetailButton()) {
			viewDetailAction();
		} else if (event.getSource() == manageUserPanel.getSortButton()) {
			sortAction();
		} else if (event.getSource() == manageUserPanel.getCreateButton()) {
			createUserController.preprocessAndDisplayUI();
		} else if (event.getSource() == sortDialog.getSortButton()) {
			sortActionOfSortDialog();
		} else if (event.getSource() == sortDialog.getCancelButton()) {
			cancelActionOfSortDialog();
		}
	}

	public void preprocessAndDisplayUI() {
		loadUserList();
		manageUserPanel.setVisible(true);
	}

	private void loadUserList() {
		NonEditableTableModel tableModel = (NonEditableTableModel) manageUserPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		// Load all users from the database.
		UserDAO daoModel = new UserDAO();
		ArrayList<Pair<User, String>> userList = (ArrayList<Pair<User, String>>) daoModel.getAllWithLocationName();

		// Check connection.
		if (userList.size() == 1 && userList.get(0).getLeftValue().isEmpty() && userList.get(0).getRightValue().isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else {  // Add those data into the table.
			for (Pair<User, String> item : userList) {
				int infectiousUserId = item.getLeftValue().getInfectiousUserId();
				infectiousUserId = (infectiousUserId == 0) ? -1 : infectiousUserId;
				// Because of SQL INTEGER TYPE: NULL in SQL => 0 in Java
				// infectiousUserId only contains NULL or a number which is greater than 0.

				tableModel.addRow(new Object[] {
						item.getLeftValue().getUserId(),
						item.getLeftValue().getIdentifierNumber(),
						item.getLeftValue().getFullname(),
						User.STATUS_NAMES[item.getLeftValue().getStatus()],
						item.getRightValue(),
						infectiousUserId
				});
			}
		}
	}

	private void searchAction() {
		// Get search value.
		final String searchOption = String.valueOf(manageUserPanel.getSearchOptions().getSelectedItem());
		final String searchValue = UtilityFunctions.removeRedundantWhiteSpace(
				manageUserPanel.getSearchValueTextField().getText()
		);

		if (searchValue.isEmpty())
			loadUserList();
		else {
			UserDAO daoModel = new UserDAO();
			final String field = searchOption.equals("ID Card") ? "identifierNumber" : "fullname";
			ArrayList<Pair<User, String>> userList = (ArrayList<Pair<User, String>>) daoModel.searchByAndResultIncludedLocationName(
					field, searchValue
			);

			// Check connection.
			if (userList.size() == 1 && userList.get(0).getLeftValue().isEmpty() && userList.get(0).getRightValue().isEmpty())
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			else {  // Add those data into the table.
				NonEditableTableModel tableModel = (NonEditableTableModel) manageUserPanel.getScrollableTable().getTableModel();
				tableModel.removeAllRows();

				for (Pair<User, String> item : userList) {
					tableModel.addRow(new Object[] {
							item.getLeftValue().getUserId(),
							item.getLeftValue().getIdentifierNumber(),
							item.getLeftValue().getFullname(),
							User.STATUS_NAMES[item.getLeftValue().getStatus()],
							item.getRightValue()
					});
				}
			}
		}
	}

	private void editAction() {
		// Get selected row.
		JTable table = manageUserPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		// If no selection -> show message dialog.
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
					mainFrame,
					"Please select a row!",
					"Edit User",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			int infectiousUserId = (int) table.getValueAt(selectedRow, 5);
			editUserController.preprocessAndDisplayUI(infectiousUserId);
		}
	}

	private void viewDetailAction() {
		// Get selected row.
		JTable table = manageUserPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		// If no selected row -> show message dialog.
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
					mainFrame,
					"Please select a row!",
					"View Detail",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {  // Otherwise, show view activity dialog.
			int userId = (int) table.getValueAt(selectedRow, 0);
			UserDAO daoModel = new UserDAO();

			// Load User entity
			User user = daoModel.get(userId).get();
			user.logToScreen();  // testing
			if (user.isEmpty()) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				return;
			}

			// Quarantine location name
			LocationDAO locationDAOModel = new LocationDAO();
			Optional<Object> locationName = locationDAOModel.getOneField(user.getLocationId(), "locationName");
			if (locationName.isEmpty()) {  // connection is unavailable
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				return;
			}

			// Load infectious person's full name
			Optional<Object> infectiousPersonFullName = Optional.of("Unknown");
			if (user.getInfectiousUserId() != 0)  // when using jdbc's getInt() method, if null then value is 0.
				infectiousPersonFullName = daoModel.getOneField(user.getInfectiousUserId(), "fullname");
			if (infectiousPersonFullName.isEmpty() && user.getInfectiousUserId() != 0) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				return;
			}

			ArrayList<User> involvedUserList = loadAllInvolvedUserList(daoModel, user.getUserId());
			if (involvedUserList.size() == 1 && involvedUserList.get(0).isEmpty()) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			} else {
				// Display values
				ViewUserDetailDialog viewUserDetailDialog = new ViewUserDetailDialog(mainFrame);
				NonEditableTableModel userInvolvedListTableModel = (NonEditableTableModel) viewUserDetailDialog.getScrollableTable()
																											   .getTableModel();

				viewUserDetailDialog.getFullnameTextField()
							  .setText(" " + user.getFullname());
				viewUserDetailDialog.getIdCardTextField()
							  .setText(" " + user.getIdentifierNumber());
				viewUserDetailDialog.getYearBirthTextField()
							  .setText(" " + user.getYearOfBirth());
				viewUserDetailDialog.getAddressTextField()
							  .setText(" " + user.getAddress());
				viewUserDetailDialog.getCurrentStatusTextField()
							  .setText(" " + User.STATUS_NAMES[user.getStatus()]);
				viewUserDetailDialog.getQuarantineTextField()
							  .setText(" " + locationName.get());
				viewUserDetailDialog.getInfectiousPersonTextField()
							  .setText(" " + infectiousPersonFullName.get());

				for (User involvedUser : involvedUserList) {
					userInvolvedListTableModel.addRow(new Object[] {
							involvedUser.getIdentifierNumber(),
							involvedUser.getFullname(),
							involvedUser.getYearOfBirth(),
							User.STATUS_NAMES[involvedUser.getStatus()]
					});
				}

				viewUserDetailDialog.setVisible(true);
			}
		}
	}

	private void sortAction() {
		sortDialog.setOrderType(SortDialog.ASCENDING_ORDER);
		sortDialog.getSortOptions().setSelectedIndex(0);
		sortDialog.setVisible(true);
	}

	private void sortActionOfSortDialog() {
		// Get selected options.
		boolean isAscending = sortDialog.isAscendingOrder();
		String sortOption = String.valueOf(sortDialog.getSortOptions().getSelectedItem());

		// Convert sort option to field name (table's column name in the database)
		String fieldName = "";
		switch (sortOption) {
			case "ID Card" -> fieldName = "identifierNumber";
			case "Full Name" -> fieldName = "fullname";
			case "Status" -> fieldName = "status";
		}

		// Get sorted list of users
		UserDAO userDAOModel = new UserDAO();
		ArrayList<Pair<User, String>> userList = (ArrayList<Pair<User, String>>) userDAOModel.sortByOneFieldWithLocationName(
				fieldName, isAscending
		);

		// Check connection
		if (userList.size() == 1 && userList.get(0).getLeftValue().isEmpty() && userList.get(0).getRightValue().isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else {  // Add sorted list of users into the table
			NonEditableTableModel tableModel = (NonEditableTableModel) manageUserPanel.getScrollableTable()
																					  .getTableModel();
			tableModel.removeAllRows();

			for (Pair<User, String> item : userList) {
				int infectiousUserId = item.getLeftValue().getInfectiousUserId();
				infectiousUserId = (infectiousUserId == 0) ? -1 : infectiousUserId;
				// Because of SQL INTEGER TYPE: NULL in SQL => 0 in Java
				// infectiousUserId only contains NULL or a number which is greater than 0.

				tableModel.addRow(new Object[]{
						item.getLeftValue().getUserId(),
						item.getLeftValue().getIdentifierNumber(),
						item.getLeftValue().getFullname(),
						User.STATUS_NAMES[item.getLeftValue().getStatus()],
						item.getRightValue(),
						infectiousUserId
				});
			}

			JOptionPane.showMessageDialog(
					sortDialog,
					"Sort successfully",
					"Sort User",
					JOptionPane.INFORMATION_MESSAGE
			);

			sortDialog.setVisible(false);
		}
	}

	private void cancelActionOfSortDialog() {
		int option = JOptionPane.showConfirmDialog(
				sortDialog,
				"Are you sure to close?",
				"Sort User",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION)
			sortDialog.setVisible(false);
	}

	private ArrayList<User> loadAllInvolvedUserList(UserDAO daoModel, int userInvolvedId) {
		// Load all users from the database.
		ArrayList<User> userList = (ArrayList<User>) daoModel.getAllByInfectiousUserId(userInvolvedId);

		// Check connection.
		if (userList.size() == 1 && userList.get(0).isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));

		return userList;
	}
}
