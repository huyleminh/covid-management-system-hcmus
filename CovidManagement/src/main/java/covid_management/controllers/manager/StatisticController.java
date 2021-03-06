package covid_management.controllers.manager;

import covid_management.dao.OrderDAO;
import covid_management.dao.OrderDetailDAO;
import covid_management.dao.UserHistoryDAO;
import covid_management.models.User;
import covid_management.views.manager.panels.StatisticPanel;
import shared.components.dialogs.ConnectionErrorDialog;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StatisticController implements ActionListener {
	final private StatisticPanel statisticPanel;
	final private ConnectionErrorDialog connectionErrorDialog;

	public StatisticController(JFrame mainFrame, StatisticPanel statisticPanel) {
		this.statisticPanel = statisticPanel;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);

		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
		this.statisticPanel.getStatisticOptions().addActionListener(this);
		this.statisticPanel.getMonthOptions().addActionListener(this);
		this.statisticPanel.getYearOptions().addActionListener(this);
		this.statisticPanel.getRefreshButton().addActionListener(this);
	}

	public void preprocessAndDisplayUI() {
		String statsOption = String.valueOf(statisticPanel.getStatisticOptions().getSelectedItem());

		if (statsOption.equals(StatisticPanel.STATISTIC_OPTION_NAMES[0]))
			refreshAction();
		else
			statisticPanel.getStatisticOptions().setSelectedItem(StatisticPanel.STATISTIC_OPTION_NAMES[0]);

		statisticPanel.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectAction();
		} else {
			refreshAction();
		}
	}

	private void reconnectAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		refreshAction();
	}

	private void refreshAction() {
		statisticPanel.resetChart();
		statisticActionImp();
	}

	private ArrayList<Number> getStatisticValues(String statsOption)
	throws DBConnectionException {
		ArrayList<Number> statsValues = new ArrayList<>();
		int month = (int) statisticPanel.getMonthOptions().getSelectedItem();
		int year = (int) statisticPanel.getYearOptions().getSelectedItem();

		switch (statsOption) {
			case "The number of people at each state" -> {
				UserHistoryDAO daoModel = new UserHistoryDAO();
				statsValues = daoModel.getNumberOfPeopleAtEachStateByMonthAndYear(month, year);
			}
			case "The number of changing state" -> {
				UserHistoryDAO daoModel = new UserHistoryDAO();
				statsValues = daoModel.getNumberOfChangingStateByMonthAndYear(month, year);
			}
			case "The number of recoveries" -> {
				UserHistoryDAO daoModel = new UserHistoryDAO();
				statsValues = daoModel.getNumberOfRecoveryPeopleByMonthAndYear(month, year);
			}
			case "The total amount of debt" -> {
				OrderDAO daoModel = new OrderDAO();
				statsValues = daoModel.getTotalPriceByMonthAndYear(month, year);
			}
			case "Statistic of consumption of necessaries (quantity)" -> {
				OrderDetailDAO daoModel = new OrderDetailDAO();
				statsValues = daoModel.getAllQuantityOfSoldNecessariesByMonthAndYear(month, year);
			}
			case "Statistic of consumption of necessaries (income)" -> {
				OrderDetailDAO daoModel = new OrderDetailDAO();
				statsValues = daoModel.getTotalPriceOfSoldNecessariesByMonthAndYear(month, year);
			}
		}

		return statsValues;
	}

	private void statisticActionImp() {
		try {
			String statsOption = String.valueOf(statisticPanel.getStatisticOptions().getSelectedItem());
			ArrayList<Number> values = getStatisticValues(statsOption);
			ArrayList<String> rowKeys = loadRowKeys(statsOption);

			int year = (int) statisticPanel.getYearOptions().getSelectedItem();
			int month = (int) statisticPanel.getMonthOptions().getSelectedItem();
			String chartTitle = createChartTitleFrom(statsOption, month, year);

			statisticPanel.setChartTitle(chartTitle);
			statisticPanel.setValues(values, rowKeys, loadColumnKeys(statsOption));
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private ArrayList<String> loadRowKeys(String statsOption)
	throws DBConnectionException {
		ArrayList<String> rowKeys = new ArrayList<>();

		switch (statsOption) {
			case "The number of people at each state",
					"The number of changing state" -> rowKeys.addAll(List.of(User.STATUS_NAMES));

			case "The number of recoveries" -> rowKeys.add("Recovery");
			case "The total amount of debt" -> rowKeys.add("Total amount");
			case "Statistic of consumption of necessaries (quantity)",
					"Statistic of consumption of necessaries (income)" -> {
				int month = (int) statisticPanel.getMonthOptions().getSelectedItem();
				int year = (int) statisticPanel.getYearOptions().getSelectedItem();
				OrderDetailDAO daoModel = new OrderDetailDAO();
				ArrayList<String> necessariesNameList = daoModel.getAllNecessariesNamesByMonthAndYear(month, year);

				rowKeys.addAll(necessariesNameList);
			}
		}

		return rowKeys;
	}

	private ArrayList<String> loadColumnKeys(String statsOption) {
		ArrayList<String> columnKeys = new ArrayList<>();

		if (statsOption.equals("The number of changing state")) {
			columnKeys.add("Before");
			columnKeys.add("After");
		} else {
			columnKeys.add("");
		}

		return columnKeys;
	}

	private String createChartTitleFrom(String statsOption, int month, int year) {
		Month monthInstance = Month.of(month);
		int index = statsOption.indexOf(" (");  // remove " (quantity)" or " (price)"
		if (index != -1)
			statsOption = statsOption.substring(0, index);

		statsOption += " in %s, %d".formatted(monthInstance.getDisplayName(TextStyle.FULL, Locale.ENGLISH), year);
		return statsOption;
	}
}
