package covid_management.controllers.manager;

import covid_management.dao.LocationDAO;
import covid_management.dao.UserDAO;
import covid_management.models.User;
import covid_management.views.manager.dialogs.CreateUserDialog;
import covid_management.views.manager.dialogs.EditUserDialog;
import covid_management.views.manager.dialogs.ViewUserDetailDialog;
import covid_management.views.manager.panels.ManageUserPanel;
import covid_management.views.shared.dialogs.SortDialog;
import shared.components.NonEditableTableModel;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.Pair;
import shared.utilities.UtilityFunctions;

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
		this.editUserController = new EditUserController(
				mainFrame,
				editUserDialog,
				connectionErrorDialog,
				username,
				manageUserPanel.getScrollableTable().getTable()
		);
		this.createUserController = new CreateUserController(
				mainFrame,
				createUserDialog,
				connectionErrorDialog,
				username
		);

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

	public void preprocessAndDisplayUI() {
		loadUserList();
		manageUserPanel.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectAction();
		} else if (event.getSource() == manageUserPanel.getSearchButton()) {
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
			sortDialog.setVisible(false);
		}
	}

	private void reconnectAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadUserList();
	}

	private void loadUserList() {
		NonEditableTableModel tableModel = (NonEditableTableModel) manageUserPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		try {
			// Load all users from the database.
			UserDAO daoModel = new UserDAO();
			ArrayList<Pair<User, String>> userList = daoModel.getAllWithLocationName();

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
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
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
			try {
				UserDAO daoModel = new UserDAO();
				final String field = searchOption.equals("ID Card") ? "identifierNumber" : "fullname";
				ArrayList<Pair<User, String>> userList = daoModel.searchByAndResultIncludedLocationName(field, searchValue);

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
			} catch (DBConnectionException e) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				e.printStackTrace();
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
			try {
				int userId = (int) table.getValueAt(selectedRow, 0);
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

					ArrayList<User> involvedUserList = daoModel.getAllByInfectiousUserId(user.getUserId());

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
						userInvolvedListTableModel.addRow(new Object[]{
								involvedUser.getIdentifierNumber(),
								involvedUser.getFullname(),
								involvedUser.getYearOfBirth(),
								User.STATUS_NAMES[involvedUser.getStatus()]
						});
					}

					viewUserDetailDialog.setVisible(true);
				}
			} catch (DBConnectionException e) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				e.printStackTrace();
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

		try {
			// Get sorted list of users
			UserDAO userDAOModel = new UserDAO();
			ArrayList<Pair<User, String>> userList = userDAOModel.sortByOneFieldWithLocationName(fieldName, isAscending);

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
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}
}
