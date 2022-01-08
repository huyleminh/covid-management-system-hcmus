package covid_management.controllers.manager;

import covid_management.dao.NecessariesDAO;
import covid_management.models.Necessaries;
import covid_management.models.NecessariesHistory;
import covid_management.views.manager.dialogs.EditNecessariesDialog;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.exceptions.DBConnectionException;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;

public class EditNecessariesController implements ActionListener {
	final private EditNecessariesDialog editNecessariesDialog;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private String managerUsername;

	private Necessaries originalNecessaries;
	private boolean isSuccess;

	public EditNecessariesController(
			EditNecessariesDialog editNecessariesDialog,
			ConnectionErrorDialog connectionErrorDialog,
			String username
	) {
		// Initialization
		this.editNecessariesDialog = editNecessariesDialog;
		this.connectionErrorDialog = connectionErrorDialog;
		this.managerUsername = username;
		this.isSuccess = false;

		// Add actions listeners
		this.editNecessariesDialog.getSaveButton().addActionListener(this);
		this.editNecessariesDialog.getCancelButton().addActionListener(this);
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void preprocessAndDisplayUI(Necessaries originalNecessaries) {
		this.originalNecessaries = originalNecessaries;
		this.isSuccess = false;

		// Set original values
		editNecessariesDialog.getNecessariesNameTextField().setText(originalNecessaries.getNecessariesName());
		editNecessariesDialog.getLimitedQuantityTextField().setValue(originalNecessaries.getLimit());
		editNecessariesDialog.getPriceTextField().setValue(originalNecessaries.getPrice());
		editNecessariesDialog.getStartDateChooser().setSelectedDate(originalNecessaries.getStartDate());
		editNecessariesDialog.getEndDateChooser().setSelectedDate(originalNecessaries.getExpiredDate());

		// display UI
		editNecessariesDialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == editNecessariesDialog.getSaveButton()) {
			saveAction();
		} else if (event.getSource() == editNecessariesDialog.getCancelButton()) {
			editNecessariesDialog.setVisible(false);
		}
	}

	private void saveAction() {
		// Get editing values
		String newNecessariesName = UtilityFunctions.removeRedundantWhiteSpace(
				editNecessariesDialog.getNecessariesNameTextField().getText()
		);
		byte newQuantity = Byte.parseByte(editNecessariesDialog.getLimitedQuantityTextField().getText());
		int newPrice = Integer.parseInt(editNecessariesDialog.getPriceTextField().getText());
		String newStartDateAsString = "%04d-%02d-%02d 00:00:00".formatted(
				editNecessariesDialog.getStartDateChooser().getSelectedYear(),
				editNecessariesDialog.getStartDateChooser().getSelectedMonth(),
				editNecessariesDialog.getStartDateChooser().getSelectedDay()
		);
		String newEndDateAsString = "%04d-%02d-%02d 23:59:59".formatted(
				editNecessariesDialog.getEndDateChooser().getSelectedYear(),
				editNecessariesDialog.getEndDateChooser().getSelectedMonth(),
				editNecessariesDialog.getEndDateChooser().getSelectedDay()
		);

		// Initialize variables for updating
		ArrayList<String> fields = new ArrayList<>();
		ArrayList<Object> values = new ArrayList<>();
		ArrayList<String> descriptionList = new ArrayList<>();
		ArrayList<Byte> operationTypes = new ArrayList<>();

		try {
			// Validation of necessaries name
			boolean isValidNecessariesName = validateAndAddNecessariesNameWillChange(
					fields,
					values,
					descriptionList,
					operationTypes,
					newNecessariesName
			);

			if (!isValidNecessariesName) {
				showErrorMessage(editNecessariesDialog, "Edit Necessaries", "Invalid necessaries name");
				return;
			}

			// Add changed attributes
			addChangedQuantity(fields, values, descriptionList, operationTypes, newQuantity);
			addChangedPrice(fields, values, descriptionList, operationTypes, newPrice);

			// Validation of start date and expired date
			boolean isValidDate = validateAndAddDateWillChange(
					fields,
					values,
					descriptionList,
					operationTypes,
					newStartDateAsString,
					newEndDateAsString
			);
			if (!isValidDate) {
				showErrorMessage(editNecessariesDialog, "Edit Necessaries", "Invalid start date or expired date");
				return;
			}

			if (fields.isEmpty()) {
				int option = JOptionPane.showConfirmDialog(
						editNecessariesDialog,
						"Information does not change. Are you sure to close?",
						"Edit User",
						JOptionPane.YES_NO_OPTION
				);

				if (option == JOptionPane.YES_OPTION)
					editNecessariesDialog.setVisible(false);
			} else {
				NecessariesDAO daoModel = new NecessariesDAO();
				daoModel.update(
						originalNecessaries.getNecessariesId(),
						fields,
						values,
						managerUsername,
						descriptionList,
						operationTypes
				);

				JOptionPane.showMessageDialog(
						editNecessariesDialog,
						"Edit successfully",
						"Edit Necessaries",
						JOptionPane.INFORMATION_MESSAGE
				);

				this.isSuccess = true;
				editNecessariesDialog.setVisible(false);
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private boolean validateDate(String newStartDateAsString, String newEndDateAsString) {
		Timestamp startDate = Timestamp.valueOf(newStartDateAsString);
		Timestamp endDate = Timestamp.valueOf(newEndDateAsString);

		return startDate.compareTo(endDate) <= 0;
	}

	private boolean validateAndAddNecessariesNameWillChange(
			ArrayList<String> fields,
			ArrayList<Object> values,
			ArrayList<String> descriptionList,
			ArrayList<Byte> operationTypes,
			String newNecessariesName
	) throws DBConnectionException {
		if (newNecessariesName.isEmpty() || newNecessariesName.length() > 50)
			return false;
		if (originalNecessaries.getNecessariesName().equals(newNecessariesName))
			return true;

		NecessariesDAO daoModel = new NecessariesDAO();
		boolean isExisting = daoModel.isExistingNecessariesName(newNecessariesName);

		if (isExisting) {
			showErrorMessage(
					editNecessariesDialog,
					"Edit Necessaries",
					"This necessaries name is existing"
			);
			return false;
		}

		String description = NecessariesHistory.generateDescriptionWithoutFormatting(
													   NecessariesHistory.CHANGE_NECESSARIES_NAME
											   )
											   .formatted(
													   originalNecessaries.getNecessariesName(),
													   newNecessariesName
											   );

		fields.add("necessariesName");
		values.add(newNecessariesName);
		descriptionList.add(description);
		operationTypes.add(NecessariesHistory.CHANGE_NECESSARIES_NAME);

		return true;
	}

	private void addChangedQuantity(
			ArrayList<String> fields,
			ArrayList<Object> values,
			ArrayList<String> descriptionList,
			ArrayList<Byte> operationTypes,
			byte newLimitQuantity
	) {
		if (originalNecessaries.getLimit() != newLimitQuantity) {
			String description = NecessariesHistory.generateDescriptionWithoutFormatting(
														   NecessariesHistory.CHANGE_LIMIT_QUANTITY
												   )
												   .formatted(originalNecessaries.getLimit(), newLimitQuantity);

			fields.add("limit");
			values.add(newLimitQuantity);
			descriptionList.add(description);
			operationTypes.add(NecessariesHistory.CHANGE_LIMIT_QUANTITY);
		}
	}

	private void addChangedPrice(
			ArrayList<String> fields,
			ArrayList<Object> values,
			ArrayList<String> descriptionList,
			ArrayList<Byte> operationTypes,
			int newPrice
	) {
		if (originalNecessaries.getPrice() != newPrice) {
			String description = NecessariesHistory.generateDescriptionWithoutFormatting(NecessariesHistory.CHANGE_PRICE)
												   .formatted(originalNecessaries.getPrice(), newPrice);

			fields.add("price");
			values.add(newPrice);
			descriptionList.add(description);
			operationTypes.add(NecessariesHistory.CHANGE_PRICE);
		}
	}

	private boolean validateAndAddDateWillChange(
			ArrayList<String> fields,
			ArrayList<Object> values,
			ArrayList<String> descriptionList,
			ArrayList<Byte> operationTypes,
			String newStartDateAsString,
			String newEndDateAsString
	) {
		if (!validateDate(newStartDateAsString, newEndDateAsString))
			return false;

		boolean isChangeDate = false;
		String originalStartDateAsString = UtilityFunctions.formatTimestamp(
				Constants.TIMESTAMP_WITHOUT_NANOSECOND,
				originalNecessaries.getStartDate()
		);
		String originalEndDateAsString = UtilityFunctions.formatTimestamp(
				Constants.TIMESTAMP_WITHOUT_NANOSECOND,
				originalNecessaries.getExpiredDate()
		);

		if (!originalStartDateAsString.equals(newStartDateAsString)) {
			fields.add("startDate");
			values.add(Timestamp.valueOf(newStartDateAsString));
			isChangeDate = true;
		}

		if (!originalEndDateAsString.equals(newEndDateAsString)) {
			fields.add("expiredDate");
			values.add(Timestamp.valueOf(newEndDateAsString));
			isChangeDate = true;
		}

		if (isChangeDate) {
			String descriptionOfChangingDate = NecessariesHistory.generateDescriptionWithoutFormatting(
																		 NecessariesHistory.CHANGE_DATE
																 )
																 .formatted(
																		 originalStartDateAsString,
																		 originalEndDateAsString,
																		 newStartDateAsString,
																		 newEndDateAsString
																 );

			descriptionList.add(descriptionOfChangingDate);
			operationTypes.add(NecessariesHistory.CHANGE_DATE);
		}

		return true;
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);

	}
}
