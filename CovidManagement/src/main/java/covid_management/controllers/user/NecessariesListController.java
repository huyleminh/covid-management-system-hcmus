package covid_management.controllers.user;

import covid_management.dao.DebtDAO;
import covid_management.dao.NecessariesDAO;
import covid_management.models.Necessaries;
import covid_management.views.shared.dialogs.FilterNecessariesDialog;
import covid_management.views.shared.dialogs.SortDialog;
import covid_management.views.user.dialogs.InputNumberDialog;
import covid_management.views.user.panels.NecessariesListPanel;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;

public class NecessariesListController implements ActionListener {
	final private NecessariesListPanel necessariesListPanel;
	final private SortDialog sortDialog;
	final private FilterNecessariesDialog filterDialog;
	final private InputNumberDialog inputQuantityDialog;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private int userId;

	final private CartController cartController;

	public NecessariesListController(
			JFrame mainFrame,
			NecessariesListPanel necessariesListPanel,
			CartController cartController,
			int userId
	) {
		this.necessariesListPanel = necessariesListPanel;
		this.sortDialog = new SortDialog(
				mainFrame,
				"Sort Necessaries",
				new String[] { "Necessaries Name", "Limited Quantity", "Start Date", "End Date", "Price" }
		);
		this.filterDialog = new FilterNecessariesDialog(mainFrame, "Filter Necessaries");
		this.inputQuantityDialog = new InputNumberDialog(
				mainFrame, "Input Quantity", "Quantity", 0, 0
		);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.cartController = cartController;
		this.userId = userId;


		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
		this.necessariesListPanel.getSearchButton().addActionListener(this);
		this.necessariesListPanel.getAddToCartButton().addActionListener(this);
		this.necessariesListPanel.getSortButton().addActionListener(this);
		this.necessariesListPanel.getFilterButton().addActionListener(this);
		this.sortDialog.getSortButton().addActionListener(this);
		this.sortDialog.getCancelButton().addActionListener(this);
		this.filterDialog.getFilterButton().addActionListener(this);
		this.filterDialog.getCancelButton().addActionListener(this);
		this.inputQuantityDialog.getOkButton().addActionListener(this);
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

	public void preprocess() {
		getAllNecessariesBy("");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectAction();
		} else if (event.getSource() == necessariesListPanel.getSearchButton()) {
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
		} else if (event.getSource() == inputQuantityDialog.getOkButton()) {
			okActionOfInputNumberDialog();
		} else if (event.getSource() == inputQuantityDialog.getCancelButton()) {
			inputQuantityDialog.setVisible(false);
		}
	}

	private void reconnectAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
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
		try {
			String orderType = sortDialog.isAscendingOrder() ? "ASC" : "DESC";
			String sortOption = String.valueOf(sortDialog.getSortOptions().getSelectedItem());
			String field = convertSortOptionToFieldName(sortOption);

			NecessariesDAO daoModel = new NecessariesDAO();
			ArrayList<Necessaries> necessariesList = daoModel.getAllCanPurchaseByUserIdAndSortBy(
					field, orderType, userId
			);

			addNecessariesListIntoTable(necessariesList);
//			JOptionPane.showMessageDialog(
//					sortDialog,
//					"Sort successfully",
//					"Sort Necessaries",
//					JOptionPane.INFORMATION_MESSAGE
//			);
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
				ArrayList<Necessaries> necessariesList = daoModel.getAllCanPurchaseByUserIdAndFilterBy(
						fields, values, userId
				);

				addNecessariesListIntoTable(necessariesList);
//				JOptionPane.showMessageDialog(
//						filterDialog,
//						"Filter successfully",
//						"Filter Necessaries",
//						JOptionPane.INFORMATION_MESSAGE
//				);
				filterDialog.setVisible(false);
			} catch (DBConnectionException e) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				e.printStackTrace();
			}
		}
	}

	private void okActionOfInputNumberDialog() {
		JTable table = necessariesListPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		int quantity = inputQuantityDialog.getNumber();
		int price = (int) table.getValueAt(selectedRow, 5);
		int totalPrice = quantity * price;

		if (quantity == 0) {
			showErrorMessage(
					inputQuantityDialog,
					"Input Quantity",
					"Quantity is invalid"
			);
			return;
		}

		try {
			DebtDAO daoModel = new DebtDAO();
			int totalDebt = daoModel.getTotalDebtByUsedId(userId);

			if (totalPrice + cartController.getTotalAmount() + totalDebt > Constants.MAX_DEBT) {
				showErrorMessage(
						inputQuantityDialog,
						"Input Quantity",
						"You can not add these items because you will reach the maximum debt if you add."
				);
			} else {
				int currentQuantity = (byte) table.getValueAt(selectedRow, 2);

				Vector<Object> item = new Vector<>(5);
				item.add(table.getValueAt(selectedRow, 0));
				item.add((byte) currentQuantity);
				item.add(table.getValueAt(selectedRow, 1));
				item.add((byte) quantity);
				item.add(price);
				item.add(totalPrice);
				cartController.addCartItem(item);

				byte newQuantity = (byte) (currentQuantity - quantity);
				table.setValueAt(newQuantity, selectedRow, 2);
//			if (newQuantity == 0) {
//				NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
//				tableModel.removeRow(selectedRow);
//			} else {
//				table.setValueAt(newQuantity, selectedRow, 2);
//			}

				JOptionPane.showMessageDialog(
						inputQuantityDialog,
						"Add successfully",
						"Add To Cart",
						JOptionPane.INFORMATION_MESSAGE
				);
				inputQuantityDialog.setVisible(false);
			}
		} catch (DBConnectionException e) {
			inputQuantityDialog.setVisible(false);
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void getAllNecessariesBy(String necessariesName) {
		try {
			NecessariesDAO daoModel = new NecessariesDAO();
			ArrayList<Necessaries> necessariesList = daoModel.getAllCanPurchaseByUserIdAndNecessariesName(
					userId, necessariesName
			);

			addNecessariesListIntoTable(necessariesList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private void addNecessariesListIntoTable(ArrayList<Necessaries> necessariesList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) necessariesListPanel.getScrollableTable()
																					   .getTableModel();

		tableModel.removeAllRows();
		for (Necessaries necessaries : necessariesList) {
			int necessariesId = necessaries.getNecessariesId();
			byte limit = necessaries.getLimit();
			int price = necessaries.getPrice();

			if (cartController.getNumberOfItems() != 0) {
				Vector<Object> item = cartController.getCartItem(necessariesId);

				if (item != null) {
					int maxQuantity = (byte) item.get(1);
					int cartItemPrice = (int) item.get(4);

					// If this item has been changed its price, this item will be removed out of the cart.
					if (price != cartItemPrice)
						cartController.removeCartItem(necessariesId);
					else {
						byte newCartItemQuantity = (byte) item.get(3);

						if (limit != maxQuantity) {
							cartController.setMaxQuantityOf(necessariesId, limit);
							newCartItemQuantity = (byte) Math.min(limit, newCartItemQuantity);
						}

						limit -= newCartItemQuantity;  // Subtracts the quantity of this item in the cart.
					}
				}
			}

			tableModel.addRow(new Object[]{
					necessariesId,
					necessaries.getNecessariesName(),
					limit,
					UtilityFunctions.formatTimestamp(
							Constants.TIMESTAMP_WITHOUT_NANOSECOND,
							necessaries.getStartDate()
					),
					UtilityFunctions.formatTimestamp(
							Constants.TIMESTAMP_WITHOUT_NANOSECOND,
							necessaries.getExpiredDate()
					),
					price
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
