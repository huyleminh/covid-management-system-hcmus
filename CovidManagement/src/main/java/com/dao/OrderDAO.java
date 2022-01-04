package com.dao;

import com.models.*;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO implements DAO<Order, Integer> {
	@Override
	public List<Order> getAll() {
		return null;
	}

	@Override
	public Optional<Order> get(Integer id) {
		return Optional.empty();
	}

	@Override
	public boolean create(Order entity) {
		return false;
	}

	public boolean create(
			Integer userId,
			ArrayList<Integer> necessariesIdList,
			ArrayList<String> necessariesNameList,
			ArrayList<Integer> priceList,
			ArrayList<Byte> quantityList,
			Integer totalPrice
	) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		boolean isCreated = false;

		if (connection != null) {
			PreparedStatement preparedStatementInsertOrder = null;
			PreparedStatement preparedStatementInsertOrderDetail = null;
			PreparedStatement preparedStatementInsertDebt = null;

			try {
				Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
				String insertOrderStatement = "INSERT COVID_MANAGEMENT.Order " +
						"(userId, createdDate, totalPrice) VALUES (?, ?, ?)";

				preparedStatementInsertOrder = connection.prepareStatement(
						insertOrderStatement,
						Statement.RETURN_GENERATED_KEYS
				);
				preparedStatementInsertOrder.setInt(1, userId);
				preparedStatementInsertOrder.setTimestamp(2, currentTimestamp);
				preparedStatementInsertOrder.setInt(3, totalPrice);

				connection.setAutoCommit(false);
				int insertOrderRowAffected = preparedStatementInsertOrder.executeUpdate();
				ResultSet generatedKeys = preparedStatementInsertOrder.getGeneratedKeys();

				if (generatedKeys.next()) {
					int orderId = generatedKeys.getInt(1);

					String insertOrderDetailStatement = "INSERT COVID_MANAGEMENT.OrderDetail " +
							"(orderId, necessariesId, necessariesName, price, quantity, purchasedAt) " +
							"VALUES (?, ?, ?, ?, ?, ?)";

					String insertDebtStatement = "INSERT COVID_MANAGEMENT.Debt " +
							"(userId, debtDate, totalDebt) VALUES (?, ?, ?)";

					preparedStatementInsertOrderDetail = connection.prepareStatement(insertOrderDetailStatement);
					preparedStatementInsertDebt = connection.prepareStatement(insertDebtStatement);

					int countOrderDetail = necessariesIdList.size();
					for (int i = 0; i < countOrderDetail; i++) {
						preparedStatementInsertOrderDetail.setInt(1, orderId);
						preparedStatementInsertOrderDetail.setInt(2, necessariesIdList.get(i));
						preparedStatementInsertOrderDetail.setNString(3, necessariesNameList.get(i));
						preparedStatementInsertOrderDetail.setInt(4, priceList.get(i));
						preparedStatementInsertOrderDetail.setByte(5, quantityList.get(i));
						preparedStatementInsertOrderDetail.setTimestamp(6, currentTimestamp);
						preparedStatementInsertOrderDetail.addBatch();
					}

					preparedStatementInsertDebt.setInt(1, userId);
					preparedStatementInsertDebt.setTimestamp(2, currentTimestamp);
					preparedStatementInsertDebt.setInt(3, totalPrice);

					int[] insertUserHistoryRowsAffected = preparedStatementInsertOrderDetail.executeBatch();
					int insertAccountRowAffected = preparedStatementInsertDebt.executeUpdate();

					connection.commit();
					isCreated = true;
				} else {
					connection.rollback();
				}
			} catch (SQLException e) {
				System.out.println(">>> OrderDAO.java - create(Integer userId, ArrayList<Integer> necessariesIdLst, ArrayList<String> necessariesNameLit, ArrayList<Integer>, ArrayList<Byte>, Integer) method - catch block <<<");
				e.printStackTrace();

				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

				isCreated = false;
			} finally {
				try {
					connection.setAutoCommit(true);
					if (preparedStatementInsertOrder != null)
						preparedStatementInsertOrder.close();
					if (preparedStatementInsertOrderDetail != null)
						preparedStatementInsertOrderDetail.close();
					if (preparedStatementInsertDebt != null)
						preparedStatementInsertDebt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println(">>> OrderDAO.java - create(Integer userId, ArrayList<Integer>, ArrayList<String>, ArrayList<Integer>, ArrayList<Byte>, Integer) method - finally block <<<");
					isCreated = false;
				}
			}
		}

		return isCreated;
	}

	@Override
	public boolean update(Order entity) {
		return false;
	}

	@Override
	public boolean delete(Order entity) {
		return false;
	}
}
