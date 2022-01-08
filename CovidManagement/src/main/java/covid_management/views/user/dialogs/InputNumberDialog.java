package covid_management.views.user.dialogs;

import covid_management.views.shared.panels.NumberFieldWithButton;
import shared.utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class InputNumberDialog extends JDialog {
	// Components
	private NumberFieldWithButton numberField;
	private JButton cancelButton;
	private JButton okButton;

	public InputNumberDialog(JFrame frame, String title, String label, int min, int max) {
		super(frame, title, true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(230, 100));
		initComponents(panel, label, min, max);

		setAlwaysOnTop(true);
		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel, String label, int min, int max) {
		JLabel numberLabel = new JLabel(label);
		numberLabel.setBounds(20, 20, 80, 30);
		panel.add(numberLabel);

		numberField = new NumberFieldWithButton(min, max, 110);
		numberField.setBounds(100, 20, 110, 30);
		panel.add(numberField);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(30, 60, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBackground(new Color(229, 229, 229));
		cancelButton.setForeground(Color.BLACK);
		panel.add(cancelButton);

		// Add button
		okButton = new JButton("Ok");
		okButton.setBounds(120, 60, Constants.BUTTON_SMALL_WIDTH, Constants.BUTTON_HEIGHT);
		okButton.setHorizontalTextPosition(JButton.CENTER);
		okButton.setBackground(Constants.LIGHT_BLUE);
		okButton.setForeground(Color.WHITE);
		panel.add(okButton);
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getOkButton() {
		return okButton;
	}

	public int getNumber() {
		return numberField.getValue();
	}

	public void setNumber(int number) {
		numberField.setValue(number);
	}

	public void setRange(int min, int max) {
		numberField.setRange(min, max);
	}

	public void setVisibleIncreaseAnDecreaseButtons(boolean visible) {
		numberField.setVisibleIncreaseAnDecreaseButtons(visible);
	}
}
