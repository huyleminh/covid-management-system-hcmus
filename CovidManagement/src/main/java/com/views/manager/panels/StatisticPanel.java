package com.views.manager.panels;

import com.models.User;
import com.utilities.Constants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StatisticPanel extends JPanel {
	// Constants.
	private static final String[] STATISTIC_OPTION_NAMES = {
			"The number of people at each state",
			"The number of changing state",
			"The number of recoveries",
			"Statistic of consumption of necessaries",
			"The total amount of debt"
	};
	private static final String CATEGORY_AXIS_LABEL = "Months";
	private static final String[] VALUE_AXIS_NAMES = {
			"Number of people",
			"Number of times",
			"Number of people",
			"Number of packages",
			"Money (VND)"
	};
	private static final String[] COLUMN_KEYS = {
			"September",
			"October",
			"November",
			"December"
	};
	private static final String[][] ROW_KEYS = {
			User.STATUS_NAMES,
			{"Changing state"},
			{"Recovery"},
			{"Combo 1", "Combo 2", "Combo 3", "Combo 4", "Combo 5"}, // need to review, possible change
			{"Total amount"},
	};

	// Bar chart.
	private DefaultCategoryDataset dataset;

	// Components.
	private JComboBox<String> statisticOptions;
	private JButton refreshButton;
	private ChartPanel chartPanel;

	// testing
	private static String selectingOption;

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
		statisticOptions.setBounds(60, 0, 320, Constants.TEXT_HEIGHT);
		add(statisticOptions);

		selectingOption = STATISTIC_OPTION_NAMES[0];

		// ----------------------------------------------
		statisticOptions.addActionListener((event) -> {
			String selectedItem = (String)statisticOptions.getSelectedItem();
			if (!selectingOption.equals(selectedItem)) {
				selectingOption = selectedItem;
				resetChart();
			}
		});
		// ----------------------------------------------

		refreshButton = new JButton("Refresh");
		refreshButton.setBounds(670, 0, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		refreshButton.setBackground(Constants.LIGHT_BLUE);
		refreshButton.setHorizontalTextPosition(SwingConstants.CENTER);
		refreshButton.setForeground(Color.WHITE);
		add(refreshButton);

		// ----------------------------------------------
		refreshButton.addActionListener((event) -> {
			String[] rowKeys = ROW_KEYS[getRowKeyIndex((String) statisticOptions.getSelectedItem())];
			Random randomGenerator = new Random();

			ArrayList<Number> values = new ArrayList<>();
			int count = COLUMN_KEYS.length * rowKeys.length;
			for (int i = 0; i < count; i++)
				values.add(randomGenerator.nextInt(1, 100));

			setValues(values, rowKeys, COLUMN_KEYS);
		});
		// ----------------------------------------------

		dataset = new DefaultCategoryDataset();
		JFreeChart barChart = ChartFactory.createBarChart(
				STATISTIC_OPTION_NAMES[0] + " in the last four months",
				CATEGORY_AXIS_LABEL,
				VALUE_AXIS_NAMES[0],
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);
		chartPanel = new ChartPanel(barChart);
		chartPanel.setBounds(0, 40, 780, 540);
		chartPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		add(chartPanel);
	}

	private int getRowKeyIndex(String statisticOptionName) {
		for (int i = 0; i < STATISTIC_OPTION_NAMES.length; i++)
			if (statisticOptionName.equals(STATISTIC_OPTION_NAMES[i]))
				return i;

		return 0;
	}

	public void setValue(Number value, String rowKey, String columnKey) {
		dataset.setValue(value, rowKey, columnKey);
	}

	public void setValues(ArrayList<Number> values, String[] rowKeys, String[] columnKeys) {
		int index = 0;
		for (String columnKey : columnKeys) {
			for (String rowKey : rowKeys) {
				dataset.setValue(values.get(index), rowKey, columnKey);
				++index;
			}
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

	private void resetChart() {
		removeAllValues();
		removeAllRows();
		removeAllColumns();

		int rowKeyIndex = getRowKeyIndex(selectingOption);
		JFreeChart barChart = ChartFactory.createBarChart(
				STATISTIC_OPTION_NAMES[rowKeyIndex] + " in the last four months",
				CATEGORY_AXIS_LABEL,
				VALUE_AXIS_NAMES[rowKeyIndex],
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);
		chartPanel.setChart(barChart);
	}

	public JComboBox<String> getStatisticOptions() {
		return statisticOptions;
	}

	public JButton getRefreshButton() {
		return refreshButton;
	}
}
