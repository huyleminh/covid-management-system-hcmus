package com.dao;

import com.models.NecessariesHistory;
import com.utilities.SingletonDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NecessariesHistoryDAO implements DAO<NecessariesHistory, Integer> {
	@Override
	public List<NecessariesHistory> getAll() {
		return null;
	}

	public List<NecessariesHistory> getAllByManagerUsername(String managerUsername) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<NecessariesHistory> necessariesHistoryList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				final String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.NecessariesHistory WHERE managerUsername = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setString(1, managerUsername);
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					necessariesHistoryList.add(new NecessariesHistory(
							resultSet.getInt("historyId"),
							resultSet.getString("managerUsername"),
							resultSet.getTimestamp("date"),
							resultSet.getNString("description"),
							resultSet.getByte("operationType")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> NecessariesHistoryDAO.java - line 44 <<<");
				e.printStackTrace();

				necessariesHistoryList.clear();
				necessariesHistoryList.add(NecessariesHistory.emptyInstance);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> NecessariesHistoryDAO.java - line 54 <<<");

						necessariesHistoryList.clear();
						necessariesHistoryList.add(NecessariesHistory.emptyInstance);
					}
				}
			}
		} else {
			necessariesHistoryList.add(NecessariesHistory.emptyInstance);
		}

		return necessariesHistoryList;
	}

	@Override
	public Optional<NecessariesHistory> get(Integer id) {
		return Optional.empty();
	}

	@Override
	public boolean create(NecessariesHistory entity) {
		return false;
	}

	@Override
	public boolean update(NecessariesHistory entity) {
		return false;
	}

	@Override
	public boolean delete(NecessariesHistory entity) {
		return false;
	}
}
