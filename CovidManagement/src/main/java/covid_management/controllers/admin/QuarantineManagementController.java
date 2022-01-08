package covid_management.controllers.admin;

import covid_management.dao.LocationDAO;
import covid_management.models.Location;
import covid_management.views.admin.dialogs.CreateQuarantineDialog;
import covid_management.views.admin.dialogs.EditQuarantineDialog;
import covid_management.views.admin.panels.QuarantineManagementPanel;
import shared.components.NonEditableTableModel;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class QuarantineManagementController implements ActionListener {
	final private JFrame mainFrame;
	final private QuarantineManagementPanel quarantineManagementPanel;
	final private EditQuarantineDialog editQuarantineDialog;
	final private CreateQuarantineDialog createQuarantineDialog;
	final private ConnectionErrorDialog connectionErrorDialog;

	public QuarantineManagementController(JFrame mainFrame, QuarantineManagementPanel quarantineManagementPanel) {
		this.mainFrame = mainFrame;
		this.quarantineManagementPanel = quarantineManagementPanel;
		this.editQuarantineDialog = new EditQuarantineDialog(mainFrame);
		this.createQuarantineDialog = new CreateQuarantineDialog(mainFrame);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);

		this.quarantineManagementPanel.getEditButton().addActionListener(this);
		this.quarantineManagementPanel.getCreateButton().addActionListener(this);
		this.editQuarantineDialog.getSaveButton().addActionListener(this);
		this.editQuarantineDialog.getCancelButton().addActionListener(this);
		this.createQuarantineDialog.getCreateButton().addActionListener(this);
		this.createQuarantineDialog.getCancelButton().addActionListener(this);
	}

	public void preprocessAndDisplayUI() {
		loadLocationList();
		quarantineManagementPanel.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectAction();
		} else if (event.getSource() == quarantineManagementPanel.getEditButton()) {
			editAction();
		} else if (event.getSource() == quarantineManagementPanel.getCreateButton()) {
			createAction();
		} else if (event.getSource() == editQuarantineDialog.getSaveButton()) {
			saveActionOfEditQuarantineDialog();
		} else if (event.getSource() == editQuarantineDialog.getCancelButton()) {
			editQuarantineDialog.setVisible(false);
		} else if (event.getSource() == createQuarantineDialog.getCreateButton()) {
			createActionOfCreateQuarantineDialog();
		} else if (event.getSource() == createQuarantineDialog.getCancelButton()) {
			createQuarantineDialog.setVisible(false);
		}
	}

	// Get list of quarantine locations from the database and display their information on the screen.
	private void loadLocationList() {
		// Clear all data if possible.
		NonEditableTableModel tableModel = (NonEditableTableModel) quarantineManagementPanel.getScrollableTable()
																							.getTableModel();
		tableModel.removeAllRows();

		// Load list of quarantine locations (quarantine location name, capacity, current slots)
		try {
			LocationDAO daoModel = new LocationDAO();
			ArrayList<Location> locationList = daoModel.getAll();

			for (Location location : locationList) {
				if (!location.getLocationName().equals("Không có")) {
					tableModel.addRow(new Object[] {
							location.getLocationId(),  // hidden column
							location.getLocationName(),
							location.getCapacity(),
							location.getCurrentSlots()
					});
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void reconnectAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadLocationList();
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

	private void createAction() {
		createQuarantineDialog.getLocationNameField().setText("");
		createQuarantineDialog.getCapacityField().setValue(1);
		createQuarantineDialog.setVisible(true);
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

			try {
				LocationDAO daoModel = new LocationDAO();
				boolean isExisting = daoModel.isExisting("locationName", locationName);

				if (isExisting && !locationName.equals(oldLocation.getLocationName())) {
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
			} catch (DBConnectionException e) {
				editQuarantineDialog.setVisible(false);
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				e.printStackTrace();
			}
		}
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

		try {
			LocationDAO locationDAO = new LocationDAO();
			boolean isExisting = locationDAO.isExisting("locationName", locationName);

			if (isExisting) {  // Check existence of this location name.
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
					locationDAO.create(new Location(-1, locationName, capacity, (short) 0));

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
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
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
			try {
				daoModel.updateExceptCurrentSlots(editedLocation);

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
			} catch (DBConnectionException e) {
				editQuarantineDialog.setVisible(false);
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				e.printStackTrace();
			}
		}
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
