package com.views.shared.dialogs;

import com.utilities.Constants;
import com.views.shared.panels.DateChooserPanel;

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
	private NumberFormatter quantityFormatter;
	private NumberFormatter priceFormatter;

	// Components
	private JFormattedTextField minQuantityTextField;
	private JFormattedTextField maxQuantityTextField;
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
		panel.setPreferredSize(new Dimension(400, 335));

		initInputFormatters();
		initComponents(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		Border grayLineBorder = BorderFactory.createLineBorder(Color.GRAY, 1);

		initQuantityPanel(panel, grayLineBorder);
		initPricePanel(panel, grayLineBorder);
		initStartDatePanel(panel, grayLineBorder);
		initEndDatePanel(panel, grayLineBorder);
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
	}

	private void initQuantityPanel(JPanel panel, Border lineBorder) {
		JPanel quantityPanel = new JPanel();
		quantityPanel.setLayout(null);
		quantityPanel.setBounds(HORIZONTAL_PADDING, 15, TITLED_BORDER_WIDTH, TITLED_BORDER_HEIGHT);
		quantityPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Quantity"));
		panel.add(quantityPanel);

		JLabel minQuantityLabel = new JLabel("Min");
		minQuantityLabel.setBounds(10, 20, 30, Constants.TEXT_HEIGHT);
		quantityPanel.add(minQuantityLabel);

		minQuantityTextField = new JFormattedTextField(quantityFormatter);
		minQuantityTextField.setBounds(45, 20, 120, Constants.TEXT_HEIGHT);
		minQuantityTextField.setValue(0);
		minQuantityTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		quantityPanel.add(minQuantityTextField);

		JLabel maxQuantityLabel = new JLabel("Max");
		maxQuantityLabel.setBounds(185, 20, 30, Constants.TEXT_HEIGHT);
		quantityPanel.add(maxQuantityLabel);

		maxQuantityTextField = new JFormattedTextField(quantityFormatter);
		maxQuantityTextField.setBounds(220, 20, 120, Constants.TEXT_HEIGHT);
		maxQuantityTextField.setValue(0);
		maxQuantityTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		quantityPanel.add(maxQuantityTextField);
	}

	private void initPricePanel(JPanel panel, Border lineBorder) {
		JPanel pricePanel = new JPanel();
		pricePanel.setLayout(null);
		pricePanel.setBounds(HORIZONTAL_PADDING, 85, TITLED_BORDER_WIDTH, TITLED_BORDER_HEIGHT);
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
		startDatePanel.setBounds(HORIZONTAL_PADDING, 155, TITLED_BORDER_WIDTH, TITLED_BORDER_HEIGHT);
		startDatePanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Start date"));
		panel.add(startDatePanel);

		startDateChooser = new DateChooserPanel((short) 1930, (short) 2021);
		startDateChooser.setBounds(10, 20, 330, 30);
		startDatePanel.add(startDateChooser);
	}

	private void initEndDatePanel(JPanel panel, Border lineBorder) {
		JPanel endDatePanel = new JPanel();
		endDatePanel.setLayout(null);
		endDatePanel.setBounds(HORIZONTAL_PADDING, 225, TITLED_BORDER_WIDTH, TITLED_BORDER_HEIGHT);
		endDatePanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "End date"));
		panel.add(endDatePanel);

		endDateChooser = new DateChooserPanel((short) 1930, (short) 2022);
		endDateChooser.setBounds(10, 20, 330, 30);
		endDatePanel.add(endDateChooser);
	}

	private void initButtons(JPanel panel) {
		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(115, 295, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
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

		// Filter button
		filterButton = new JButton("Filter");
		filterButton.setBounds(205, 295, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
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
}
