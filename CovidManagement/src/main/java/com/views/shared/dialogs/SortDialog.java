package com.views.shared.dialogs;

import com.utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class SortDialog extends JDialog {
	// Components
	private JComboBox<String> sortOptions;
	private JButton cancelButton;
	private JButton sortButton;

	public SortDialog(JFrame frame, String title) {
		super(frame, title, true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(400, 100));

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
		panel.setPreferredSize(new Dimension(400, 100));

		initComponents(panel);
		setOptionNames(optionNames);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
		setModal(true);
	}

	private void initComponents(JPanel panel) {
		// Sort options
		sortOptions = new JComboBox<>();
		sortOptions.setBounds(25, 20, 350, 30);
		panel.add(sortOptions);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(115, 60, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
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
		sortButton = new JButton("Sort");
		sortButton.setBounds(205, 60, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		sortButton.setHorizontalTextPosition(JButton.CENTER);
		sortButton.setBackground(Constants.LIGHT_BLUE);
		sortButton.setForeground(Color.WHITE);
		panel.add(sortButton);
	}

	public void setOptionNames(String[] optionNames) {
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(optionNames);
		sortOptions.setModel(comboBoxModel);
	}

	public String getSelectedOption() {
		Object nullableObject = sortOptions.getModel().getSelectedItem();
		return (nullableObject != null) ? ((String) nullableObject) : "";
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getSortButton() {
		return sortButton;
	}
}
