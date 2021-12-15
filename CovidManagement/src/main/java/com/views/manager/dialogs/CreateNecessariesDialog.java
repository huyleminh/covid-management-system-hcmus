package com.views.manager.dialogs;

import com.utilities.Constants;
import com.views.shared.panels.DateChooserPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class CreateNecessariesDialog extends JDialog {
	// Constants
	private static final int HORIZONTAL_PADDING = 25;
	private static final int INFO_LABEL_WIDTH = 120;
	private static final int INFO_TEXT_FIELD_WIDTH = 230;
	private static final int TITLED_BORDER_WIDTH = 350;
	private static final int TITLED_BORDER_HEIGHT = 60;

	// Input formatters
	private NumberFormatter quantityFormatter;
	private NumberFormatter priceFormatter;

	// Components
	private JTextField necessariesNameTextField;
	private JFormattedTextField limitedQuantityTextField;
	private JFormattedTextField priceTextField;
	private DateChooserPanel startDateChooser;
	private DateChooserPanel endDateChooser;

	private JButton cancelButton;
	private JButton createButton;

	public CreateNecessariesDialog(JFrame frame) {
		super(frame, "Create Necesasries", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(400, 320));

		initInputFormatters();
		initComponents(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		Border grayLineBorder = BorderFactory.createLineBorder(Color.GRAY, 1);

		initBasicPanels(panel);
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

	private void initBasicPanels(JPanel panel) {
		// Necessaries name label
		JLabel necessariesNameLabel = new JLabel("Name");
		necessariesNameLabel.setBounds(HORIZONTAL_PADDING, 20, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(necessariesNameLabel);

		// Necessaries name text field
		necessariesNameTextField = new JTextField();
		necessariesNameTextField.setBounds(HORIZONTAL_PADDING + INFO_LABEL_WIDTH, 20, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(necessariesNameTextField);

		// Limited quantity label
		JLabel limitedQuantityLabel = new JLabel("Limited quantity");
		limitedQuantityLabel.setBounds(HORIZONTAL_PADDING, 60, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(limitedQuantityLabel);

		// Limited quantity text field
		limitedQuantityTextField = new JFormattedTextField(quantityFormatter);
		limitedQuantityTextField.setBounds(HORIZONTAL_PADDING + INFO_LABEL_WIDTH, 60, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		limitedQuantityTextField.setValue(0);
		panel.add(limitedQuantityTextField);

		// Price label
		JLabel priceLabel = new JLabel("Price (VND)");
		priceLabel.setBounds(HORIZONTAL_PADDING, 100, INFO_LABEL_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(priceLabel);

		// Price text field
		priceTextField = new JFormattedTextField(priceFormatter);
		priceTextField.setBounds(HORIZONTAL_PADDING + INFO_LABEL_WIDTH, 100, INFO_TEXT_FIELD_WIDTH, Constants.TEXT_HEIGHT);
		priceTextField.setValue(0);
		panel.add(priceTextField);
	}

	private void initStartDatePanel(JPanel panel, Border lineBorder) {
		JPanel startDatePanel = new JPanel();
		startDatePanel.setLayout(null);
		startDatePanel.setBounds(HORIZONTAL_PADDING, 140, TITLED_BORDER_WIDTH, TITLED_BORDER_HEIGHT);
		startDatePanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Start date"));
		panel.add(startDatePanel);

		startDateChooser = new DateChooserPanel((short) 1930, (short) 2021);
		startDateChooser.setBounds(10, 20, 330, 30);
		startDatePanel.add(startDateChooser);
	}

	private void initEndDatePanel(JPanel panel, Border lineBorder) {
		JPanel endDatePanel = new JPanel();
		endDatePanel.setLayout(null);
		endDatePanel.setBounds(HORIZONTAL_PADDING, 210, TITLED_BORDER_WIDTH, TITLED_BORDER_HEIGHT);
		endDatePanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "End date"));
		panel.add(endDatePanel);

		endDateChooser = new DateChooserPanel((short) 1930, (short) 2022);
		endDateChooser.setBounds(10, 20, 330, 30);
		endDatePanel.add(endDateChooser);
	}

	private void initButtons(JPanel panel) {
		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(115, 280, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
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

		// Create button
		createButton = new JButton("Create");
		createButton.setBounds(205, 280, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setForeground(Color.WHITE);
		panel.add(createButton);
		createButton.addActionListener((event) -> {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure to create this user?", null, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				System.out.println("Create: Yes");
				this.setVisible(false);
			}
			else
				System.out.println("Create: No");
		});
	}

	public JTextField getNecessariesNameTextField() {
		return necessariesNameTextField;
	}

	public JTextField getLimitedQuantityTextField() {
		return limitedQuantityTextField;
	}

	public JTextField getPriceTextField() {
		return priceTextField;
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

	public JButton getCreateButton() {
		return createButton;
	}
}
