package com.views.shared.panels;

import com.utilities.Constants;
import com.utilities.UtilityFunctions;

import javax.swing.*;
import java.awt.*;

public class DateChooserPanel extends JPanel {
	// Constants
	private static final Byte[] MONTHS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

	// Components
	private JComboBox<Short> yearOptions;
	private JComboBox<Byte> monthOptions;
	private JComboBox<Byte> dayOptions;

	public DateChooserPanel(short minYear, short maxYear) {
		super();

		setLayout(null);
		setPreferredSize(new Dimension(330, 30));
		initComponents();

		minYear = (minYear < 1930) ? 1930 : minYear;
		maxYear = (maxYear < minYear) ? minYear : maxYear;

		setRangeYear(minYear, maxYear);
		resetRangeDay();

		// -------------------------------------------------
		yearOptions.addActionListener((event) -> resetRangeDay());
		monthOptions.addActionListener((event) -> resetRangeDay());
		// -------------------------------------------------
	}

	private void initComponents() {
		JLabel yearLabel = new JLabel("Year");
		yearLabel.setBounds(0, 0, 30, Constants.TEXT_HEIGHT);
		add(yearLabel);

		yearOptions = new JComboBox<>();
		yearOptions.setBounds(35, 0, 70, 30);
		add(yearOptions);

		JLabel monthLabel = new JLabel("Month");
		monthLabel.setBounds(125, 0, 50, Constants.TEXT_HEIGHT);
		add(monthLabel);

		monthOptions = new JComboBox<>(MONTHS);
		monthOptions.setBounds(180, 0, 50, 30);
		add(monthOptions);

		JLabel dayLabel = new JLabel("Day");
		dayLabel.setBounds(250, 0, 25, Constants.TEXT_HEIGHT);
		add(dayLabel);

		dayOptions = new JComboBox<>();
		dayOptions.setBounds(280, 0, 50, 30);
		add(dayOptions);
	}

	public void setRangeYear(short minYear, short maxYear) {
		if (minYear >= 1930 && maxYear >= minYear) {
			short count = (short) (maxYear - minYear + 1);
			Short[] years = new Short[count];
			for (short i = 0; i < count; i++)
				years[i] = (short) (minYear + i);

			yearOptions.setModel(new DefaultComboBoxModel<>(years));
			resetRangeDay();
		}
	}

	public short getSelectedYear() {
		return (short) yearOptions.getSelectedItem();
	}

	public byte getSelectedMonth() {
		return (byte) monthOptions.getSelectedItem();
	}

	public byte getSelectedDay() {
		return (byte) dayOptions.getSelectedItem();
	}

	private Byte[] getRangeDay(short year, byte month) {
		byte countDay;
		switch (month) {
			case 2 -> countDay = (byte) (UtilityFunctions.isLeapYear(year) ? 29 : 28);
			case 4, 6, 9, 11 -> countDay = 30;
			default -> countDay = 31; // 1, 3, 5, 7, 8, 10, 12
		}

		Byte[] days = new Byte[countDay];
		for (byte i = 0; i < countDay; i++)
			days[i] = (byte) (i + 1);

		return days;
	}

	private void resetRangeDay() {
		short year = (short) yearOptions.getSelectedItem();
		byte month = (byte) monthOptions.getSelectedItem();
		dayOptions.setModel(new DefaultComboBoxModel<>(getRangeDay(year, month)));
	}
}
