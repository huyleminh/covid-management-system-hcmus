package com.controllers.user;

import com.dao.NecessariesDAO;
import com.models.Necessaries;
import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.utilities.Pair;
import com.utilities.SingletonDBConnection;
import com.utilities.UtilityFunctions;
import com.views.shared.dialogs.ConnectionErrorDialog;
import com.views.shared.dialogs.FilterNecessariesDialog;
import com.views.shared.dialogs.SortDialog;
import com.views.user.dialogs.InputQuantityDialog;
import com.views.user.panels.NecessariesListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class NecessariesListController implements ActionListener {
	final private NecessariesListPanel necessariesListPanel;
	final private SortDialog sortDialog;
	final private FilterNecessariesDialog filterDialog;
	final private InputQuantityDialog inputQuantityDialog;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private int userId;

	public NecessariesListController(JFrame mainFrame, NecessariesListPanel necessariesListPanel, int userId) {
		this.necessariesListPanel = necessariesListPanel;
		this.sortDialog = new SortDialog(
				mainFrame,
				"Sort Necessaries",
				new String[] { "Necessaries Name", "Limited Quantity", "Start Date", "End Date", "Price" }
		);
		this.filterDialog = new FilterNecessariesDialog(mainFrame, "Filter Necessaries");
		this.inputQuantityDialog = new InputQuantityDialog(mainFrame, 0, 0);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.userId = userId;


		this.connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
			connectionErrorDialog.setExitOnCloseButton(false);
			connectionErrorDialog.setVisible(false);

			SingletonDBConnection.getInstance().connect();
			getAllNecessariesBy("");
		});

		this.necessariesListPanel.getSearchButton().addActionListener(this);
		this.necessariesListPanel.getAddToCartButton().addActionListener(this);
		this.necessariesListPanel.getSortButton().addActionListener(this);
		this.necessariesListPanel.getFilterButton().addActionListener(this);
		this.sortDialog.getSortButton().addActionListener(this);
		this.sortDialog.getCancelButton().addActionListener(this);
		this.filterDialog.getFilterButton().addActionListener(this);
		this.filterDialog.getCancelButton().addActionListener(this);
		this.inputQuantityDialog.getAddButton().addActionListener(this);
		this.inputQuantityDialog.getCancelButton().addActionListener(this);

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
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == necessariesListPanel.getSearchButton()) {
			searchAction();
		} else if (event.getSource() == necessariesListPanel.getAddToCartButton()) {
			addToCartButton();
		} else if (event.getSource() == necessariesListPanel.getSortButton()) {
			sortAction();
		} else if (event.getSource() == necessariesListPanel.getFilterButton()) {
			filterAction();
		} else if (event.getSource() == sortDialog.getSortButton()) {
			sortActionOfSortDialog();
		} else if (event.getSource() == sortDialog.getCancelButton()) {
			sortDialog.setVisible(false);
		} else if (event.getSource() == filterDialog.getFilterButton()) {
			filterActionOfFilterDialog();
		} else if (event.getSource() == filterDialog.getCancelButton()) {
			filterDialog.setVisible(false);
		} else if (event.getSource() == inputQuantityDialog.getAddButton()) {
			addActionOfInputQuantityDialog();
		} else if (event.getSource() == inputQuantityDialog.getCancelButton()) {
			inputQuantityDialog.setVisible(false);
		}
	}

	public void preprocessAndDisplayUI() {
		getAllNecessariesBy("");
	}

	private void searchAction() {
		final String searchValue = UtilityFunctions.removeRedundantWhiteSpace(
				necessariesListPanel.getSearchValueTextField().getText()
		);
		getAllNecessariesBy(searchValue);
	}

	private void addToCartButton() {
		JTable table = necessariesListPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
					necessariesListPanel,
					"Please select a row!",
					"Add To Cart",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			int maxQuantity = (byte) table.getValueAt(selectedRow, 2);

			if (maxQuantity == 0) {
				JOptionPane.showMessageDialog(
						necessariesListPanel,
						"This necessaries is out of stock",
						"Add To Cart",
						JOptionPane.INFORMATION_MESSAGE
				);
			} else {
				inputQuantityDialog.setRange(0, maxQuantity);
				inputQuantityDialog.setVisible(true);
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
		String orderType = sortDialog.isAscendingOrder() ? "ASC" : "DESC";
		String sortOption = String.valueOf(sortDialog.getSortOptions().getSelectedItem());
		String field = convertSortOptionToFieldName(sortOption);

		NecessariesDAO daoModel = new NecessariesDAO();
		ArrayList<Necessaries> necessariesList = (ArrayList<Necessaries>) daoModel.getAllCanPurchaseByUserIdAndSortBy(
				field, orderType, userId
		);

		addNecessariesListIntoTable(necessariesList);
		JOptionPane.showMessageDialog(
				sortDialog,
				"Sort successfully",
				"Sort Necessaries",
				JOptionPane.INFORMATION_MESSAGE
		);
		sortDialog.setVisible(false);
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
					LocalDateTime.of(
							LocalDate.of(
									filterDialog.getStartDateChooser().getSelectedYear(),
									filterDialog.getStartDateChooser().getSelectedMonth(),
									filterDialog.getStartDateChooser().getSelectedDay()
							),
							LocalTime.MIN
					)
			);

			Timestamp endDate = Timestamp.valueOf(
					LocalDateTime.of (
							LocalDate.of(
									filterDialog.getEndDateChooser().getSelectedYear(),
									filterDialog.getEndDateChooser().getSelectedMonth(),
									filterDialog.getEndDateChooser().getSelectedDay()
							),
							LocalTime.MAX
					)
			);

			// The startDate is after the endDate
			if (startDate.compareTo(endDate) > 0) {
				showErrorMessage(filterDialog, "Filter Necessaries", "Invalid start and end date");
				return;
			}

			fields.add("date");
			values.add(new Pair<>(startDate, endDate));
		}

		if (fields.isEmpty()) {
			JOptionPane.showMessageDialog(
					filterDialog,
					"Please select at lease one filter type and enter filter value",
					"Filter Necessaries",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			NecessariesDAO daoModel = new NecessariesDAO();
			ArrayList<Necessaries> necessariesList = (ArrayList<Necessaries>) daoModel.getAllCanPurchaseByUserIdAndFilterBy(
					fields, values, userId
			);

			addNecessariesListIntoTable(necessariesList);
			JOptionPane.showMessageDialog(
					filterDialog,
					"Filter successfully",
					"Filter Necessaries",
					JOptionPane.INFORMATION_MESSAGE
			);
			filterDialog.setVisible(false);
		}
	}

	private void addActionOfInputQuantityDialog() {
		JTable table = necessariesListPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		int quantity = inputQuantityDialog.getQuantity();
		int price = (int) table.getValueAt(selectedRow, 5);
		int totalPrice = quantity * price;

		if (totalPrice /*+ cart.getTotalPrice()*/ > Constants.MAX_DEBT) {
			showErrorMessage(
					inputQuantityDialog,
					"Input Quantity",
					"You can not add these items because you will reach the maximum debt if you add"
			);
		} else {
//			Vector<Object> rowValue = new Vector<>(5);
//			rowValue.add(table.getValueAt(selectedRow, 0));
//			rowValue.add(table.getValueAt(selectedRow, 1));
//			rowValue.add(quantity);
//			rowValue.add(price);
//			rowValue.add(totalPrice);

//			cart.add(rowValue);

			int currentQuantity = (byte) table.getValueAt(selectedRow, 2);
			byte newQuantity = (byte) (currentQuantity - quantity);
			table.setValueAt(newQuantity, selectedRow, 2);

			JOptionPane.showMessageDialog(
					inputQuantityDialog,
					"Add To Cart",
					"Add successfully",
					JOptionPane.INFORMATION_MESSAGE
			);
			inputQuantityDialog.setVisible(false);
		}
	}

	private void getAllNecessariesBy(String necessariesName) {
		NecessariesDAO daoModel = new NecessariesDAO();
		ArrayList<Necessaries> necessariesList = (ArrayList<Necessaries>) daoModel.getAllCanPurchaseByUserIdAndNecessariesName(
				userId, necessariesName
		);

		addNecessariesListIntoTable(necessariesList);
	}

	private void addNecessariesListIntoTable(ArrayList<Necessaries> necessariesList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) necessariesListPanel.getScrollableTable()
																					   .getTableModel();
		tableModel.removeAllRows();

		for (Necessaries necessaries : necessariesList) {
			tableModel.addRow(new Object[]{
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
			});
		}
	}

	private String convertSortOptionToFieldName(String option) {
		String fieldName = "Necessaries Name";

		switch (option) {
			case "Necessaries Name" -> fieldName = "necessariesName";
			case "Limited Quantity" -> fieldName = "limit";
			case "Start Date" 		-> fieldName = "startDate";
			case "End Date" 		-> fieldName = "expiredDate";
			case "Price" 			-> fieldName = "price";
		}

		return fieldName;
	}

	private void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);

	}
}
