package com.dao;

import com.models.OrderDetail;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDetailDAO implements DAO<OrderDetail, Integer> {

	@Override
	public List<OrderDetail> getAll() {
		return null;
	}

	public List<OrderDetail> getAllByUserId(Integer userId) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<OrderDetail> orderDetailList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT od.* FROM COVID_MANAGEMENT.OrderDetail od " +
						"JOIN COVID_MANAGEMENT.Order o ON od.orderId = o.orderId WHERE o.userId = ?";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, userId);
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					orderDetailList.add(
							new OrderDetail(
									resultSet.getInt("detailNo"),
									resultSet.getInt("orderId"),
									resultSet.getInt("necessariesId"),
									resultSet.getNString("necessariesName"),
									resultSet.getInt("price"),
									resultSet.getByte("quantity"),
									resultSet.getTimestamp("purchasedAt")
							)
					);
				}
			} catch (SQLException e) {
				System.out.println(">>> OrderDetailDAO.java - line 55 <<<");
				e.printStackTrace();

				orderDetailList.clear();
				orderDetailList.add(OrderDetail.emptyInstance);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> OrderDetailDAO.java - line 65 <<<");

						orderDetailList.clear();
						orderDetailList.add(OrderDetail.emptyInstance);
					}
				}
			}
		} else {
			orderDetailList.add(OrderDetail.emptyInstance);
		}

		return orderDetailList;
	}

	public ArrayList<String> getAllNecessariesNamesByMonthAndYear(int month, int year) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<String> necessariesNameList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT DISTINCT necessariesName FROM COVID_MANAGEMENT.OrderDetail" +
						" WHERE YEAR(purchasedAt) = ? AND MONTH(purchasedAt) = ?" +
						" ORDER BY necessariesName";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setInt(1, year);
				preparedStatement.setInt(2, month);
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next())
					necessariesNameList.add(resultSet.getNString(1));
			} catch (SQLException e) {
				System.out.println(">>> OrderDetailDAO.java getAllNecessariesNamesByMonthAndYear(int, int) - catch block <<<");
				e.printStackTrace();

				necessariesNameList.clear();
				necessariesNameList.add("empty");
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> OrderDetailDAO.java getAllNecessariesNamesByMonthAndYear(int, int) - finally block <<<");

						necessariesNameList.clear();
						necessariesNameList.add("empty");
					}
				}
			}
		} else {
			necessariesNameList.add("empty");
		}

		return necessariesNameList;
	}

	public ArrayList<Number> getAllQuantityOfSoldNecessariesByMonthAndYear(int month, int year) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Number> statsValue = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT COUNT(*) AS 'count' FROM COVID_MANAGEMENT.OrderDetail" +
						" WHERE YEAR(purchasedAt) = ? AND MONTH(purchasedAt) = ? " +
						" GROUP BY necessariesName ORDER BY necessariesName";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setInt(1, year);
				preparedStatement.setInt(2, month);

				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next())
					statsValue.add(resultSet.getInt(1));

				if (statsValue.isEmpty())
					statsValue.add(0);
			} catch (SQLException e) {
				System.out.println(">>> OrderDetailDAO.java getAllQuantityOfSoldNecessariesByMonthAndYear(int, int) - catch block <<<");
				e.printStackTrace();
				statsValue.clear();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> OrderDetailDAO.java getAllQuantityOfSoldNecessariesByMonthAndYear(int, int) - finally block <<<");
						statsValue.clear();
					}
				}
			}
		}

		return statsValue;
	}

	public ArrayList<Number> getTotalPriceOfSoldNecessariesByMonthAndYear(int month, int year) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Number> statsValue = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT SUM(price) AS 'totalPrice' FROM COVID_MANAGEMENT.OrderDetail" +
						" WHERE YEAR(purchasedAt) = ? AND MONTH(purchasedAt) = ? " +
						" GROUP BY necessariesName ORDER BY necessariesName";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setInt(1, year);
				preparedStatement.setInt(2, month);

				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next())
					statsValue.add(resultSet.getInt(1));

				if (statsValue.isEmpty())
					statsValue.add(0);
			} catch (SQLException e) {
				System.out.println(">>> OrderDetailDAO.java getTotalPriceOfSoldNecessariesByMonthAndYear(int, int) - catch block <<<");
				e.printStackTrace();
				statsValue.clear();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> OrderDetailDAO.java getTotalPriceOfSoldNecessariesByMonthAndYear(int, int) - finally block <<<");
						statsValue.clear();
					}
				}
			}
		}

		return statsValue;
	}

	@Override
	public Optional<OrderDetail> get(Integer id) {
		return Optional.empty();
	}

	@Override
	public boolean create(OrderDetail entity) {
		return false;
	}

	@Override
	public boolean update(OrderDetail entity) {
		return false;
	}

	@Override
	public boolean delete(OrderDetail entity) {
		return false;
	}
}
