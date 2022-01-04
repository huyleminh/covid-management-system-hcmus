package com.dao;

import com.models.Debt;
import com.utilities.SingletonDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DebtDAO implements DAO<Debt, Integer> {
	@Override
	public List<Debt> getAll() {
		return null;
	}

	public List<Debt> getAllByUserId(Integer userId) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Debt> debtList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				final String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Debt WHERE userId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, userId);
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					debtList.add(new Debt(
							resultSet.getInt("debtId"),
							resultSet.getInt("userId"),
							resultSet.getTimestamp("debtDate"),
							resultSet.getInt("totalDebt")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> DebtDAO.java - line 43 <<<");
				e.printStackTrace();

				debtList.clear();
				debtList.add(Debt.emptyInstance);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> DebtDAO.java - line 53 <<<");

						debtList.clear();
						debtList.add(Debt.emptyInstance);
					}
				}
			}
		} else {
			debtList.add(Debt.emptyInstance);
		}

		return debtList;
	}

	@Override
	public Optional<Debt> get(Integer id) {
		return Optional.empty();
	}

	@Override
	public boolean create(Debt entity) {
		return false;
	}

	@Override
	public boolean update(Debt entity) {
		return false;
	}

	@Override
	public boolean delete(Debt entity) {
		return false;
	}

	public int getTotalDebtByUsedId(Integer userId) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		int totalDebt = -1;

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				final String sqlStatement = "SELECT SUM(totalDebt) AS 'totalDebt' " +
						"FROM COVID_MANAGEMENT.Debt WHERE userId = ?";

				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, userId);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next())
					totalDebt = resultSet.getInt(1);
				else
					totalDebt = 0;
			} catch (SQLException e) {
				System.out.println(">>> DebtDAO.java - getTotalDebtByUsedId(Integer) - catch block <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> DebtDAO.java - getTotalDebtByUsedId(Integer) - finally block <<<");
					}
				}
			}
		}

		return totalDebt;
	}
}
