package covid_management.dao;

import covid_management.models.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class OrderDAO implements DAO<Order, Integer> {
	private static final Logger logger = LogManager.getLogger(OrderDAO.class);

	@Override
	public ArrayList<Order> getAll() throws DBConnectionException {
		return null;
	}

	@Override
	public Optional<Order> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void create(Order entity) throws DBConnectionException {

	}

	public void create(
			Integer userId,
			ArrayList<Integer> necessariesIdList,
			ArrayList<String> necessariesNameList,
			ArrayList<Integer> priceList,
			ArrayList<Byte> quantityList,
			Integer totalPrice
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

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
			preparedStatementInsertOrder.executeUpdate();
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

				preparedStatementInsertOrderDetail.executeBatch();
				preparedStatementInsertDebt.executeUpdate();

				connection.commit();
			} else {
				connection.rollback();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());

			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error(ex.getMessage());
				logger.error(ex.getStackTrace());
			}

			throw DBConnectionException.INSTANCE;
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
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}

	@Override
	public void update(Order entity) throws DBConnectionException {

	}

	@Override
	public void delete(Order entity) throws DBConnectionException {

	}

	public ArrayList<Number> getTotalPriceByMonthAndYear(int month, int year) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Number> statsValues = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT totalPrice FROM COVID_MANAGEMENT.Order" +
					" WHERE YEAR(createdDate) = ? AND MONTH(createdDate) = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, year);
			preparedStatement.setInt(2, month);

			ResultSet resultSet = preparedStatement.executeQuery();
			int totalPrice = 0;

			while (resultSet.next())
				totalPrice += resultSet.getInt("totalPrice");

			statsValues.add(totalPrice);
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

		return statsValues;
	}
}
