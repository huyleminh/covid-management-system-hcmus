package com.controllers.admin;

import com.dao.AccountDAO;
import com.dao.NecessariesHistoryDAO;
import com.dao.UserHistoryDAO;
import com.models.Account;
import com.models.NecessariesHistory;
import com.models.UserHistory;
import com.models.table.NonEditableTableModel;
import com.utilities.SingletonDBConnection;
import com.utilities.UtilityFunctions;
import com.utilities.ValidationHandler;
import com.views.admin.dialogs.CreateAccountDialog;
import com.views.admin.dialogs.ViewActivityDialog;
import com.views.admin.panels.ManagerManagementPanel;
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

public class ManagerManagementController implements ActionListener {
	private JFrame mainFrame;
	private ManagerManagementPanel managerManagementPanel;
	private AccountDAO accountDAOModel;
	private UserHistoryDAO userHistoryDAOModel;
	private NecessariesHistoryDAO necessariesHistoryDAOModel;
	private CreateAccountDialog createAccountDialog;
	private ConnectionErrorDialog connectionErrorDialog;

	public ManagerManagementController(JFrame mainFrame, ManagerManagementPanel managerManagementPanel) {
		this.mainFrame = mainFrame;
		this.managerManagementPanel = managerManagementPanel;
		this.accountDAOModel = new AccountDAO();
		this.userHistoryDAOModel = new UserHistoryDAO();
		this.necessariesHistoryDAOModel = new NecessariesHistoryDAO();
		this.createAccountDialog = new CreateAccountDialog(mainFrame);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);

		this.connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
			connectionErrorDialog.setExitOnCloseButton(false);
			connectionErrorDialog.setVisible(false);

			SingletonDBConnection.getInstance().connect();
			loadManagerList();
		});

		this.managerManagementPanel.getLockOrUnlockButton().addActionListener(this);
		this.managerManagementPanel.getViewActivityButton().addActionListener(this);
		this.managerManagementPanel.getCreateButton().addActionListener(this);
		this.createAccountDialog.getCreateButton().addActionListener(this);
		this.createAccountDialog.getCancelButton().addActionListener(this);

		loadManagerList();

		// Add component listener
		this.createAccountDialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				createAccountDialog.getUsernameField().setText("");
				createAccountDialog.getPasswordFieldPanel().getPasswordField().setText("");
				createAccountDialog.getPasswordFieldPanel().setPasswordVisible(false);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == managerManagementPanel.getLockOrUnlockButton()) {
			lockOrUnlockAction();
		} else if (event.getSource() == managerManagementPanel.getViewActivityButton()) {
			viewActivityAction();
		} else if (event.getSource() == managerManagementPanel.getCreateButton()) {
			createAccountDialog.setVisible(true);
		} else if (event.getSource() == createAccountDialog.getCreateButton()) {
			createActionOfCreateManagerDialog();
		} else if (event.getSource() == createAccountDialog.getCancelButton()) {
			cancelActionOfCreateManagerDialog();
		}
	}

	// Get list of managers from the database and display their username and status (active or inactive).
	private void loadManagerList() {
		// Clear all values if possible.
		NonEditableTableModel tableModel = (NonEditableTableModel) managerManagementPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		// Load list of managers' username and status from the Account table.
		ArrayList<Account> managerList = (ArrayList<Account>) accountDAOModel.getAllByRole(Account.MANAGER);

		// If this list only contains an empty account, showing Connection Failed dialog.
		if (managerList.size() == 1 && managerList.get(0).equals(Account.emptyAccount))
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else if (!managerList.isEmpty()) {  // Otherwise, adding this list into the table using the table model.
			for (Account account : managerList) {
				tableModel.addRow(new Object[] { account.getUsername(), convertStatusByteToString(account.getIsActive()) });
			}
		}
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

				boolean isUpdated = accountDAOModel.updateOneField(dummyAccount, "isActive");

				if (!isUpdated) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				} else {
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
				}

				table.clearSelection();
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
		} else {
			NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
			Vector<Object> rowValue = tableModel.getRowValue(selectedRow);

			// Load all rows belongs to selected manager.
			final String managerUsername = String.valueOf(rowValue.get(0));
			ArrayList<UserHistory> userHistoryList = (ArrayList<UserHistory>) userHistoryDAOModel.getAllByManagerUsername(managerUsername);
			ArrayList<NecessariesHistory> necessariesHistoryList = (ArrayList<NecessariesHistory>) necessariesHistoryDAOModel.getAllByManagerUsername(managerUsername);

			// Check connection to show Connection Failed dialog if the connection is unavailable.
			if (
					(userHistoryList.size() == 1 && userHistoryList.get(0).isEmpty()) ||
							(necessariesHistoryList.size() == 1 && necessariesHistoryList.get(0).isEmpty())
			) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			} else {
				ViewActivityDialog viewActivityDialog = new ViewActivityDialog(mainFrame);
				viewActivityDialog.getManagerUsernameTextField().setText("Manager's username: " + managerUsername);

				NonEditableTableModel viewActivityTableModel = (NonEditableTableModel) viewActivityDialog.getScrollableTable()
						.getTableModel();

				// Add userHistoryList into the table.
				if (!userHistoryList.isEmpty()) {
					for (UserHistory userHistory : userHistoryList) {
						viewActivityTableModel.addRow(new Object[] {
								userHistory.getDescription(),
								userHistory.getDate().toString()
						});
					}
				}

				// Add userHistoryList into the table.
				if (!necessariesHistoryList.isEmpty()) {
					for (NecessariesHistory necessariesHistory : necessariesHistoryList) {
						viewActivityTableModel.addRow(new Object[] {
								necessariesHistory.getDescription(),
								necessariesHistory.getDate().toString()
						});
					}
				}

				// TODO: Remember to clear all data after this dialog is hiding. But currently it is redundant because that dialog is created inside this function.
				viewActivityDialog.setVisible(true);
			}

			table.clearSelection();
		}
	}

	private void createActionOfCreateManagerDialog() {
		// get username and password to validate and check correction
		final String username = createAccountDialog.getUsernameField().getText();
		final String password = String.valueOf(createAccountDialog.getPasswordFieldPanel().getPasswordField().getPassword());

		// Validate username and password
		if (!ValidationHandler.validateUsername(username) || !ValidationHandler.validatePassword(password))
			showErrorMessage(createAccountDialog, "Create Account Manager", "Invalid username or password");
		else {
			Optional<Account> optionalAccount = accountDAOModel.get(username);

			if (optionalAccount.isPresent()) {
				if (optionalAccount.get().isEmpty())
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				else
					showErrorMessage(createAccountDialog, "Create Account Manager", "This username is existing");
			} else {
				int option = JOptionPane.showConfirmDialog(
						createAccountDialog,
						"Are you sure to create this account?",
						"Create Account Manager",
						JOptionPane.YES_NO_OPTION
				);

				if (option == JOptionPane.YES_OPTION) {
					Account newManagerAccount = new Account(
							username, UtilityFunctions.hashPassword(password), Account.MANAGER, Account.ACTIVE, -1
					);  // -1 as null

					boolean isCreated = accountDAOModel.create(newManagerAccount);

					if (!isCreated)
						SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					else {
						JOptionPane.showMessageDialog(
								createAccountDialog,
								"This account is created successfully",
								"Create Account Manager",
								JOptionPane.INFORMATION_MESSAGE
						);

						createAccountDialog.setVisible(false);
						NonEditableTableModel tableModel = (NonEditableTableModel) managerManagementPanel.getScrollableTable().getTableModel();
						tableModel.addRow(new Object[] { username, "Active" });
					}
				}
			}
		}
	}

	private void cancelActionOfCreateManagerDialog() {
		int option = JOptionPane.showConfirmDialog(
				createAccountDialog,
				"Are you sure to close?",
				"Create Account Manager",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION)
			createAccountDialog.setVisible(false);
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
