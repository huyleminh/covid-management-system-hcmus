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

	public List<Pair<Timestamp, OrderDetail>> getAllByUserId(Integer userId) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Pair<Timestamp, OrderDetail>> orderDetailList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				StringBuilder sqlStatement = new StringBuilder().append("SELECT ")
																.append("o.createdDate AS 'createdDate', ")
																.append("od.*")
//														 		.append("od.necessariesName AS 'necessariesName', ")
//														 		.append("od.price AS 'price', ")
//														 		.append("od.quantity AS 'quantity' ")
																.append("FROM COVID_MANAGEMENT.Order o JOIN COVID_MANAGEMENT.OrderDetail od ")
																.append("ON o.orderId = od.orderId ")
																.append("WHERE o.userId = ?;");

				preparedStatement = connection.prepareStatement(sqlStatement.toString());

				preparedStatement.setInt(1, userId);
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					orderDetailList.add(new Pair<>(
							resultSet.getTimestamp("createdDate"),
							new OrderDetail(
									resultSet.getInt("detailNo"),
									resultSet.getInt("orderId"),
									resultSet.getNString("necessariesName"),
									resultSet.getInt("price"),
									resultSet.getByte("quantity")
							)
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> OrderDetailDAO.java - line 55 <<<");
				e.printStackTrace();

				orderDetailList.clear();
				orderDetailList.add(new Pair<>(null, OrderDetail.emptyOrderDetail));
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> OrderDetailDAO.java - line 65 <<<");

						orderDetailList.clear();
						orderDetailList.add(new Pair<>(null, OrderDetail.emptyOrderDetail));
					}
				}
			}
		} else {
			orderDetailList.add(new Pair<>(null, OrderDetail.emptyOrderDetail));
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
