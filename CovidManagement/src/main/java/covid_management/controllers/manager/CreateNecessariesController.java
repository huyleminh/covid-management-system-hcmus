package covid_management.controllers.manager;

import covid_management.dao.NecessariesDAO;
import covid_management.models.Necessaries;
import covid_management.models.NecessariesHistory;
import covid_management.views.manager.dialogs.CreateNecessariesDialog;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.exceptions.DBConnectionException;
import shared.utilities.UtilityFunctions;

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
			createNecessariesDialog.setVisible(false);
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

		try {
			// Check existing of necessaries name
			NecessariesDAO daoModel = new NecessariesDAO();
			boolean isExistingNecessariesName = daoModel.isExistingNecessariesName(necessariesName);

			if (isExistingNecessariesName) {
				showErrorMessage(
						createNecessariesDialog,
						"Create Necessaries",
						"This necessaries name is existing"
				);
			} else {
				// Initialize new necessaries instance and make description to save history
				Necessaries necessaries = new Necessaries(
						-1,
						necessariesName,
						limitQuantity,
						startDate,
						expiredDate,
						price
				);
				String description = NecessariesHistory.generateDescriptionWithoutFormatting(
															   NecessariesHistory.ADD_NEW_NECESSARIES
													   )
													   .formatted(necessariesName);

				daoModel.create(necessaries, managerUsername, description);

				JOptionPane.showMessageDialog(
						createNecessariesDialog,
						"Create successfully",
						"Create Necessaries",
						JOptionPane.INFORMATION_MESSAGE
				);

				this.isSuccess = true;
				createNecessariesDialog.setVisible(false);
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
