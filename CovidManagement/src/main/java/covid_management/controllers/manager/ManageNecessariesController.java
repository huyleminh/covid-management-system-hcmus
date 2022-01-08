package covid_management.controllers.manager;

import covid_management.dao.NecessariesDAO;
import covid_management.models.Necessaries;
import covid_management.models.NecessariesHistory;
import covid_management.views.manager.dialogs.CreateNecessariesDialog;
import covid_management.views.manager.dialogs.EditNecessariesDialog;
import covid_management.views.manager.panels.ManageNecessariesPanel;
import covid_management.views.shared.dialogs.FilterNecessariesDialog;
import covid_management.views.shared.dialogs.SortDialog;
import shared.components.NonEditableTableModel;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.Constants;
import shared.utilities.Pair;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ManageNecessariesController implements ActionListener {
	final private ManageNecessariesPanel manageNecessariesPanel;
	final private EditNecessariesDialog editNecessariesDialog;
	final private SortDialog sortDialog;
	final private FilterNecessariesDialog filterDialog;
	final private CreateNecessariesDialog createNecessariesDialog;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private EditNecessariesController editNecessariesController;
	final private CreateNecessariesController createNecessariesController;
	final private String managerUsername;

	public ManageNecessariesController(
			JFrame mainFrame,
			ManageNecessariesPanel manageNecessariesPanel,
			String username
	) {
		// Initialization
		this.manageNecessariesPanel = manageNecessariesPanel;
		this.editNecessariesDialog = new EditNecessariesDialog(mainFrame);
		this.sortDialog = new SortDialog(
				mainFrame,
				"Sort Necessaries",
				new String[] { "Necessaries Name", "Quantity", "Start Date", "End Date", "Price" }
		);
		this.filterDialog = new FilterNecessariesDialog(mainFrame, "Filter Necessaries");
		this.createNecessariesDialog = new CreateNecessariesDialog(mainFrame);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.editNecessariesController = new EditNecessariesController(
				editNecessariesDialog,
				connectionErrorDialog,
				username
		);
		this.createNecessariesController = new CreateNecessariesController(
				createNecessariesDialog,
				connectionErrorDialog,
				username
		);
		this.managerUsername = username;

		// Add actions listeners
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
		this.manageNecessariesPanel.getSearchButton().addActionListener(this);
		this.manageNecessariesPanel.getEditButton().addActionListener(this);
		this.manageNecessariesPanel.getRemoveButton().addActionListener(this);
		this.manageNecessariesPanel.getSortButton().addActionListener(this);
		this.manageNecessariesPanel.getFilterButton().addActionListener(this);
		this.manageNecessariesPanel.getCreateButton().addActionListener(this);
		this.sortDialog.getSortButton().addActionListener(this);
		this.sortDialog.getCancelButton().addActionListener(this);
		this.filterDialog.getFilterButton().addActionListener(this);
		this.filterDialog.getCancelButton().addActionListener(this);

		// Add item listeners
		this.filterDialog.getQuantityEditableButton().addItemListener((event) -> {
			boolean isSelected = filterDialog.getQuantityEditableButton().isSelected();
			filterDialog.setEnabledQuantityPanel(isSelected);
		});
		this.filterDialog.getPriceEditableButton().addItemListener((event) -> {
			boolean isSelected = filterDialog.getPriceEditableButton().isSelected();
			filterDialog.setEnabledPricePanel(isSelected);
		});
		this.filterDialog.getDateEditableButton().addItemListener((event) -> {
			boolean isSelected = filterDialog.getDateEditableButton().isSelected();
			filterDialog.setEnabledDatePanel(isSelected);
		});

		// Add component listeners
		editNecessariesDialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (editNecessariesController.isSuccess())
					loadNecessariesList();
			}
		});
		createNecessariesDialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (createNecessariesController.isSuccess())
					loadNecessariesList();
			}
		});
	}

	public void preprocessAndDisplayUI() {
		loadNecessariesList();
		manageNecessariesPanel.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectAction();
		} else if (event.getSource() == manageNecessariesPanel.getSearchButton()) {
			searchAction();
		} else if (event.getSource() == manageNecessariesPanel.getEditButton()) {
			editAction();
		} else if (event.getSource() == manageNecessariesPanel.getRemoveButton()) {
			removeAction();
		} else if (event.getSource() == manageNecessariesPanel.getSortButton()) {
			sortAction();
		} else if (event.getSource() == manageNecessariesPanel.getFilterButton()) {
			filterAction();
		} else if (event.getSource() == manageNecessariesPanel.getCreateButton()) {
			createNecessariesController.preprocessAndDisplayUI();
		} else if (event.getSource() == sortDialog.getSortButton()) {
			sortActionOfSortDialog();
		} else if (event.getSource() == sortDialog.getCancelButton()) {
			sortDialog.setVisible(false);
		} else if (event.getSource() == filterDialog.getFilterButton()) {
			filterActionOfFilterDialog();
		} else if (event.getSource() == filterDialog.getCancelButton()) {
			filterDialog.setVisible(false);
		}
	}

	private void reconnectAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadNecessariesList();
	}

	private void searchAction() {
		// Get search value.
		final String searchValue = UtilityFunctions.removeRedundantWhiteSpace(
				manageNecessariesPanel.getSearchValueTextField().getText()
		);

		if (searchValue.isEmpty())
			loadNecessariesList();
		else {
			try {
				NecessariesDAO daoModel = new NecessariesDAO();
				ArrayList<Necessaries> necessariesList = daoModel.searchByNecessariesName(searchValue);

				addNecessariesListIntoTable(necessariesList);
			} catch (DBConnectionException e) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				e.printStackTrace();
			}
		}
	}

	private void editAction() {
		JTable table = manageNecessariesPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
					manageNecessariesPanel,
					"Please select a row!",
					"Remove Necessaries",
					JOptionPane.INFORMATION_MESSAGE
			);
			return;
		}

		// Get original values
		Necessaries originalNecessaries = new Necessaries(
				(int) table.getValueAt(selectedRow, 0),
				String.valueOf(table.getValueAt(selectedRow, 1)),
				(byte) table.getValueAt(selectedRow, 2),
				Timestamp.valueOf(String.valueOf(table.getValueAt(selectedRow, 3))),
				Timestamp.valueOf(String.valueOf(table.getValueAt(selectedRow, 4))),
				(int) table.getValueAt(selectedRow, 5)
		);

		editNecessariesController.preprocessAndDisplayUI(originalNecessaries);
	}

	private void removeAction() {
		JTable table = manageNecessariesPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
					manageNecessariesPanel,
					"Please select a row!",
					"Remove Necessaries",
					JOptionPane.INFORMATION_MESSAGE
			);
			return;
		}

		int option = JOptionPane.showConfirmDialog(
				manageNecessariesPanel,
				"Are you sure to delete this necessaries",
				"Remove Necessaries",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION) {
			try {
				int necessariesId = (int) table.getValueAt(selectedRow, 0);
				String necessariesName = String.valueOf(table.getValueAt(selectedRow, 1));
				String description = NecessariesHistory.generateDescriptionWithoutFormatting(
															   NecessariesHistory.REMOVE_NECESSARIES
													   )
													   .formatted(necessariesName);

				NecessariesDAO daoModel = new NecessariesDAO();
				daoModel.delete(necessariesId, managerUsername, description);

				JOptionPane.showMessageDialog(
						manageNecessariesPanel,
						"Remove successfully",
						"Remove Necessaries",
						JOptionPane.INFORMATION_MESSAGE
				);

				loadNecessariesList();
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

	private void filterAction() {
		filterDialog.getQuantityEditableButton().setSelected(false);
		filterDialog.getPriceEditableButton().setSelected(false);
		filterDialog.getDateEditableButton().setSelected(false);
		filterDialog.getMinQuantityField().setValue(0);
		filterDialog.getMaxQuantityField().setValue(0);
		filterDialog.getMinPriceTextField().setValue(0);
		filterDialog.getMaxPriceTextField().setValue(0);
		filterDialog.getStartDateChooser().makeDefaultSelectedItem();
		filterDialog.getEndDateChooser().makeDefaultSelectedItem();
		filterDialog.setVisible(true);
	}

	private void sortActionOfSortDialog() {
		try {
			// Get selected options.
			boolean isAscending = sortDialog.isAscendingOrder();
			String sortOption = String.valueOf(sortDialog.getSortOptions().getSelectedItem());

			// Convert sort option to field name (table's column name in the database)
			String fieldName = getFieldNameBySortOption(sortOption);

			// Get sorted list of users
			NecessariesDAO daoModel = new NecessariesDAO();
			ArrayList<Necessaries> necessariesList = daoModel.sortBy(fieldName, isAscending);

			addNecessariesListIntoTable(necessariesList);

			JOptionPane.showMessageDialog(
					sortDialog,
					"Sort successfully",
					"Sort Necessaries",
					JOptionPane.INFORMATION_MESSAGE
			);

			sortDialog.setVisible(false);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void filterActionOfFilterDialog() {
		ArrayList<String> fields = new ArrayList<>();
		ArrayList<Pair<Object, Object>> values = new ArrayList<>();

		if (filterDialog.getQuantityEditableButton().isSelected()) {
			byte minQuantity = (byte) filterDialog.getMinQuantityField().getValue();
			byte maxQuantity = (byte) filterDialog.getMaxQuantityField().getValue();

			if ((minQuantity > maxQuantity) || (minQuantity == 0 && maxQuantity == 0)) {
				showErrorMessage(filterDialog, "Filter Necessaries", "Invalid min and max quantity");
				return;
			}

			fields.add("limit");
			values.add(new Pair<>(minQuantity, maxQuantity));
		}

		if (filterDialog.getPriceEditableButton().isSelected()) {
			int minPrice = (int) filterDialog.getMinPriceTextField().getValue();
			int maxPrice = (int) filterDialog.getMaxPriceTextField().getValue();

			if ((minPrice > maxPrice) || (minPrice == 0 && maxPrice == 0)) {
				showErrorMessage(filterDialog, "Filter Necessaries", "Invalid min and max price");
				return;
			}

			fields.add("price");
			values.add(new Pair<>(minPrice, maxPrice));
		}

		if (filterDialog.getDateEditableButton().isSelected()) {
			Timestamp startDate = Timestamp.valueOf(
					"%04d-%02d-%02d 00:00:00".formatted(
							filterDialog.getStartDateChooser().getSelectedYear(),
							filterDialog.getStartDateChooser().getSelectedMonth(),
							filterDialog.getStartDateChooser().getSelectedDay()
					)
			);
			Timestamp expiredDate = Timestamp.valueOf(
					"%04d-%02d-%02d 23:59:59".formatted(
							filterDialog.getEndDateChooser().getSelectedYear(),
							filterDialog.getEndDateChooser().getSelectedMonth(),
							filterDialog.getEndDateChooser().getSelectedDay()
					)
			);

			// The startDate is after the endDate
			if (startDate.compareTo(expiredDate) > 0) {
				showErrorMessage(filterDialog, "Filter Necessaries", "Invalid start and end date");
				return;
			}

			fields.add("date");
			values.add(new Pair<>(startDate, expiredDate));
		}

		if (fields.isEmpty()) {
			JOptionPane.showMessageDialog(
					filterDialog,
					"Please select at lease one filter type and enter filter value",
					"Filter Necessaries",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			try {
				NecessariesDAO daoModel = new NecessariesDAO();
				ArrayList<Necessaries> necessariesList = daoModel.filterBy(fields, values);

				addNecessariesListIntoTable(necessariesList);
				JOptionPane.showMessageDialog(
						filterDialog,
						"Filter successfully",
						"Filter Necessaries",
						JOptionPane.INFORMATION_MESSAGE
				);

				filterDialog.setVisible(false);
			} catch (DBConnectionException e) {
				filterDialog.setVisible(false);
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				e.printStackTrace();
			}
		}
	}

	private void loadNecessariesList() {
		try {
			// Load all necessaries from the database.
			NecessariesDAO daoModel = new NecessariesDAO();
			ArrayList<Necessaries> necessariesList = daoModel.getAll();

			addNecessariesListIntoTable(necessariesList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void addNecessariesListIntoTable(ArrayList<Necessaries> necessariesList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) manageNecessariesPanel.getScrollableTable()
																						 .getTableModel();

		tableModel.removeAllRows();
		for (Necessaries necessaries : necessariesList) {
			tableModel.addRow(
					new Object[] {
							necessaries.getNecessariesId(),
							necessaries.getNecessariesName(),
							necessaries.getLimit(),
							UtilityFunctions.formatTimestamp(
									Constants.TIMESTAMP_WITHOUT_NANOSECOND,
									necessaries.getStartDate()
							),
							UtilityFunctions.formatTimestamp(
									Constants.TIMESTAMP_WITHOUT_NANOSECOND,
									necessaries.getExpiredDate()
							),
							necessaries.getPrice()
					}
			);
		}
	}

	private String getFieldNameBySortOption(String sortOption) {
		String field = "";

		switch (sortOption) {
			case "Necessaries Name" -> field = "necessariesName";
			case "Quantity" -> field = "limit";
			case "Start Date" -> field = "startDate";
			case "End Date" -> field = "expiredDate";
			case "Price" -> field = "price";
		}

		return field;
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
