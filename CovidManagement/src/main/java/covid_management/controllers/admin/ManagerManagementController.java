package covid_management.controllers.admin;

import covid_management.controllers.ValidationHandler;
import covid_management.dao.AccountDAO;
import covid_management.dao.NecessariesHistoryDAO;
import covid_management.dao.UserHistoryDAO;
import covid_management.models.Account;
import covid_management.models.NecessariesHistory;
import covid_management.models.UserHistory;
import covid_management.views.admin.dialogs.CreateAccountDialog;
import covid_management.views.admin.dialogs.ViewActivityDialog;
import covid_management.views.admin.panels.ManagerManagementPanel;
import shared.components.NonEditableTableModel;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;

public class ManagerManagementController implements ActionListener {
	final private JFrame mainFrame;
	final private ManagerManagementPanel managerManagementPanel;
	final private AccountDAO accountDAOModel;
	final private UserHistoryDAO userHistoryDAOModel;
	final private NecessariesHistoryDAO necessariesHistoryDAOModel;
	final private CreateAccountDialog createAccountDialog;
	final private ConnectionErrorDialog connectionErrorDialog;

	public ManagerManagementController(JFrame mainFrame, ManagerManagementPanel managerManagementPanel) {
		this.mainFrame = mainFrame;
		this.managerManagementPanel = managerManagementPanel;
		this.accountDAOModel = new AccountDAO();
		this.userHistoryDAOModel = new UserHistoryDAO();
		this.necessariesHistoryDAOModel = new NecessariesHistoryDAO();
		this.createAccountDialog = new CreateAccountDialog(mainFrame);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);

		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
		this.managerManagementPanel.getLockOrUnlockButton().addActionListener(this);
		this.managerManagementPanel.getViewActivityButton().addActionListener(this);
		this.managerManagementPanel.getCreateButton().addActionListener(this);
		this.createAccountDialog.getCreateButton().addActionListener(this);
		this.createAccountDialog.getCancelButton().addActionListener(this);
	}

	public void preprocessAndDisplayUI() {
		loadManagerList();
		managerManagementPanel.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectAction();
		} else if (event.getSource() == managerManagementPanel.getLockOrUnlockButton()) {
			lockOrUnlockAction();
		} else if (event.getSource() == managerManagementPanel.getViewActivityButton()) {
			viewActivityAction();
		} else if (event.getSource() == managerManagementPanel.getCreateButton()) {
			createAction();
		} else if (event.getSource() == createAccountDialog.getCreateButton()) {
			createActionOfCreateAccountDialog();
		} else if (event.getSource() == createAccountDialog.getCancelButton()) {
			createAccountDialog.setVisible(false);
		}
	}

	private void reconnectAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadManagerList();
	}

	private void lockOrUnlockAction() {
		JTable table = managerManagementPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		// Check whether admin did select a row or not.
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
					mainFrame,
					"Please select a row!",
					"Lock/Unlock account",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
			Vector<Object> rowValue = tableModel.getRowValue(selectedRow);
			final byte currentStatus = convertStatusStringToByte((String) rowValue.get(1));
			final String operationName = (currentStatus == Account.ACTIVE) ? "lock" : "unlock";

			int option = JOptionPane.showConfirmDialog(
					mainFrame,
					"Are you sure to " + operationName + " this account?",
					"Lock/Unlock account",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				Account dummyAccount = Account.createEmpty();
				dummyAccount.setUsername((String) rowValue.get(0));
				dummyAccount.setIsActive(toggleStatus(currentStatus));

				try {
					accountDAOModel.updateOneFieldByUsername(dummyAccount, "isActive");
					JOptionPane.showMessageDialog(
							mainFrame,
							((currentStatus == Account.ACTIVE) ? "Lock" : "Unlock") + " successfully",
							"Lock/Unlock account",
							JOptionPane.INFORMATION_MESSAGE
					);

					// TODO
					// loadManagerList();
					// or
					final String statusDidUpdate = convertStatusByteToString(toggleStatus(currentStatus));
					tableModel.setValueAt(statusDidUpdate, selectedRow, 1);

					table.clearSelection();
				} catch (DBConnectionException e) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					e.printStackTrace();
				}
			}
		}
	}

	private void viewActivityAction() {
		JTable table = managerManagementPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		// Check whether admin did select a row or not.
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
					mainFrame,
					"Please select a row!",
					"View activity",
					JOptionPane.INFORMATION_MESSAGE
			);
			return;
		}

		try {
			NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
			Vector<Object> rowValue = tableModel.getRowValue(selectedRow);

			// Load all rows belongs to selected manager.
			final String managerUsername = String.valueOf(rowValue.get(0));
			ArrayList<UserHistory> userHistoryList = userHistoryDAOModel.getAllByManagerUsername(managerUsername);
			ArrayList<NecessariesHistory> necessariesHistoryList = necessariesHistoryDAOModel.getAllByManagerUsername(
					managerUsername
			);

			ViewActivityDialog viewActivityDialog = new ViewActivityDialog(mainFrame);
			viewActivityDialog.getManagerUsernameTextField().setText(" Manager's username: " + managerUsername);

			NonEditableTableModel viewActivityTableModel = (NonEditableTableModel) viewActivityDialog.getScrollableTable()
																									 .getTableModel();

			// Add userHistoryList into the table.
			if (!userHistoryList.isEmpty()) {
				for (UserHistory userHistory : userHistoryList) {
					viewActivityTableModel.addRow(new Object[]{
							userHistory.getDescription(),
							UtilityFunctions.formatTimestamp(
									Constants.TIMESTAMP_WITHOUT_NANOSECOND,
									userHistory.getDate()
							)
					});
				}
			}

			// Add userHistoryList into the table.
			if (!necessariesHistoryList.isEmpty()) {
				for (NecessariesHistory necessariesHistory : necessariesHistoryList) {
					viewActivityTableModel.addRow(new Object[]{
							necessariesHistory.getDescription(),
							UtilityFunctions.formatTimestamp(
									Constants.TIMESTAMP_WITHOUT_NANOSECOND,
									necessariesHistory.getDate()
							)
					});
				}
			}

			// TODO: Remember to clear all data after this dialog is hiding. But currently it is redundant because that dialog is created inside this function.
			viewActivityDialog.setVisible(true);

			table.clearSelection();
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void createAction() {
		createAccountDialog.getUsernameField().setText("");
		createAccountDialog.getPasswordFieldPanel().getPasswordField().setText("");
		createAccountDialog.getPasswordFieldPanel().setPasswordVisible(false);
		createAccountDialog.getConfirmPasswordFieldPanel().getPasswordField().setText("");
		createAccountDialog.getConfirmPasswordFieldPanel().setPasswordVisible(false);
		createAccountDialog.setVisible(true);
	}

	private void createActionOfCreateAccountDialog() {
		// get username and password to validate and check correction
		final String username = createAccountDialog.getUsernameField().getText();
		final String password = String.valueOf(createAccountDialog.getPasswordFieldPanel().getPasswordValue());
		final String confirmPassword = String.valueOf(createAccountDialog.getConfirmPasswordFieldPanel().getPasswordValue());

		// Validate username and password
		if (
				!ValidationHandler.validateUsername(username) ||
				!ValidationHandler.validatePassword(password) ||
				!ValidationHandler.validatePassword(confirmPassword)
		) {
			showErrorMessage(
					createAccountDialog,
					"Create Account Manager",
					"Invalid username or password or confirm password"
			);
		}
		else if (!password.equals(confirmPassword)) {
			showErrorMessage(createAccountDialog, "Create Account Manager", "Confirm password does not match with password");
		} else {

			try {
				Optional<Account> optionalAccount = accountDAOModel.get(username);

				if (optionalAccount.isEmpty()) {
					showErrorMessage(createAccountDialog, "Create Account Manager", "This username is existing");
				} else {
					int option = JOptionPane.showConfirmDialog(
							createAccountDialog,
							"Are you sure to create this account?",
							"Create Account Manager",
							JOptionPane.YES_NO_OPTION
					);

					if (option == JOptionPane.YES_OPTION) {
						accountDAOModel.create(
								new Account(
										username,
										UtilityFunctions.hashPassword(password),
										Account.MANAGER,
										Account.ACTIVE,
										-1  // -1 as NULL in SQL
								)
						);

						JOptionPane.showMessageDialog(
								createAccountDialog,
								"This account is created successfully",
								"Create Account Manager",
								JOptionPane.INFORMATION_MESSAGE
						);

						createAccountDialog.setVisible(false);
						NonEditableTableModel tableModel = (NonEditableTableModel) managerManagementPanel.getScrollableTable()
																										 .getTableModel();
						tableModel.addRow(new Object[]{username, "Active"});
					}
				}
			} catch (DBConnectionException e) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				e.printStackTrace();
			}
		}
	}

	// Get list of managers from the database and display their username and status (active or inactive).
	private void loadManagerList() {
		// Clear all values if possible.
		NonEditableTableModel tableModel = (NonEditableTableModel) managerManagementPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		// Load list of managers' username and status from the Account table.
		try {
			ArrayList<Account> managerList = accountDAOModel.getAllByRole(Account.MANAGER);

			for (Account account : managerList) {
				tableModel.addRow(
						new Object[]{
								account.getUsername(),
								convertStatusByteToString(account.getIsActive())
						}
				);
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private byte convertStatusStringToByte(String status) {
		return status.equals("Active") ? Account.ACTIVE : Account.INACTIVE;
	}

	private String convertStatusByteToString(byte status) {
		return status == Account.ACTIVE ? "Active" : "Locked";
	}

	private byte toggleStatus(byte status) {
		return status == Account.ACTIVE ? Account.INACTIVE : Account.ACTIVE;
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
