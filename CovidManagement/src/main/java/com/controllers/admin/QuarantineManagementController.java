package com.controllers.admin;

import com.dao.LocationDAO;
import com.models.Location;
import com.models.table.NonEditableTableModel;
import com.utilities.SingletonDBConnection;
import com.utilities.UtilityFunctions;
import com.views.admin.dialogs.CreateQuarantineDialog;
import com.views.admin.dialogs.EditQuarantineDialog;
import com.views.admin.panels.QuarantineManagementPanel;
import com.views.shared.dialogs.ConnectionErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Vector;

public class QuarantineManagementController implements ActionListener {
	private JFrame mainFrame;
	private QuarantineManagementPanel quarantineManagementPanel;
	private EditQuarantineDialog editQuarantineDialog;
	private CreateQuarantineDialog createQuarantineDialog;
	private ConnectionErrorDialog connectionErrorDialog;

	public QuarantineManagementController(JFrame mainFrame, QuarantineManagementPanel quarantineManagementPanel) {
		this.mainFrame = mainFrame;
		this.quarantineManagementPanel = quarantineManagementPanel;
		this.editQuarantineDialog = new EditQuarantineDialog(mainFrame);
		this.createQuarantineDialog = new CreateQuarantineDialog(mainFrame);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);

		this.connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
			connectionErrorDialog.setExitOnCloseButton(false);
			connectionErrorDialog.setVisible(false);

			SingletonDBConnection.getInstance().connect();
			loadLocationList();
		});

		this.quarantineManagementPanel.getEditButton().addActionListener(this);
		this.quarantineManagementPanel.getCreateButton().addActionListener(this);
		this.editQuarantineDialog.getSaveButton().addActionListener(this);
		this.editQuarantineDialog.getCancelButton().addActionListener(this);
		this.createQuarantineDialog.getCreateButton().addActionListener(this);
		this.createQuarantineDialog.getCancelButton().addActionListener(this);

		// Add component listeners
		this.createQuarantineDialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				createQuarantineDialog.getLocationNameField().setText("");
				createQuarantineDialog.getCapacityField().setValue(1);
			}
		});
		this.quarantineManagementPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent event) {
				loadLocationList();
			}
		});

		loadLocationList();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == quarantineManagementPanel.getEditButton()) {
			editAction();
		} else if (event.getSource() == quarantineManagementPanel.getCreateButton()) {
			createQuarantineDialog.setVisible(true);
		} else if (event.getSource() == editQuarantineDialog.getSaveButton()) {
			saveActionOfEditQuarantineDialog();
		} else if (event.getSource() == editQuarantineDialog.getCancelButton()) {
			cancelActionOfEditQuarantineDialog();
		} else if (event.getSource() == createQuarantineDialog.getCreateButton()) {
			createActionOfCreateQuarantineDialog();
		} else if (event.getSource() == createQuarantineDialog.getCancelButton()) {
			cancelActionOfCreateQuarantineDialog();
		}
	}

	// Get list of quarantine locations from the database and display their information on the screen.
	private void loadLocationList() {
		// Clear all data if possible.
		NonEditableTableModel tableModel = (NonEditableTableModel) quarantineManagementPanel.getScrollableTable()
																							.getTableModel();
		tableModel.removeAllRows();

		// Load list of quarantine locations (quarantine location name, capacity, current slots)
		LocationDAO daoModel = new LocationDAO();
		ArrayList<Location> locationList = (ArrayList<Location>) daoModel.getAll();

		// Check connection
		if (locationList.size() == 1 && locationList.get(0).isEmpty())
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else if (!locationList.isEmpty()) {  // If connection is available, then add those into the table
			for (Location location : locationList) {
				tableModel.addRow(new Object[] {
						location.getLocationId(),  // hidden column
						location.getLocationName(),
						location.getCapacity(),
						location.getCurrentSlots()
				});
			}
		}
	}

	private void editAction() {
		JTable table = quarantineManagementPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		// Check whether admin did select a row or not.
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
					mainFrame,
					"Please select a row!",
					"Edit Quarantine Location",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
			Vector<Object> rowValue = tableModel.getRowValue(selectedRow);

			editQuarantineDialog.getLocationNameField().setText(String.valueOf(rowValue.get(1)));
			editQuarantineDialog.getCapacityField().setValue(rowValue.get(2));

			editQuarantineDialog.setVisible(true);
		}
	}

	private void saveActionOfEditQuarantineDialog() {
		JTable table = quarantineManagementPanel.getScrollableTable().getTable();
		NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
		Vector<Object> rowValue = tableModel.getRowValue(table.getSelectedRow());
		Location oldLocation = new Location(
				(int) rowValue.get(0),
				String.valueOf(rowValue.get(1)),
				(short) rowValue.get(2),
				(short) rowValue.get(3)
		);

		final String locationName = UtilityFunctions.removeRedundantWhiteSpace(
				editQuarantineDialog.getLocationNameField().getText()
		);
		final short capacity = Short.parseShort(editQuarantineDialog.getCapacityField().getText());

		if (locationName.equals(oldLocation.getLocationName()) && capacity == oldLocation.getCapacity()) {
			int option = JOptionPane.showConfirmDialog(
					editQuarantineDialog,
					"Information does not change. Are you sure to close?",
					"Edit Quarantine Location",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION)
				editQuarantineDialog.setVisible(false);
		} else if (locationName.isBlank() || locationName.length() > 50){
			showErrorMessage(editQuarantineDialog, "Edit Quarantine Location", "Location name is invalid");
		} else if (capacity < oldLocation.getCurrentSlots()) {
			showErrorMessage(
					editQuarantineDialog,
					"Edit Quarantine Location",
					"The capacity must be greater than or equal to the current slots"
			);
		} else {
			LocationDAO daoModel = new LocationDAO();
			byte state = daoModel.isExisting("locationName", locationName);

			if (state == LocationDAO.CONNECTION_ERROR) {  // Check connection.
				editQuarantineDialog.setVisible(false);
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			} else if (state == LocationDAO.EXISTING && !locationName.equals(oldLocation.getLocationName())) {
				// Check existence of this location name.
				showErrorMessage(
						editQuarantineDialog,
						"Edit Quarantine Location",
						"This location is existing"
				);
			} else {
				saveEditedLocation(daoModel, new Location(
						oldLocation.getLocationId(),
						locationName,
						capacity,
						(short) -1
				));
			}
		}
	}

	private void cancelActionOfEditQuarantineDialog() {
		int option = JOptionPane.showConfirmDialog(
				editQuarantineDialog,
				"Are you sure to close?",
				"Edit Quarantine Location",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION)
			editQuarantineDialog.setVisible(false);
	}

	private void createActionOfCreateQuarantineDialog() {
		// Get location name, capacity and current slots field.
		final String locationName = UtilityFunctions.removeRedundantWhiteSpace(
				createQuarantineDialog.getLocationNameField().getText()
		);
		final short capacity = Short.parseShort(createQuarantineDialog.getCapacityField().getText());

		// Validate location name
		if (locationName.isBlank() || locationName.length() > 50) {
			showErrorMessage(
					createQuarantineDialog,
					"Create Quarantine Location",
					"Location name is invalid"
			);
			return;
		}

		LocationDAO locationDAO = new LocationDAO();
		byte state = locationDAO.isExisting("locationName", locationName);

		if (state == LocationDAO.CONNECTION_ERROR)  // Check connection.
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		else if (state == LocationDAO.EXISTING) {  // Check existence of this location name.
			showErrorMessage(
					createQuarantineDialog,
					"Create Quarantine Location",
					"This location is existing"
			);
		} else {
			int option = JOptionPane.showConfirmDialog(
					createQuarantineDialog,
					"Are you sure to create this location",
					"Create Quarantine Location",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				// Save those data to database.
				boolean isCreated = locationDAO.create(new Location(-1, locationName, capacity, (short) 0));

				if (!isCreated)
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				else {
					JOptionPane.showMessageDialog(
							createQuarantineDialog,
							"This location is created successfully",
							"Create Quarantine Location",
							JOptionPane.INFORMATION_MESSAGE
					);

					createQuarantineDialog.setVisible(false);
					loadLocationList();
				}
			}
		}
	}

	private void cancelActionOfCreateQuarantineDialog() {
		int option = JOptionPane.showConfirmDialog(
				createQuarantineDialog,
				"Are you sure to close?",
				"Create Quarantine Location",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION)
			createQuarantineDialog.setVisible(false);
	}

	private void saveEditedLocation(LocationDAO daoModel, Location editedLocation) {
		int option = JOptionPane.showConfirmDialog(
				editQuarantineDialog,
				"Are you sure to save this location",
				"Create Quarantine Location",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION) {
			// Save those data to database.
			boolean isCreated = daoModel.updateExceptCurrentSlots(editedLocation);

			if (!isCreated) {
				editQuarantineDialog.setVisible(false);
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			} else {
				JOptionPane.showMessageDialog(
						editQuarantineDialog,
						"This location is edited successfully",
						"Create Quarantine Location",
						JOptionPane.INFORMATION_MESSAGE
				);

				editQuarantineDialog.setVisible(false);

				JTable table = quarantineManagementPanel.getScrollableTable().getTable();
				NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
				int selectedRow = table.getSelectedRow();

				table.clearSelection();
				tableModel.setValueAt(editedLocation.getLocationName(), selectedRow, 1);
				tableModel.setValueAt(editedLocation.getCapacity(), selectedRow, 2);
				tableModel.setValueAt(editedLocation.getCurrentSlots(), selectedRow, 3);
			}
		}
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
