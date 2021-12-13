package com.views.shared.dialogs;

import com.toedter.calendar.JDateChooser;
import com.utilities.Constants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.TimeZone;

public class FilterNecessariesDialog extends JDialog {
	// Constant
	private static final ImageIcon CALENDAR_ICON = new ImageIcon(Constants.CALENDAR_ICON_FILE_PATH);

	// Input formatters
	private NumberFormatter quantityFormatter;
	private NumberFormatter priceFormatter;
	private DateFormatter dateFormatter;

	// Components
	private JFormattedTextField minQuantityTextField;
	private JFormattedTextField maxQuantityTextField;
	private JFormattedTextField minPriceTextField;
	private JFormattedTextField maxPriceTextField;
	private JFormattedTextField startDateTextField;
	private JFormattedTextField endDateTextField;
	private JButton cancelButton;
	private JButton filterButton;

	public FilterNecessariesDialog(JFrame frame, String title) {
		super(frame, title, true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(500, 220));

		initInputFormatters();
		initComponents(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);

		initQuantityPanel(panel, lineBorder);
		initPricePanel(panel, lineBorder);
		initStartDatePanel(panel, lineBorder);
		initEndDatePanel(panel, lineBorder);
		initButtons(panel);
	}

	private void initInputFormatters() {
		// Number formatter without grouping separator
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);

		// Quantity formatter
		quantityFormatter = new NumberFormatter(numberFormat);
		quantityFormatter.setMinimum(0);
		quantityFormatter.setMaximum(127);
		quantityFormatter.setAllowsInvalid(false);
		quantityFormatter.setCommitsOnValidEdit(true);

		// Price formatter
		priceFormatter = new NumberFormatter(numberFormat);
		priceFormatter.setMinimum(0);
		priceFormatter.setMaximum(8388607);
		priceFormatter.setAllowsInvalid(false);
		priceFormatter.setCommitsOnValidEdit(true);

		// Date format in GMT+7
		DateFormat dateFormat = DateFormat.getDateInstance();
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));

		// Date formatter
		dateFormatter = new DateFormatter(dateFormat);
		dateFormatter.setAllowsInvalid(false);
		dateFormatter.setCommitsOnValidEdit(true);
	}

	private void initQuantityPanel(JPanel panel, Border lineBorder) {
		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setBounds(25, 20, 80, 30);
		panel.add(quantityLabel);

		JLabel minQuantityLabel = new JLabel("Min");
		minQuantityLabel.setBounds(115, 20, 30, 30);
		panel.add(minQuantityLabel);

		minQuantityTextField = new JFormattedTextField(quantityFormatter);
		minQuantityTextField.setBounds(150, 20, 135, 30);
		minQuantityTextField.setValue(0);
		minQuantityTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(minQuantityTextField);

		JLabel maxQuantityLabel = new JLabel("Max");
		maxQuantityLabel.setBounds(305, 20, 30, 30);
		panel.add(maxQuantityLabel);

		maxQuantityTextField = new JFormattedTextField(quantityFormatter);
		maxQuantityTextField.setBounds(340, 20, 135, 30);
		maxQuantityTextField.setValue(0);
		maxQuantityTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(maxQuantityTextField);
	}

	private void initPricePanel(JPanel panel, Border lineBorder) {
		JLabel priceLabel = new JLabel("Price (VND)");
		priceLabel.setBounds(25, 60, 80, 30);
		panel.add(priceLabel);

		JLabel minPriceLabel = new JLabel("Min");
		minPriceLabel.setBounds(115, 60, 30, 30);
		panel.add(minPriceLabel);

		minPriceTextField = new JFormattedTextField(priceFormatter);
		minPriceTextField.setBounds(150, 60, 135, 30);
		minPriceTextField.setValue(0);
		minPriceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(minPriceTextField);

		JLabel maxPriceLabel = new JLabel("Max");
		maxPriceLabel.setBounds(305, 60, 30, 30);
		panel.add(maxPriceLabel);

		maxPriceTextField = new JFormattedTextField(priceFormatter);
		maxPriceTextField.setBounds(340, 60, 135, 30);
		maxPriceTextField.setValue(0);
		maxPriceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(maxPriceTextField);
	}

	private void initStartDatePanel(JPanel panel, Border lineBorder) {
		JLabel startDateLabel = new JLabel("Start date");
		startDateLabel.setBounds(25, 100, 80, 30);
		panel.add(startDateLabel);

		startDateTextField = new JFormattedTextField(dateFormatter);
		startDateTextField.setBounds(115, 100, 320, 30);
		startDateTextField.setBorder(lineBorder);
		startDateTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(startDateTextField);

		JDateChooser startDateChooser = new JDateChooser();
		startDateChooser.setBounds(445, 100, 30, 30);
		startDateChooser.setIcon(CALENDAR_ICON);
		panel.add(startDateChooser);
	}

	private void initEndDatePanel(JPanel panel, Border lineBorder) {
		JLabel endDateLabel = new JLabel("End date");
		endDateLabel.setBounds(25, 140, 80, 30);
		panel.add(endDateLabel);

		endDateTextField = new JFormattedTextField(dateFormatter);
		endDateTextField.setBounds(115, 140, 320, 30);
		endDateTextField.setBorder(lineBorder);
		endDateTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(endDateTextField);

		JDateChooser endDateChooser = new JDateChooser();
		endDateChooser.setBounds(445, 140, 30, 30);
		endDateChooser.setIcon(CALENDAR_ICON);
		panel.add(endDateChooser);
	}

	private void initButtons(JPanel panel) {
		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(165, 180, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);
		cancelButton.addActionListener((event) -> {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure to close?", null, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				System.out.println("Cancel: Yes");
				this.setVisible(false);
			}
			else
				System.out.println("Cancel: No");
		});

		// Sort button
		filterButton = new JButton("Filter");
		filterButton.setBounds(255, 180, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		filterButton.setHorizontalTextPosition(JButton.CENTER);
		filterButton.setBackground(Constants.LIGHT_BLUE);
		filterButton.setForeground(Color.WHITE);
		panel.add(filterButton);
	}

	public JFormattedTextField getMinQuantityTextField() {
		return minQuantityTextField;
	}

	public JFormattedTextField getMaxQuantityTextField() {
		return maxQuantityTextField;
	}

	public JFormattedTextField getMinPriceTextField() {
		return minPriceTextField;
	}

	public JFormattedTextField getMaxPriceTextField() {
		return maxPriceTextField;
	}

	public JFormattedTextField getStartDateTextField() {
		return startDateTextField;
	}

	public JFormattedTextField getEndDateTextField() {
		return endDateTextField;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getFilterButton() {
		return filterButton;
	}
}
