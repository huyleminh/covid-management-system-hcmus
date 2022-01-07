package com.controllers.user;

import com.dao.OrderDAO;
import com.models.table.NonEditableTableModel;
import com.utilities.Constants;
import com.utilities.SingletonDBConnection;
import com.utilities.UtilityFunctions;
import com.views.shared.dialogs.ConnectionErrorDialog;
import com.views.user.dialogs.InputNumberDialog;
import com.views.user.panels.CartPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class CartController implements ActionListener {
	final private CartPanel cartPanel;
	final private InputNumberDialog inputQuantityDialog;
	final private ConnectionErrorDialog connectionErrorDialog;
	final private int userId;

	final Vector<Vector<Object>> cartItems;
	private int totalAmount;

	public CartController(JFrame mainFrame, CartPanel cartPanel, int userId) {
		this.cartPanel = cartPanel;
		this.inputQuantityDialog = new InputNumberDialog(
				mainFrame, "Input Quantity", "Quantity", 0, 0
		);
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.userId = userId;
		this.cartItems = new Vector<>();
		this.totalAmount = 0;

		this.connectionErrorDialog.getReconnectButton().addActionListener((event) -> {
			connectionErrorDialog.setExitOnCloseButton(false);
			connectionErrorDialog.setVisible(false);

			SingletonDBConnection.getInstance().connect();
			addCartItemsIntoTable();
		});

		this.cartPanel.getRemoveButton().addActionListener(this);
		this.cartPanel.getChangeQuantityButton().addActionListener(this);
		this.cartPanel.getCheckoutButton().addActionListener(this);
		this.inputQuantityDialog.getOkButton().addActionListener(this);
		this.inputQuantityDialog.getCancelButton().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == cartPanel.getRemoveButton()) {
			removeAction();
		} else if (event.getSource() == cartPanel.getChangeQuantityButton()) {
			changeQuantityAction();
		} else if (event.getSource() == cartPanel.getCheckoutButton()) {
			checkoutAction();
		} else if (event.getSource() == inputQuantityDialog.getOkButton()) {
			okActionOfInputNumberDialog();
		} else if (event.getSource() == inputQuantityDialog.getCancelButton()) {
			inputQuantityDialog.setVisible(false);
		}
	}

	public void preprocess() {
		addCartItemsIntoTable();
	}

	private void removeAction() {
		JTable table = cartPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
					cartPanel,
					"Please select a row!",
					"Remove",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();

			totalAmount -= (int) cartItems.get(selectedRow).get(5);
			cartPanel.getTotalAmountValueLabel().setText(UtilityFunctions.formatMoneyVND(totalAmount) + " VND");
			tableModel.removeRow(selectedRow);
			cartItems.removeElementAt(selectedRow);

			JOptionPane.showMessageDialog(
					cartPanel,
					"Remove successfully",
					"Remove",
					JOptionPane.INFORMATION_MESSAGE
			);
		}
	}

	private void changeQuantityAction() {
		JTable table = cartPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
					cartPanel,
					"Please select a row!",
					"Change Quantity",
					JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			int currentQuantity = (byte) table.getValueAt(selectedRow, 3);
			int remainingTotalAmountCanBuy = Constants.MAX_DEBT - cartPanel.getTotalAmountValue();
			int price = (int) table.getValueAt(selectedRow, 4);
			int remainingQuantityCanBuy = remainingTotalAmountCanBuy / price;

			inputQuantityDialog.setRange(0, currentQuantity + remainingQuantityCanBuy);
			inputQuantityDialog.setVisible(true);
		}
	}

	private void checkoutAction() {
		if (cartItems.isEmpty()) {
			JOptionPane.showMessageDialog(
					cartPanel,
					"Your cart is empty",
					"Checkout",
					JOptionPane.INFORMATION_MESSAGE
			);
			return;
		}

		int option = JOptionPane.showConfirmDialog(
				cartPanel,
				"Are you sure to buy these items?",
				"Checkout",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION) {
			ArrayList<Integer> necessariesIdList = new ArrayList<>();
			ArrayList<String> necessariesNameList = new ArrayList<>();
			ArrayList<Integer> priceList = new ArrayList<>();
			ArrayList<Byte> quantityList = new ArrayList<>();
			int totalPrice = cartPanel.getTotalAmountValue();

			preprocessOfInsertingOrder(necessariesIdList, necessariesNameList, priceList, quantityList);

			OrderDAO daoModel = new OrderDAO();
			boolean isInserted = daoModel.create(
					userId,
					necessariesIdList,
					necessariesNameList,
					priceList,
					quantityList,
					totalPrice
			);

			if (!isInserted)
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			else {
				JOptionPane.showMessageDialog(
						cartPanel,
						"Checkout successfully",
						"Checkout",
						JOptionPane.INFORMATION_MESSAGE
				);

				cartItems.clear();
				((NonEditableTableModel) cartPanel.getScrollableTable().getTableModel()).removeAllRows();
				cartPanel.getTotalAmountValueLabel().setText("0 VND");
			}
		}
	}

	private void okActionOfInputNumberDialog() {
		JTable table = cartPanel.getScrollableTable().getTable();
		NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
		int selectedRow = table.getSelectedRow();

		int oldQuantity = (byte) table.getValueAt(selectedRow, 3);
		int newQuantity = inputQuantityDialog.getNumber();

		if (oldQuantity == newQuantity) {
			int option = JOptionPane.showConfirmDialog(
					inputQuantityDialog,
					"Information does not change. Are you sure to close?",
					"Edit User",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION)
				inputQuantityDialog.setVisible(false);

			return;
		} else if (newQuantity == 0) {
			totalAmount -= (int) cartItems.get(selectedRow).get(5);
			cartPanel.getTotalAmountValueLabel().setText(UtilityFunctions.formatMoneyVND(totalAmount) + " VND");
			tableModel.removeRow(selectedRow);
			cartItems.removeElementAt(selectedRow);
		} else {
			int totalPrice = (int) table.getValueAt(selectedRow, 5);
			int price = (int) table.getValueAt(selectedRow, 4);

			totalPrice += price * (newQuantity - oldQuantity);
			totalAmount += price * (newQuantity - oldQuantity);

			cartItems.get(selectedRow).setElementAt((byte) newQuantity, 3);
			cartItems.get(selectedRow).setElementAt(totalPrice, 5);
			table.setValueAt((byte) newQuantity, selectedRow, 3);
			table.setValueAt(totalPrice, selectedRow, 5);
			cartPanel.getTotalAmountValueLabel().setText(UtilityFunctions.formatMoneyVND(totalAmount) + " VND");
		}

		JOptionPane.showMessageDialog(
				inputQuantityDialog,
				"Change successfully",
				"Change Quantity",
				JOptionPane.INFORMATION_MESSAGE
		);

		inputQuantityDialog.setVisible(false);
	}

	private void addCartItemsIntoTable() {
		NonEditableTableModel tableModel = (NonEditableTableModel) cartPanel.getScrollableTable().getTableModel();

		tableModel.removeAllRows();
		totalAmount = 0;

		cartItems.forEach(item ->  {
			tableModel.addRow(item);
			totalAmount += (int) item.get(5);
		});

		cartPanel.getTotalAmountValueLabel().setText(UtilityFunctions.formatMoneyVND(totalAmount) + " VND");
	}

	private void preprocessOfInsertingOrder(
			ArrayList<Integer> necessariesIdList,
			ArrayList<String> necessariesNameList,
			ArrayList<Integer> priceList,
			ArrayList<Byte> quantityList
	) {
		cartItems.forEach(item -> {
			necessariesIdList.add((Integer) item.get(0));
			necessariesNameList.add(String.valueOf(item.get(2)));
			priceList.add((Integer) item.get(4));
			quantityList.add((Byte) item.get(3));
		});
	}

	// The functions below will be used to add an item into the cart at the NecessariesListPanel tab.
	public int getTotalAmount() {
		return totalAmount;
	}

	public int getNumberOfItems() {
		return cartItems.size();
	}

	public Vector<Object> getCartItem(int necessariesId) {
		int index = findCartItem(necessariesId);
		return index == -1 ? null : cartItems.get(index);
	}

	public void setMaxQuantityOf(int necessariesId, int newMaxQuantity) {
		int index = findCartItem(necessariesId);
		if (index != -1) {
			byte newQuantity = (byte) Math.min(newMaxQuantity, (byte) cartItems.get(index).get(3));

			cartItems.get(index).setElementAt(newQuantity, 3);
			cartItems.get(index).setElementAt(newMaxQuantity, 1);
		}
	}

	public void addCartItem(Vector<Object> item) {
		int necessariesId = (int) item.get(0);
		int index = findCartItem(necessariesId);

		if (index == -1) {
			cartItems.add(item);
		} else {
			byte newQuantity = (byte) ((byte) cartItems.get(index).get(3) + (byte) item.get(3));
			int newTotalPrice = (int) cartItems.get(index).get(5) + (int) item.get(5);

			cartItems.get(index).setElementAt(newQuantity, 3);
			cartItems.get(index).setElementAt(newTotalPrice, 5);
		}
	}

	public void removeCartItem(int necessariesId) {
		int index = findCartItem(necessariesId);
		if (index != -1)
			cartItems.removeElementAt(index);
	}

	private int findCartItem(int necessariesId) {
		for (int i = 0; i < cartItems.size(); i++) {
			if ((int) cartItems.get(i).get(0) == necessariesId)
				return i;
		}

		return -1;
	}
}
