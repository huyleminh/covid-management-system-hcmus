package com.dao;

import com.models.User;
import com.models.UserHistory;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class UserHistoryDAO implements DAO<UserHistory, Integer> {
	@Override
	public List<UserHistory> getAll() {
		return null;
	}

	public List<UserHistory> getAllByManagerUsername(String managerUsername) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<UserHistory> userHistoryList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				final String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.UserHistory WHERE managerUsername = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setString(1, managerUsername);
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					userHistoryList.add(new UserHistory(
							resultSet.getInt("historyId"),
							resultSet.getString("managerUsername"),
							resultSet.getInt("userId"),
							resultSet.getTimestamp("date"),
							resultSet.getNString("description"),
							resultSet.getByte("operationType")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> UserHistoryDAO.java - line 45 <<<");
				e.printStackTrace();

				userHistoryList.clear();
				userHistoryList.add(UserHistory.emptyInstance);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserHistoryDAO.java - line 55 <<<");

						userHistoryList.clear();
						userHistoryList.add(UserHistory.emptyInstance);
					}
				}
			}
		} else {
			userHistoryList.add(UserHistory.emptyInstance);
		}

		return userHistoryList;
	}

	public List<UserHistory> getAllManagedHistoryByUserId(Integer userId) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<UserHistory> userHistoryList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				final String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.UserHistory " +
						"WHERE userId = ? AND operationType = ? AND operationType = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, userId);
				preparedStatement.setByte(2, UserHistory.DIRECTLY_CHANGE_STATUS);
				preparedStatement.setByte(3, UserHistory.CHANGE_QUARANTINE);
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					userHistoryList.add(new UserHistory(
							resultSet.getInt("historyId"),
							resultSet.getString("managerUsername"),
							resultSet.getInt("userId"),
							resultSet.getTimestamp("date"),
							resultSet.getNString("description"),
							resultSet.getByte("operationType")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> UserHistoryDAO.java - line 97 <<<");
				e.printStackTrace();

				userHistoryList.clear();
				userHistoryList.add(UserHistory.emptyInstance);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserHistoryDAO.java - line 107 <<<");

						userHistoryList.clear();
						userHistoryList.add(UserHistory.emptyInstance);
					}
				}
			}
		} else {
			userHistoryList.add(UserHistory.emptyInstance);
		}

		return userHistoryList;
	}

	public ArrayList<Number> getNumberOfPeopleAtEachStateByMonthAndYear(int month, int year) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Number> statsValues = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				int nextMonth = (month == 12) ? 1 : month + 1;
				int yearOfNextMonth =  (month == 12) ? year + 1 : year;
				Timestamp firstDayOfNextMonth = Timestamp.valueOf(
						"%d-%d-01 00:00:00".formatted(yearOfNextMonth, nextMonth)
				);

				String sqlStatement = "SELECT uh.historyId, uh.userId, uh.description, uh.operationType, u.status" +
						" FROM COVID_MANAGEMENT.UserHistory uh LEFT JOIN COVID_MANAGEMENT.User u" +
						" ON uh.userId = u.userId" +
						" WHERE (operationType = ? OR operationType = ? OR operationType = ?) AND uh.date < ?" +
						" ORDER BY uh.date DESC";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setInt(1, UserHistory.ADD_NEW_USER);
				preparedStatement.setInt(2, UserHistory.DIRECTLY_CHANGE_STATUS);
				preparedStatement.setInt(3, UserHistory.INDIRECTLY_CHANGE_STATUS);
				preparedStatement.setTimestamp(4, firstDayOfNextMonth);

				ResultSet resultSet = preparedStatement.executeQuery();
				int[] peopleCountAtEachState = new int[User.STATUS_NAMES.length];

				HashMap<Integer, Boolean> marker = new HashMap<>();
				while (resultSet.next()) {
					int userId = resultSet.getInt("userId");

					if (!marker.containsKey(userId)) {
						if (resultSet.getInt("operationType") == UserHistory.ADD_NEW_USER) {
							++peopleCountAtEachState[resultSet.getByte("status")];
						} else {
							String description = resultSet.getNString("description");
							int startIndex = description.indexOf("sang ");
							int endIndex = description.indexOf(" của người dùng");
							String statusAsString = description.substring(startIndex + 5, endIndex);

							++peopleCountAtEachState[User.byteValueOfStatus(statusAsString)];
						}

						marker.put(userId, true);
					}
				}

				for (int count : peopleCountAtEachState)
					statsValues.add(count);

			} catch (SQLException e) {
				System.out.println(">>> UserHistoryDAO.java - getNumberOfPeopleAtEachStateByMonthAndYear(int, int) - catch block <<<");
				e.printStackTrace();
				statsValues.clear();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserHistoryDAO.java - getNumberOfPeopleAtEachStateByMonthAndYear(int, int) - finally block <<<");
						statsValues.clear();
					}
				}
			}
		}

		return statsValues;
	}

	public ArrayList<Number> getNumberOfChangingStateByMonthAndYear(int month, int year) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Number> statsValues = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT description FROM COVID_MANAGEMENT.UserHistory" +
						" WHERE (operationType = ? OR operationType = ?) AND YEAR(date) = ? AND MONTH(date) = ?";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setInt(1, UserHistory.DIRECTLY_CHANGE_STATUS);
				preparedStatement.setInt(2, UserHistory.INDIRECTLY_CHANGE_STATUS);
				preparedStatement.setInt(3, year);
				preparedStatement.setInt(4, month);

				ResultSet resultSet = preparedStatement.executeQuery();
				int[] changingStateCount = new int[User.STATUS_NAMES.length * 2];

				while (resultSet.next()) {
					String description = resultSet.getNString(1);
					int index = description.indexOf(" sang ");
					String oldStatusAsString = description.substring(description.indexOf("từ ") + 2, index);
					String newStatusAsString = description.substring(index + 6, description.indexOf(" của người dùng "));

					++changingStateCount[User.byteValueOfStatus(oldStatusAsString)];
					++changingStateCount[User.byteValueOfStatus(newStatusAsString) + User.STATUS_NAMES.length];
				}

				for (int count : changingStateCount)
					statsValues.add(count);

			} catch (SQLException e) {
				System.out.println(">>> UserHistoryDAO.java - getNumberOfChangingStateByRangeMonth(int, int) - catch block <<<");
				e.printStackTrace();
				statsValues.clear();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserHistoryDAO.java - getNumberOfChangingStateByRangeMonth(int, int) - finally block <<<");
						statsValues.clear();
					}
				}
			}
		}

		return statsValues;
	}

	public ArrayList<Number> getNumberOfRecoveryPeopleByMonthAndYear(int month, int year) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Number> statsValues = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT description FROM COVID_MANAGEMENT.UserHistory" +
						" WHERE operationType = ? AND YEAR(date) = ? AND MONTH(date) = ?";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setInt(1, UserHistory.DIRECTLY_CHANGE_STATUS);
				preparedStatement.setInt(2, year);
				preparedStatement.setInt(3, month);

				ResultSet resultSet = preparedStatement.executeQuery();
				int recoveryPeopleCount = 0;

				while (resultSet.next()) {
					String description = resultSet.getNString(1);
					int indexOfRecovery = description.indexOf("sang " + User.STATUS_NAMES[0]);
					if (indexOfRecovery != -1)
						++recoveryPeopleCount;
				}

				statsValues.add(recoveryPeopleCount);
			} catch (SQLException e) {
				System.out.println(">>> UserHistoryDAO.java - getNumberOfRecoveryPeopleByRangeMonth(int, int) - catch block <<<");
				e.printStackTrace();
				statsValues.clear();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserHistoryDAO.java - getNumberOfRecoveryPeopleByRangeMonth(int, int) - finally block <<<");
						statsValues.clear();
					}
				}
			}
		}

		return statsValues;
	}

	@Override
	public Optional<UserHistory> get(Integer id) {
		return Optional.empty();
	}

	@Override
	public boolean create(UserHistory entity) {
		return false;
	}

	@Override
	public boolean update(UserHistory entity) {
		return false;
	}

	@Override
	public boolean delete(UserHistory entity) {
		return false;
	}
}
