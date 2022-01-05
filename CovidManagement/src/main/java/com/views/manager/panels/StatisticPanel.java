package com.views.manager.panels;

import com.utilities.Constants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StatisticPanel extends JPanel {
	// Constants.
	public static final String[] STATISTIC_OPTION_NAMES = {
			"The number of people at each state",
			"The number of changing state",
			"The number of recoveries",
			"The total amount of debt",
			"Statistic of consumption of necessaries (quantity)",
			"Statistic of consumption of necessaries (income)",
	};
	public static final String[] VALUE_AXIS_NAMES = {
			"Number of people",
			"Number of times",
			"Number of people",
			"Money (VND)",
			"Number of packages",
			"Money (VND)",
	};
	private static final Integer[] YEARS = {2019, 2020, 2021, 2022};
	private static final Integer[] MONTHS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

	// Bar chart.
	private DefaultCategoryDataset dataset;

	// Components.
	private JComboBox<String> statisticOptions;
	private JComboBox<Integer> yearOptions;
	private JComboBox<Integer> monthOptions;
	private JButton refreshButton;
	private ChartPanel chartPanel;

	public StatisticPanel() {
		super();

		setLayout(null);
		initComponents();
	}

	private void initComponents() {
		JLabel statisticOptionLabel = new JLabel("Options");
		statisticOptionLabel.setBounds(0, 0, 50, Constants.TEXT_HEIGHT);
		add(statisticOptionLabel);

		statisticOptions = new JComboBox<>(STATISTIC_OPTION_NAMES);
		statisticOptions.setBounds(60, 0, 340, Constants.TEXT_HEIGHT);
		add(statisticOptions);

		JLabel monthLabel = new JLabel("Month");
		monthLabel.setBounds(450, 0, 50, Constants.TEXT_HEIGHT);
		add(monthLabel);

		monthOptions = new JComboBox<>(MONTHS);
		monthOptions.setBounds(500, 0, 50, 30);
		add(monthOptions);

		JLabel yearLabel = new JLabel("Year");
		yearLabel.setBounds(560, 0, 30, Constants.TEXT_HEIGHT);
		add(yearLabel);

		yearOptions = new JComboBox<>(YEARS);
		yearOptions.setBounds(590, 0, 70, 30);
		add(yearOptions);

		refreshButton = new JButton("Refresh");
		refreshButton.setBounds(670, 0, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		refreshButton.setBackground(Constants.LIGHT_BLUE);
		refreshButton.setHorizontalTextPosition(SwingConstants.CENTER);
		refreshButton.setForeground(Color.WHITE);
		add(refreshButton);

		dataset = new DefaultCategoryDataset();
		chartPanel = new ChartPanel(createBarChart());
		chartPanel.setBounds(0, 40, 780, 540);
		chartPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		add(chartPanel);
	}

	public void setValue(Number value, String rowKey, String columnKey) {
		dataset.setValue(value, rowKey, columnKey);
	}

	public void setValues(ArrayList<Number> values, ArrayList<String> rowKeys, ArrayList<String> columnKeys) {
		int index = 0;

		for (String columnKey : columnKeys) {
			for (String rowKey : rowKeys) {
				dataset.setValue(values.get(index), rowKey, columnKey);
				++index;
			}
		}

		double upperBound = chartPanel.getChart()
				.getCategoryPlot()
				.getRangeAxis()
				.getUpperBound();

		if (upperBound < 10.0) {
			chartPanel.getChart()
					.getCategoryPlot()
					.getRangeAxis()
					.setUpperBound(upperBound + 10.0);
		}
	}

	public void removeAllRows() {
		List<String> rowKeys = dataset.getRowKeys();
		for (String rowKey : rowKeys)
			dataset.removeRow(rowKey);
	}

	public void removeAllColumns() {
		List<String> columnKeys = dataset.getColumnKeys();
		for (String columnKey : columnKeys)
			dataset.removeColumn(columnKey);
	}

	public void removeAllValues() {
		dataset.clear();
	}

	private String createChartTitleFrom(String statsOption, int month, int year) {
		Month monthInstance = Month.of(month);
		int index = statsOption.indexOf(" (");  // remove " (quantity)" or " (price)"
		if (index != -1)
			statsOption = statsOption.substring(0, index);

		statsOption += " in %s, %d".formatted(monthInstance.getDisplayName(TextStyle.FULL, Locale.ENGLISH), year);
		return statsOption;
	}

	private JFreeChart createBarChart() {
		int year = (int) yearOptions.getSelectedItem();
		int month = (int) monthOptions.getSelectedItem();
		Month monthInstance = Month.of(month);
		String statsOption = String.valueOf(getStatisticOptions().getSelectedItem());

		String chartTitle = createChartTitleFrom(statsOption, month, year);
		String categoryName = "%s, %d".formatted(
				monthInstance.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
				year
		);

		return ChartFactory.createBarChart(
				chartTitle,
				categoryName,
				VALUE_AXIS_NAMES[getRowKeyIndex(statsOption)],
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);
	}

	public void resetChart() {
		removeAllValues();
		removeAllRows();
		removeAllColumns();

		chartPanel.setChart(createBarChart());
	}

	private int getRowKeyIndex(String statsOptionName) {
		for (int i = 0; i < STATISTIC_OPTION_NAMES.length; i++)
			if (statsOptionName.equals(STATISTIC_OPTION_NAMES[i]))
				return i;

		return 0;
	}

	public void setChartTitle(String chartTitle) {
		chartPanel.getChart().setTitle(chartTitle);
	}

	public JComboBox<String> getStatisticOptions() {
		return statisticOptions;
	}

	public JComboBox<Integer> getYearOptions() {
		return yearOptions;
	}

	public JComboBox<Integer> getMonthOptions() {
		return monthOptions;
	}

	public JButton getRefreshButton() {
		return refreshButton;
	}
}
