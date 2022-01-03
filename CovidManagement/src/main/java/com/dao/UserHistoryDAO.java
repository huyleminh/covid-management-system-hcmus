package com.dao;

import com.models.UserHistory;
import com.utilities.SingletonDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
