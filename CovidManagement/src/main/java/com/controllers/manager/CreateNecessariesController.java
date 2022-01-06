package com.controllers.manager;

import com.dao.NecessariesDAO;
import com.models.Necessaries;
import com.models.NecessariesHistory;
import com.utilities.UtilityFunctions;
import com.views.manager.dialogs.CreateNecessariesDialog;
import com.views.shared.dialogs.ConnectionErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class CreateNecessariesController implements ActionListener {
	final private CreateNecessariesDialog createNecessariesDialog;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private String managerUsername;

	private boolean isSuccess;

	public CreateNecessariesController(
			CreateNecessariesDialog createNecessariesDialog,
			ConnectionErrorDialog connectionErrorDialog,
			String username
	) {
		// Initialization
		this.createNecessariesDialog = createNecessariesDialog;
		this.connectionErrorDialog = connectionErrorDialog;
		this.managerUsername = username;
		this.isSuccess = false;

		// Add actions listeners
		this.createNecessariesDialog.getCreateButton().addActionListener(this);
		this.createNecessariesDialog.getCancelButton().addActionListener(this);
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void preprocessAndDisplayUI() {
		this.isSuccess = false;
		createNecessariesDialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == createNecessariesDialog.getCreateButton()) {
			createAction();
		} else if (event.getSource() == createNecessariesDialog.getCancelButton()) {
			cancelAction();
		}
	}

	private void createAction() {
		// Get values
		String necessariesName = UtilityFunctions.removeRedundantWhiteSpace(
				createNecessariesDialog.getNecessariesNameTextField().getText()
		);
		byte limitQuantity = Byte.parseByte(createNecessariesDialog.getLimitedQuantityTextField().getText());
		int price = Integer.parseInt(createNecessariesDialog.getPriceTextField().getText());
		Timestamp startDate = Timestamp.valueOf(
				"%04d-%02d-%02d 00:00:00".formatted(
						createNecessariesDialog.getStartDateChooser().getSelectedYear(),
						createNecessariesDialog.getStartDateChooser().getSelectedMonth(),
						createNecessariesDialog.getStartDateChooser().getSelectedDay()
				)
		);
		Timestamp expiredDate = Timestamp.valueOf(
				"%04d-%02d-%02d 23:59:59".formatted(
						createNecessariesDialog.getEndDateChooser().getSelectedYear(),
						createNecessariesDialog.getEndDateChooser().getSelectedMonth(),
						createNecessariesDialog.getEndDateChooser().getSelectedDay()
				)
		);

		// Validation
		if (necessariesName.isEmpty() || necessariesName.length() > 50) {
			showErrorMessage(createNecessariesDialog, "Create Necessaries", "Invalid necessaries name");
			return;
		} else if (startDate.compareTo(expiredDate) > 0) {
			showErrorMessage(createNecessariesDialog, "Create Necessaries", "Invalid start date or expired date");
			return;
		}

		// Check existing of necessaries name
		byte isExistingNecessariesName = isExistingNecessariesName(necessariesName);
		if (isExistingNecessariesName == NecessariesDAO.CONNECTION_ERROR) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			return;
		} else if (isExistingNecessariesName == NecessariesDAO.EXISTING) {
			showErrorMessage(createNecessariesDialog, "Create Necessaries", "This necessaries name is existing");
			return;
		}

		// Initialize new necessaries instance and make description to save history
		Necessaries necessaries = new Necessaries(
				-1, necessariesName, limitQuantity, startDate, expiredDate, price
		);
		String description = NecessariesHistory.generateDescriptionWithoutFormatting(NecessariesHistory.ADD_NEW_NECESSARIES)
											   .formatted(necessariesName);

		NecessariesDAO daoModel = new NecessariesDAO();
		boolean isCreated = daoModel.create(necessaries, managerUsername, description);

		if (!isCreated) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
		} else {
			JOptionPane.showMessageDialog(
					createNecessariesDialog,
					"Create successfully",
					"Create Necessaries",
					JOptionPane.INFORMATION_MESSAGE
			);

			this.isSuccess = true;
			createNecessariesDialog.setVisible(false);
		}
	}

	private void cancelAction() {
		int option = JOptionPane.showConfirmDialog(
				createNecessariesDialog,
				"Are you sure to close?",
				"Create Necessaries",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION)
			createNecessariesDialog.setVisible(false);
	}

	private byte isExistingNecessariesName(String newNecessariesName) {
		NecessariesDAO daoModel = new NecessariesDAO();
		return daoModel.isExistingNecessariesName(newNecessariesName);
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);

	}
}
