package covid_management.dao;

import covid_management.models.OrderDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class OrderDetailDAO implements DAO<OrderDetail, Integer> {
	private static final Logger logger = LogManager.getLogger(OrderDetailDAO.class);

	@Override
	public ArrayList<OrderDetail> getAll() throws DBConnectionException {
		return null;
	}

	public ArrayList<OrderDetail> getAllByUserId(Integer userId) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<OrderDetail> orderDetailList = new ArrayList<>();
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
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
					logger.error(e.getStackTrace());
				}
			}
		}

		return orderDetailList;
	}

	public ArrayList<String> getAllNecessariesNamesByMonthAndYear(int month, int year)
	throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<String> necessariesNameList = new ArrayList<>();
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
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return necessariesNameList;
	}

	public ArrayList<Number> getAllQuantityOfSoldNecessariesByMonthAndYear(int month, int year)
	throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Number> statsValue = new ArrayList<>();
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
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return statsValue;
	}

	public ArrayList<Number> getTotalPriceOfSoldNecessariesByMonthAndYear(int month, int year)
	throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Number> statsValue = new ArrayList<>();
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
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return statsValue;
	}

	@Override
	public Optional<OrderDetail> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void create(OrderDetail entity) throws DBConnectionException {

	}

	@Override
	public void update(OrderDetail entity) throws DBConnectionException {

	}

	@Override
	public void delete(OrderDetail entity) throws DBConnectionException {

	}
}
