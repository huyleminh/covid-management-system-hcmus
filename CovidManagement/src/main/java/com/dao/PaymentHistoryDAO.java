package com.dao;

import com.models.PaymentHistory;
import com.utilities.SingletonDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentHistoryDAO implements DAO<PaymentHistory, Integer> {
	@Override
	public List<PaymentHistory> getAll() {
		return null;
	}

	public List<PaymentHistory> getAllByUserId(Integer userId) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<PaymentHistory> paymentHistoryList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				final String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.PaymentHistory WHERE userId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, userId);
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					paymentHistoryList.add(new PaymentHistory(
							resultSet.getInt("historyId"),
							resultSet.getInt("userId"),
							resultSet.getTimestamp("date"),
							resultSet.getInt("paymentAmount")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> PaymentHistoryDAO.java - line 43 <<<");
				e.printStackTrace();

				paymentHistoryList.clear();
				paymentHistoryList.add(PaymentHistory.emptyInstance);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> PaymentHistoryDAO.java - line 53 <<<");

						paymentHistoryList.clear();
						paymentHistoryList.add(PaymentHistory.emptyInstance);
					}
				}
			}
		} else {
			paymentHistoryList.add(PaymentHistory.emptyInstance);
		}

		return paymentHistoryList;
	}

	@Override
	public Optional<PaymentHistory> get(Integer id) {
		return Optional.empty();
	}

	@Override
	public boolean create(PaymentHistory entity) {
		return false;
	}

	@Override
	public boolean update(PaymentHistory entity) {
		return false;
	}

	@Override
	public boolean delete(PaymentHistory entity) {
		return false;
	}
}
