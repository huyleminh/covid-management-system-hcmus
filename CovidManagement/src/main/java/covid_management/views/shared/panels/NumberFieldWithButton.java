package covid_management.views.shared.panels;

import shared.utilities.Constants;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class NumberFieldWithButton extends JPanel {
	// Components
	final private JFormattedTextField numberTextField;
	final private BasicArrowButton increaseButton;
	final private BasicArrowButton decreaseButton;

	private int minValue;
	private int maxValue;

	public NumberFieldWithButton(int minValue, int maxValue, int width) {
		super();
		this.minValue = minValue;
		this.maxValue = maxValue;

		setLayout(null);
		setPreferredSize(new Dimension(width, Constants.TEXT_HEIGHT));

		// Number formatter without grouping separator
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);

		// Number formatter
		NumberFormatter quantityFormatter = new NumberFormatter(numberFormat);
		quantityFormatter.setMinimum(minValue);
		quantityFormatter.setMaximum(maxValue);
		quantityFormatter.setAllowsInvalid(false);
		quantityFormatter.setCommitsOnValidEdit(true);

		// Number text field
		numberTextField = new JFormattedTextField(quantityFormatter);
		numberTextField.setBounds(0, 0, width - 30, 30);
		numberTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		numberTextField.setValue(minValue);
		numberTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		add(numberTextField);

		// Increase button
		increaseButton = new BasicArrowButton(SwingConstants.NORTH);
		increaseButton.setBounds(width - 30, 0, 30, 15);
		add(increaseButton);

		// Decrease button
		decreaseButton = new BasicArrowButton(SwingConstants.SOUTH);
		decreaseButton.setBounds(width - 30, 15, 30, 15);
		add(decreaseButton);

		addActionForBasicArrowButtons();
	}

	private void addActionForBasicArrowButtons() {
		increaseButton.addActionListener((event) -> {
			int value = Integer.parseInt(numberTextField.getText());
			if (value < maxValue) {
				numberTextField.setValue(value + 1);
			}
		});

		decreaseButton.addActionListener((event) -> {
			int value = Integer.parseInt(numberTextField.getText());
			if (value > minValue) {
				numberTextField.setValue(value - 1);
			}
		});
	}

	public void setEnabledNumberFieldAndButtons(boolean enabled) {
		numberTextField.setEnabled(enabled);
		increaseButton.setEnabled(enabled);
		decreaseButton.setEnabled(enabled);
	}

	public int getValue() {
		return Integer.parseInt(numberTextField.getText());
	}

	public void setValue(int value) {
		if (value < minValue || value > maxValue) {
			numberTextField.setValue(minValue);
		} else {
			numberTextField.setValue(value);
		}
	}

	public void setRange(int min, int max) {
		NumberFormatter numberFormatter = (NumberFormatter) numberTextField.getFormatter();
		numberFormatter.setMinimum(min);
		numberFormatter.setMaximum(max);

		this.numberTextField.setValue(min);
		this.minValue = min;
		this.maxValue = max;
	}

	public void setVisibleIncreaseAnDecreaseButtons(boolean visible) {
		increaseButton.setVisible(visible);
		decreaseButton.setVisible(visible);
	}
}
