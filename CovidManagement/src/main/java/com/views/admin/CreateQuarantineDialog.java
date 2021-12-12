package com.views.admin;

import com.utilities.Constants;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class CreateQuarantineDialog extends JDialog {
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
//			mainView.display();
//		});
//	}

	private static final int LEFT_PADDING = 45;
	private static final int MIN_WIDTH = 100;
	private static final int MAX_WIDTH = 270;


	JTextField locationNameField;
	JFormattedTextField capacityField;
	JButton cancelButton;
	JButton createButton;

	CreateQuarantineDialog(JFrame frame) {
		super(frame);
		this.setTitle("Create Quarantine Location");

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(460, 140));
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

		NumberFormatter positiveIntegerFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
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

		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(145, 100, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
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

		createButton = new JButton("Create");
		createButton.setBounds(235, 100, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setBackground(Constants.LIGHT_BLUE);
		createButton.setForeground(Color.WHITE);
		panel.add(createButton);
		createButton.addActionListener((event) -> {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure to create this quarantine location?", null, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				System.out.println("Create: Yes");
				this.setVisible(false);
			}
			else
				System.out.println("Create: No");
		});
	}

	public JTextField getLocationNameField() {
		return locationNameField;
	}

	public JFormattedTextField getCapacityField() {
		return capacityField;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getCreateButton() {
		return createButton;
	}
}
