package com.views.admin;

import com.utilities.Constants;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class EditQuarantineDialog extends JDialog {
//	public static void main(String[] args) {
//		try {
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		SwingUtilities.invokeLater(() -> {
//			ManagerManagementView mainView = new ManagerManagementView();
//			CreateAccountDialog createAccountDialog = new CreateAccountDialog(mainView.getFrame());
//			ViewActivityDialog viewActivityDialog = new ViewActivityDialog(mainView.getFrame());
//			CreateQuarantineDialog createQuarantineDialog = new CreateQuarantineDialog(mainView.getFrame());
//			EditQuarantineDialog editQuarantineDialog = new EditQuarantineDialog(mainView.getFrame());
//
//			mainView.getCreateButton().addActionListener((event) -> {
//				if (mainView.getSelectingFeature() == ManagerManagementView.MANAGE_MANAGER) {
//					createQuarantineDialog.setVisible(false);
//					createAccountDialog.setVisible(true);
//				} else {
//					createAccountDialog.setVisible(false);
//					createQuarantineDialog.setVisible(true);
//				}
//			});
//			mainView.getViewActivityButton().addActionListener((event) -> {
//				viewActivityDialog.setVisible(true);
//			});
//			mainView.getEditQuarantineButton().addActionListener((event) -> {
//				editQuarantineDialog.setVisible(true);
//			});
//			mainView.display();
//		});
//	}

	private static final int LEFT_PADDING = 45;
	private static final int MIN_WIDTH = 100;
	private static final int MAX_WIDTH = 270;

	JTextField locationNameField;
	JFormattedTextField capacityField;
	JFormattedTextField currentSlotsField;
	JButton cancelButton;
	JButton saveButton;

	EditQuarantineDialog(JFrame frame) {
		super(frame);
		this.setTitle("Create Quarantine Location");

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(460, 180));
		initComponents(panel);

		this.setResizable(false);
		this.setContentPane(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setModal(true);
	}

	private void initComponents(JPanel panel) {
		JLabel locationNameLabel = new JLabel("Location name");
		locationNameLabel.setBounds(LEFT_PADDING, 20, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(locationNameLabel);

		// locationName text field
		locationNameField = new JTextField();
		locationNameField.setBounds(145, 20, MAX_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(locationNameField);

		// positive integer formatter
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);

		NumberFormatter positiveIntegerFormatter = new NumberFormatter(numberFormat);
		positiveIntegerFormatter.setMinimum(0);
		positiveIntegerFormatter.setMaximum(Integer.MAX_VALUE);
		positiveIntegerFormatter.setAllowsInvalid(false);
		positiveIntegerFormatter.setCommitsOnValidEdit(true);

		// capacity label
		JLabel capacityLabel = new JLabel("Capacity");
		capacityLabel.setBounds(LEFT_PADDING, 60, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(capacityLabel);

		// capacity input number field
		capacityField = new JFormattedTextField(positiveIntegerFormatter);
		capacityField.setBounds(145, 60, 100, Constants.TEXT_HEIGHT);
		capacityField.setValue(0);
		capacityField.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(capacityField);

		// current slots label
		JLabel currentSlotsLabel = new JLabel("Current slots");
		currentSlotsLabel.setBounds(LEFT_PADDING, 100, MIN_WIDTH, Constants.TEXT_HEIGHT);
		panel.add(currentSlotsLabel);

		// current slots input number field
		currentSlotsField = new JFormattedTextField(positiveIntegerFormatter);
		currentSlotsField.setBounds(145, 100, 100, Constants.TEXT_HEIGHT);
		currentSlotsField.setValue(0);
		currentSlotsField.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(currentSlotsField);

		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(145, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
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

		saveButton = new JButton("Save");
		saveButton.setBounds(235, 140, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		saveButton.setHorizontalTextPosition(JButton.CENTER);
		saveButton.setBackground(Constants.GREEN);
		saveButton.setForeground(Color.WHITE);
		panel.add(saveButton);
		saveButton.addActionListener((event) -> {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure to save this information?", null, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				System.out.println("Save: Yes");
				this.setVisible(false);
			}
			else
				System.out.println("Save: No");
		});
	}

	public JTextField getLocationNameField() {
		return locationNameField;
	}

	public JFormattedTextField getCapacityField() {
		return capacityField;
	}

	public JFormattedTextField getCurrentSlotsField() {
		return currentSlotsField;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getSaveButton() {
		return saveButton;
	}
}
