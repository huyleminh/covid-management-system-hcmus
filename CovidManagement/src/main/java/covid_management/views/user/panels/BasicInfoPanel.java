package covid_management.views.user.panels;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class BasicInfoPanel extends JPanel {
	// Components
	final private JTextField fullnameTextField;
	final private JTextField idCardTextField;
	final private JTextField yearBirthTextField;
	final private JTextField addressTextField;
	final private JTextField currentStatusTextField;
	final private JTextField quarantineTextField;
	final private JTextField infectiousPersonTextField;

	public BasicInfoPanel() {
		super();
		setLayout(null);

		Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);

		// Full name label
		JLabel fullnameLabel = new JLabel("Full name");
		fullnameLabel.setBounds(10, 10, 150, 30);
		add(fullnameLabel);

		// Full name text field
		fullnameTextField = new JTextField();
		fullnameTextField.setBounds(160, 10, 610, 30);
		fullnameTextField.setBorder(lineBorder);
		fullnameTextField.setEditable(false);
		add(fullnameTextField);

		// ID card label
		JLabel idCardLabel = new JLabel("ID card");
		idCardLabel.setBounds(10, 50, 150, 30);
		add(idCardLabel);

		// ID card text field
		idCardTextField = new JTextField();
		idCardTextField.setBounds(160, 50, 610, 30);
		idCardTextField.setBorder(lineBorder);
		idCardTextField.setEditable(false);
		add(idCardTextField);

		// Year of birth label
		JLabel yearBirthLabel = new JLabel("Year of birth");
		yearBirthLabel.setBounds(10, 90, 150, 30);
		add(yearBirthLabel);

		// Year of birth text field
		yearBirthTextField = new JTextField();
		yearBirthTextField.setBounds(160, 90, 610, 30);
		yearBirthTextField.setBorder(lineBorder);
		yearBirthTextField.setEditable(false);
		add(yearBirthTextField);

		// Address label
		JLabel addressLabel = new JLabel("Address");
		addressLabel.setBounds(10, 130, 150, 30);
		add(addressLabel);

		// Address text field
		addressTextField = new JTextField();
		addressTextField.setBounds(160, 130, 610, 30);
		addressTextField.setBorder(lineBorder);
		addressTextField.setEditable(false);
		add(addressTextField);

		// Current status label
		JLabel currentStatusLabel = new JLabel("Current status");
		currentStatusLabel.setBounds(10, 170, 150, 30);
		add(currentStatusLabel);

		// Current status text field
		currentStatusTextField = new JTextField();
		currentStatusTextField.setBounds(160, 170, 610, 30);
		currentStatusTextField.setBorder(lineBorder);
		currentStatusTextField.setEditable(false);
		add(currentStatusTextField);

		// Quarantine location label
		JLabel quarantineLabel = new JLabel("Quarantine location");
		quarantineLabel.setBounds(10, 210, 150, 30);
		add(quarantineLabel);

		// Quarantine location text field
		quarantineTextField = new JTextField();
		quarantineTextField.setBounds(160, 210, 610, 30);
		quarantineTextField.setBorder(lineBorder);
		quarantineTextField.setEditable(false);
		add(quarantineTextField);

		// Infectious person label
		JLabel infectiousPersonLabel = new JLabel("Infectious person");
		infectiousPersonLabel.setBounds(10, 250, 150, 30);
		add(infectiousPersonLabel);

		// Infectious person text field
		infectiousPersonTextField = new JTextField();
		infectiousPersonTextField.setBounds(160, 250, 610, 30);
		infectiousPersonTextField.setBorder(lineBorder);
		infectiousPersonTextField.setEditable(false);
		add(infectiousPersonTextField);
	}

	public void clearDataShowing() {
		fullnameTextField.setText("");
		idCardTextField.setText("");
		yearBirthTextField.setText("");
		addressTextField.setText("");
		currentStatusTextField.setText("");
		quarantineTextField.setText("");
		infectiousPersonTextField.setText("");
	}

	public JTextField getFullnameTextField() {
		return fullnameTextField;
	}

	public JTextField getIdCardTextField() {
		return idCardTextField;
	}

	public JTextField getYearBirthTextField() {
		return yearBirthTextField;
	}

	public JTextField getAddressTextField() {
		return addressTextField;
	}

	public JTextField getCurrentStatusTextField() {
		return currentStatusTextField;
	}

	public JTextField getQuarantineTextField() {
		return quarantineTextField;
	}

	public JTextField getInfectiousPersonTextField() {
		return infectiousPersonTextField;
	}
}
