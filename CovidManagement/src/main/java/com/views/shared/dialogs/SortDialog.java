package com.views.shared.dialogs;

import com.utilities.Constants;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SortDialog extends JDialog {
	// Constants
	public static final byte ASCENDING_ORDER = 0;
	public static final byte DESCENDING_ORDER = 1;

	// Components
	private JRadioButton ascendingOrderButton;
	private JRadioButton descendingOrderButton;
	private JComboBox<String> sortOptions;
	private JButton cancelButton;
	private JButton sortButton;

	public SortDialog(JFrame frame, String title) {
		super(frame, title, true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(400, 190));

		initComponents(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	public SortDialog(JFrame frame, String title, String[] optionNames) {
		super(frame);
		setTitle(title);

		// Panel
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(400, 190));

		initComponents(panel);
		setOptionNames(optionNames);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
		setModal(true);
	}

	private void initComponents(JPanel panel) {
		initOrderTypePanel(panel);
		initSortByPanel(panel);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(115, 150, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Sort button
		sortButton = new JButton("Sort");
		sortButton.setBounds(205, 150, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		sortButton.setHorizontalTextPosition(JButton.CENTER);
		sortButton.setBackground(Constants.LIGHT_BLUE);
		sortButton.setForeground(Color.WHITE);
		panel.add(sortButton);
	}

	private void initOrderTypePanel(JPanel panel) {
		JPanel orderTypePanel = new JPanel();
		orderTypePanel.setLayout(null);
		orderTypePanel.setBounds(25, 10, 350, 60);
		orderTypePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
				"Order Type",
				TitledBorder.LEFT,
				TitledBorder.TOP
		));
		panel.add(orderTypePanel);

		// Ascending order button
		ascendingOrderButton = new JRadioButton("Ascending");
		ascendingOrderButton.setBounds(25, 20, Constants.BUTTON_LARGE_WIDTH, Constants.BUTTON_HEIGHT);
		ascendingOrderButton.setSelected(true);
		orderTypePanel.add(ascendingOrderButton);

		// Descending order button
		descendingOrderButton = new JRadioButton("Descending");
		descendingOrderButton.setBounds(200, 20, Constants.BUTTON_LARGE_WIDTH, Constants.BUTTON_HEIGHT);
		orderTypePanel.add(descendingOrderButton);

		// Button group
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(ascendingOrderButton);
		buttonGroup.add(descendingOrderButton);
	}

	private void initSortByPanel(JPanel panel) {
		JPanel sortByPanel = new JPanel();
		sortByPanel.setLayout(null);
		sortByPanel.setBounds(25, 80, 350, 60);
		sortByPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
				"Sort By",
				TitledBorder.LEFT,
				TitledBorder.TOP
		));
		panel.add(sortByPanel);

		// Sort options
		sortOptions = new JComboBox<>();
		sortOptions.setBounds(10, 20, 330, 30);
		sortByPanel.add(sortOptions);
	}

	public boolean isAscendingOrder() {
		return ascendingOrderButton.isSelected();
	}

	public void setOrderType(byte orderType) {
		if (orderType == ASCENDING_ORDER)
			ascendingOrderButton.setSelected(true);
		else
			descendingOrderButton.setSelected(true);
	}

	public void setOptionNames(String[] optionNames) {
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(optionNames);
		sortOptions.setModel(comboBoxModel);
	}

	public JComboBox<String> getSortOptions() {
		return sortOptions;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getSortButton() {
		return sortButton;
	}
}
