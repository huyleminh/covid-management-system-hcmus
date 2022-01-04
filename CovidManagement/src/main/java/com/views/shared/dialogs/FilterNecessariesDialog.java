package com.views.shared.dialogs;

import com.utilities.Constants;
import com.views.shared.panels.DateChooserPanel;
import com.views.shared.panels.NumberFieldWithButton;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class FilterNecessariesDialog extends JDialog {
	// Constants
	private static final int HORIZONTAL_PADDING = 25;
	private static final int TITLED_BORDER_WIDTH = 350;
	private static final int TITLED_BORDER_HEIGHT = 60;

	// Input formatters
	private NumberFormatter priceFormatter;

	// Components
	private JRadioButton quantityEditableButton;
	private JRadioButton priceEditableButton;
	private JRadioButton dateEditableButton;
	private NumberFieldWithButton minQuantityField;
	private NumberFieldWithButton maxQuantityField;
	private JFormattedTextField minPriceTextField;
	private JFormattedTextField maxPriceTextField;
	private DateChooserPanel startDateChooser;
	private DateChooserPanel endDateChooser;

	private JButton cancelButton;
	private JButton filterButton;

	public FilterNecessariesDialog(JFrame frame, String title) {
		super(frame, title, true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(400, 375));

		initInputFormatters();
		initComponents(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		Border grayLineBorder = BorderFactory.createLineBorder(Color.GRAY, 1);

		initFilterTypePanel(panel);
		initQuantityPanel(panel, grayLineBorder);
		initPricePanel(panel, grayLineBorder);
		initStartDatePanel(panel, grayLineBorder);
		initEndDatePanel(panel, grayLineBorder);
		initButtons(panel);

		setEnabledQuantityPanel(false);
		setEnabledPricePanel(false);
		setEnabledDatePanel(false);
	}

	private void initInputFormatters() {
		// Number formatter without grouping separator
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);

		// Price formatter
		priceFormatter = new NumberFormatter(numberFormat);
		priceFormatter.setMinimum(0);
		priceFormatter.setMaximum(8388607);
		priceFormatter.setAllowsInvalid(false);
		priceFormatter.setCommitsOnValidEdit(true);
	}

	private void initFilterTypePanel(JPanel panel) {
		JLabel filterTypeLabel = new JLabel("Filter type:");
		filterTypeLabel.setBounds(HORIZONTAL_PADDING, 15, 100, 30);
		panel.add(filterTypeLabel);

		quantityEditableButton = new JRadioButton("Quantity", false);
		quantityEditableButton.setBounds(120, 15, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		panel.add(quantityEditableButton);

		priceEditableButton = new JRadioButton("Price", false);
		priceEditableButton.setBounds(225, 15, Constants.BUTTON_SMALL_WIDTH - 15, Constants.BUTTON_HEIGHT);
		panel.add(priceEditableButton);

		dateEditableButton = new JRadioButton("Date", false);
		dateEditableButton.setBounds(300, 15, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		panel.add(dateEditableButton);

	}

	private void initQuantityPanel(JPanel panel, Border lineBorder) {
		JPanel quantityPanel = new JPanel();
		quantityPanel.setLayout(null);
		quantityPanel.setBounds(HORIZONTAL_PADDING, 55, TITLED_BORDER_WIDTH, TITLED_BORDER_HEIGHT);
		quantityPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Quantity"));
		panel.add(quantityPanel);

		JLabel minQuantityLabel = new JLabel("Min");
		minQuantityLabel.setBounds(10, 20, 30, Constants.TEXT_HEIGHT);
		quantityPanel.add(minQuantityLabel);

		minQuantityField = new NumberFieldWithButton(0, 127, 120);
		minQuantityField.setBounds(45, 20, 120, Constants.TEXT_HEIGHT);
		quantityPanel.add(minQuantityField);

		JLabel maxQuantityLabel = new JLabel("Max");
		maxQuantityLabel.setBounds(185, 20, 30, Constants.TEXT_HEIGHT);
		quantityPanel.add(maxQuantityLabel);

		maxQuantityField = new NumberFieldWithButton(0, 127, 120);
		maxQuantityField.setBounds(220, 20, 120, Constants.TEXT_HEIGHT);
		quantityPanel.add(maxQuantityField);
	}

	private void initPricePanel(JPanel panel, Border lineBorder) {
		JPanel pricePanel = new JPanel();
		pricePanel.setLayout(null);
		pricePanel.setBounds(HORIZONTAL_PADDING, 125, TITLED_BORDER_WIDTH, TITLED_BORDER_HEIGHT);
		pricePanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Price with VND currency"));
		panel.add(pricePanel);

		JLabel minPriceLabel = new JLabel("Min");
		minPriceLabel.setBounds(10, 20, 30, Constants.TEXT_HEIGHT);
		pricePanel.add(minPriceLabel);

		minPriceTextField = new JFormattedTextField(priceFormatter);
		minPriceTextField.setBounds(45, 20, 120, Constants.TEXT_HEIGHT);
		minPriceTextField.setValue(0);
		minPriceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		pricePanel.add(minPriceTextField);

		JLabel maxPriceLabel = new JLabel("Max");
		maxPriceLabel.setBounds(185, 20, 30, Constants.TEXT_HEIGHT);
		pricePanel.add(maxPriceLabel);

		maxPriceTextField = new JFormattedTextField(priceFormatter);
		maxPriceTextField.setBounds(220, 20, 120, Constants.TEXT_HEIGHT);
		maxPriceTextField.setValue(0);
		maxPriceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		pricePanel.add(maxPriceTextField);
	}

	private void initStartDatePanel(JPanel panel, Border lineBorder) {
		JPanel startDatePanel = new JPanel();
		startDatePanel.setLayout(null);
		startDatePanel.setBounds(HORIZONTAL_PADDING, 195, TITLED_BORDER_WIDTH, TITLED_BORDER_HEIGHT);
		startDatePanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Start date"));
		panel.add(startDatePanel);

		startDateChooser = new DateChooserPanel((short) 1930, (short) 2021);
		startDateChooser.setBounds(10, 20, 330, 30);
		startDatePanel.add(startDateChooser);
	}

	private void initEndDatePanel(JPanel panel, Border lineBorder) {
		JPanel endDatePanel = new JPanel();
		endDatePanel.setLayout(null);
		endDatePanel.setBounds(HORIZONTAL_PADDING, 265, TITLED_BORDER_WIDTH, TITLED_BORDER_HEIGHT);
		endDatePanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "End date"));
		panel.add(endDatePanel);

		endDateChooser = new DateChooserPanel((short) 1930, (short) 2022);
		endDateChooser.setBounds(10, 20, 330, 30);
		endDatePanel.add(endDateChooser);
	}

	private void initButtons(JPanel panel) {
		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(115, 335, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Filter button
		filterButton = new JButton("Filter");
		filterButton.setBounds(205, 335, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		filterButton.setHorizontalTextPosition(JButton.CENTER);
		filterButton.setBackground(Constants.LIGHT_BLUE);
		filterButton.setForeground(Color.WHITE);
		panel.add(filterButton);
	}

	public JRadioButton getQuantityEditableButton() {
		return quantityEditableButton;
	}

	public JRadioButton getPriceEditableButton() {
		return priceEditableButton;
	}

	public JRadioButton getDateEditableButton() {
		return dateEditableButton;
	}

	public NumberFieldWithButton getMinQuantityField() {
		return minQuantityField;
	}

	public NumberFieldWithButton getMaxQuantityField() {
		return maxQuantityField;
	}

	public JFormattedTextField getMinPriceTextField() {
		return minPriceTextField;
	}

	public JFormattedTextField getMaxPriceTextField() {
		return maxPriceTextField;
	}

	public DateChooserPanel getStartDateChooser() {
		return startDateChooser;
	}

	public DateChooserPanel getEndDateChooser() {
		return endDateChooser;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getFilterButton() {
		return filterButton;
	}

	public void setEnabledQuantityPanel(boolean enabled) {
		minQuantityField.setEnabledNumberFieldAndButtons(enabled);
		maxQuantityField.setEnabledNumberFieldAndButtons(enabled);
	}

	public void setEnabledPricePanel(boolean enabled) {
		minPriceTextField.setEnabled(enabled);
		maxPriceTextField.setEnabled(enabled);
	}

	public void setEnabledDatePanel(boolean enabled) {
		startDateChooser.setEnabledSelection(enabled);
		endDateChooser.setEnabledSelection(enabled);
	}
}
