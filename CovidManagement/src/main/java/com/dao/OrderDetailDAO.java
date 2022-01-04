package com.dao;

import com.models.OrderDetail;
import com.utilities.Pair;
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
